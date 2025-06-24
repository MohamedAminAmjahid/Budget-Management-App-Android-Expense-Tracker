package esisa.ac.ma.budget.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import esisa.ac.ma.budget.R;
import esisa.ac.ma.budget.entities.Expenses;
import lombok.var;

public class EditActivity extends AppCompatActivity {

    private EditText label;
    private EditText price;
    private EditText date;
    private MaterialButton btnedit;
    private Expenses oldExp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        label = findViewById(R.id.txtlabel);
        price = findViewById(R.id.txtprice);
        date = findViewById(R.id.txtdate);
        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
                    builder.setTitleText("Sélectionnez une date");
                    MaterialDatePicker<Long> picker = builder.build();
                    picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                        @Override
                        public void onPositiveButtonClick(Long selectedDate) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                            Date date2 = new Date(selectedDate);
                            String formattedDate = sdf.format(date2);
                            date.setText(formattedDate);
                        }
                    });
                    picker.show(getSupportFragmentManager(), picker.toString());
                }
                return false;
            }
        });
        btnedit = findViewById(R.id.btnedit);
        try {
            var exp = (Expenses)getIntent().getExtras().get("edit");
                label.setText(exp.getLabel());
                price.setText(exp.getPrice() + "");
                date.setText(exp.getDate());
                btnedit.setText("EDIT");
                oldExp = exp;

        }catch (Exception e) {}

        btnedit.setOnClickListener(view -> {
            Intent toMain = new Intent();
            String ok = date.getText().toString();
            if(ok.equals("")){
                Calendar calendar = Calendar.getInstance();
                int jour = calendar.get(Calendar.DAY_OF_MONTH);
                int mois = calendar.get(Calendar.MONTH) + 1;
                int annee = calendar.get(Calendar.YEAR);

                ok = jour + "/" + mois + "/" + annee;
            }
            var expence = Expenses.builder()
                    .label(label.getText().toString())
                    .price(Float.parseFloat(price.getText().toString()))
                    .date(ok)
                    .build();
            //Calendar.getInstance().getTime();

            if(btnedit.getText().toString().equals("ADD")){
               toMain.setAction("add");
            }
            else{
                toMain.putExtra("oldExpence", oldExp);
                toMain.setAction("edit");
            }
            toMain.putExtra("e", expence);
            if(label.getText().toString().equals("")) {
                label.setText("Ce champ est obligatoire");
                label.setTextColor(Color.RED);
                label.setError("Ce champ est obligatoire");
                setResult(Activity.RESULT_CANCELED, toMain);
            }else{
                setResult(Activity.RESULT_OK, toMain);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}