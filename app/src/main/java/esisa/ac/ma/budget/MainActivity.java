package esisa.ac.ma.budget;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import esisa.ac.ma.budget.entities.Expenses;
import esisa.ac.ma.budget.views.EditActivity;
import esisa.ac.ma.budget.views.ExpensesAdapter;
import esisa.ac.ma.budget.views.LoginActivity;
import esisa.ac.ma.budget.views.PieChartActivity;
import esisa.ac.ma.budget.views.SwipeToDeleteCallback;
import lombok.val;

public class MainActivity extends AppCompatActivity {

    private ExpensesAdapter expensesAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton add;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         val launcher = registerForActivityResult(
                 new ActivityResultContracts.StartActivityForResult(), result -> {
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                        val expence = (Expenses)result.getData().getExtras().get("e");
                        val oldExpence = (Expenses)result.getData().getExtras().get("oldExpence");
                        if(result.getData().getAction().equals("add"))
                            expensesAdapter.add(expence);
                        else
                            expensesAdapter.edit(oldExpence, expence);
                    }
                 }
         );
        add = findViewById(R.id.add);
        add.setOnClickListener(view -> {
            Intent toEdit = new Intent(getApplicationContext(), EditActivity.class);
            launcher.launch(toEdit);
        });
        recyclerView = findViewById(R.id.recycler);
        expensesAdapter = new ExpensesAdapter(this);
        expensesAdapter.setEditClickListener(new ExpensesAdapter.EditClickListener() {
            @Override
            public void setOnEditListener(View view, int position) {
                val expence = expensesAdapter.getDataModel().get(position);
                Intent toEdit = new Intent(getApplicationContext(), EditActivity.class);
                toEdit.putExtra("edit", expence);
                launcher.launch(toEdit);
            }
        });
        recyclerView.setAdapter(expensesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(expensesAdapter);
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar, menu);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                expensesAdapter.reset();
                expensesAdapter.filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.putBoolean("email", false);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.menu_export:
                exportData();
                return true;
            case R.id.menu_import:
                importData();

            case R.id.menu_sort_date:
                expensesAdapter.orderByDate();
                return true;
            case R.id.menu_sort_price:
                expensesAdapter.orderByPrice();
               return true;
            case R.id.menu_pieChart:
                Intent toPieChart = new Intent(MainActivity.this, PieChartActivity.class);
                startActivity(toPieChart);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void exportData() {
        try {
            File file = new File(getExternalFilesDir(null), "expenses.csv");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write("Id, Label, Price, Date\n");
            for (Expenses expense : expensesAdapter.getDataModel()) {
                outputStreamWriter.write(expense.getId() + "," + expense.getLabel() + "," +
                        expense.getPrice() + "," + expense.getDate() + "\n");
            }
            outputStreamWriter.flush();
            outputStreamWriter.close();
            Toast.makeText(this, "Data exported successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void importData() {
        try {
            File file = new File(getExternalFilesDir(null), "expenses.csv");

            Scanner scanner = new Scanner(file);
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(",");
                Expenses expenses = Expenses.builder()
                        .label(fields[1])
                        .price(Float.parseFloat(fields[2]))
                        .date(fields[3])
                        .build();
                expensesAdapter.impor(expenses);
            }
            scanner.close();
            Toast.makeText(this, "Data imported successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}