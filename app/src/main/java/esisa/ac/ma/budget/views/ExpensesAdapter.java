package esisa.ac.ma.budget.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

import esisa.ac.ma.budget.R;
import esisa.ac.ma.budget.dao.ExpensesDao;
import esisa.ac.ma.budget.entities.Expenses;
import lombok.val;

public class ExpensesAdapter  extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder>  {

    public interface EditClickListener {
        public void setOnEditListener(View view, int position);
    }
    private EditClickListener editClickListener;
    private ExpensesDao expensesDao;

    public void setEditClickListener(EditClickListener editClickListener) {
        this.editClickListener = editClickListener;
    }

    public void setDataModel(Vector<Expenses> dataModel) {
        this.dataModel = dataModel;
    }

    private Vector<Expenses> cache;
    private Vector<Expenses> dataModel = new Vector<>();
    private Context context;

    public Context getContext() {
        return context;
    }

    public ExpensesAdapter(Context context) {
        this.context = context;
        this.expensesDao = new ExpensesDao(context);
        /*Expenses expenses = Expenses.builder()
                .label("ACHAT ESPADRINE")
                .price(500)
                .date("23-03-2023")
                .build();
        expensesDao.insert(expenses);
        expenses = Expenses.builder()
                .label("JUS")
                .price(18)
                .date("23-03-2023")
                .build();
        expensesDao.insert(expenses);*/
        dataModel = expensesDao.getAll();
        cache = dataModel;
    }

    public Vector<Expenses> getDataModel() {
        return dataModel;
    }

    public void reset() {
        dataModel = cache;
        notifyDataSetChanged();
    }
    public void add(Expenses expence) {
        expensesDao.insert(expence);
        dataModel.add(expence);
        notifyDataSetChanged();
    }

    public void impor(Expenses expence) {
        expensesDao.insert(expence);
        dataModel.add(expensesDao.getLast());
        notifyDataSetChanged();
    }
    public void orderByDate() {
        dataModel = expensesDao.getExpensesSortedByDate();
        notifyDataSetChanged();
    }
    public void orderByPrice() {
        dataModel = expensesDao.getExpensesSortedByPrice();
        notifyDataSetChanged();
    }
    public void remove(int position) {
        Expenses expenses =  dataModel.get(position);
        expensesDao.remove(expenses.getId());
        dataModel.remove(position);
        notifyDataSetChanged();
    }

    public void edit(Expenses oldExpence, Expenses expence) {
        expensesDao.update(oldExpence.getId(), expence);
        dataModel.forEach(expenses -> {
            if (oldExpence.getId() == expenses.getId()){
                expenses.setDate(expence.getDate());
                expenses.setLabel(expence.getLabel());
                expenses.setPrice(expence.getPrice());
            }
        });
        notifyDataSetChanged();
    }

    public void filter(String newText) {
        val filtredVector = new Vector<Expenses>();
        dataModel.forEach(e -> {
            if(e.getLabel().contains(newText) || e.getDate().contains(newText) || (e.getPrice()+"").contains(newText)){
                filtredVector.add(e);
            }
        });
        dataModel = filtredVector;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView label;
        TextView price;
        TextView date;
        ImageView edit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.price);
            edit = itemView.findViewById(R.id.edit);
            edit.setOnClickListener(view -> {
                editClickListener.setOnEditListener(view, getAdapterPosition());
            });
        }
    }
    @NonNull
    @Override
    public ExpensesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ExpensesAdapter.ViewHolder holder, int position) {
        holder.label.setText(dataModel.get(position).getLabel());
        holder.date.setText(dataModel.get(position).getDate());
        holder.price.setText(dataModel.get(position).getPrice() + "");
    }

    @Override
    public int getItemCount() {
        return dataModel.size();
    }
}
