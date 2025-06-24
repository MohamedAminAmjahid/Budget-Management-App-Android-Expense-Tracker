package esisa.ac.ma.budget.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.Vector;

import esisa.ac.ma.budget.dal.Database;
import esisa.ac.ma.budget.entities.Expenses;
import esisa.ac.ma.budget.entities.User;

public class ExpensesDao {

    Database database;
    SQLiteDatabase sqLiteDatabase;

    public ExpensesDao(Context context) {
        database = new Database(context);
        sqLiteDatabase = database.getWritableDatabase();
    }

    public long insert(Expenses expenses) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("label", expenses.getLabel());
        contentValues.put("price", expenses.getPrice());
        contentValues.put("date", expenses.getDate());
        return sqLiteDatabase.insert(Database.TABLE_EXPENSES, null, contentValues);
    }

    public Vector<Expenses> getAll() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Database.TABLE_EXPENSES, null);
        Expenses expenses;
        Vector<Expenses> expensesVector = new Vector<>();
        while (cursor.moveToNext()) {
            expenses = Expenses.builder()
                    .date(cursor.getString(3))
                    .id(cursor.getInt(0))
                    .label(cursor.getString(1))
                    .price(cursor.getFloat(2)).build();
            expensesVector.add(expenses);
        }
        return expensesVector;
    }

    public long update(int oldExpenseID, Expenses expenses) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("label", expenses.getLabel());
        contentValues.put("price", expenses.getPrice());
        contentValues.put("date", expenses.getDate());
        String whereClause = "id=?";
        String[] whereArgs = { oldExpenseID + ""};

        return sqLiteDatabase.update(Database.TABLE_EXPENSES, contentValues, whereClause, whereArgs);
    }

    public long remove(int id) {
        String whereClause = "id=?";
        String[] whereArgs = {  id + ""};

        return sqLiteDatabase.delete(Database.TABLE_EXPENSES, whereClause, whereArgs);
    }

    public Expenses getLast() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Database.TABLE_EXPENSES+ " ORDER BY id DESC LIMIT 1", null);
        Expenses expenses;
        if (cursor.moveToNext()) {
            expenses = Expenses.builder()
                    .date(cursor.getString(3))
                    .id(cursor.getInt(0))
                    .label(cursor.getString(1))
                    .price(cursor.getFloat(2)).build();
            cursor.close();
            return expenses;
        } else {
            cursor.close();
            return null;
        }
    }

    public Vector<Expenses> getExpensesSortedByDate() {
        Cursor cursor = sqLiteDatabase.query(Database.TABLE_EXPENSES, null, null, null, null, null, "date" + " ASC");
        Vector<Expenses> expensesVector = new Vector<>();
        while (cursor.moveToNext()) {
            Expenses expenses = Expenses.builder()
                    .date(cursor.getString(3))
                    .id(cursor.getInt(0))
                    .label(cursor.getString(1))
                    .price(cursor.getFloat(2)).build();
            expensesVector.add(expenses);
        }
        cursor.close();
        return expensesVector;
    }

    public Vector<Expenses> getExpensesSortedByPrice() {
        Cursor cursor = sqLiteDatabase.query(Database.TABLE_EXPENSES, null, null, null, null, null, "price" + " ASC");
        Vector<Expenses> expensesVector = new Vector<>();
        while (cursor.moveToNext()) {
            Expenses expenses = Expenses.builder()
                    .date(cursor.getString(3))
                    .id(cursor.getInt(0))
                    .label(cursor.getString(1))
                    .price(cursor.getFloat(2)).build();
            expensesVector.add(expenses);
        }
        cursor.close();
        return expensesVector;
    }
}
