package esisa.ac.ma.budget.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.Vector;

import esisa.ac.ma.budget.MainActivity;
import esisa.ac.ma.budget.R;
import esisa.ac.ma.budget.dao.ExpensesDao;
import esisa.ac.ma.budget.entities.Expenses;

public class PieChartActivity extends AppCompatActivity {
    private PieChart chart;
    private ExpensesDao expensesDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.piechart_activity);
        chart = findViewById(R.id.chart);
        expensesDao = new ExpensesDao(getBaseContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleRadius(61f);
        chart.setEntryLabelColor(Color.BLACK);
        chart.setEntryLabelTextSize(12f);
        setupChart();
    }
    private void setupChart() {
        Vector<Expenses> expenses = expensesDao.getAll();
        Vector<PieEntry> entries = new Vector<>();
        for (Expenses expense : expenses) {
            entries.add(new PieEntry(expense.getPrice(), expense.getLabel()));
        }
        PieDataSet dataSet = new PieDataSet(entries, "Expenses");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        chart.setData(data);
        chart.invalidate();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}