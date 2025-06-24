package esisa.ac.ma.budget.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    public static final String DB_NAME = "db_expenses";
    public static int VERSION = 1;
    public static final String TABLE_EXPENSES = "t_expenses";
    public static final String TABLE_USERS = "t_users";


    public Database(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSES + " (id INTEGER PRIMARY KEY AUTOINCREMENT, label TEXT, price FLOAT, date TEXT)" );
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, mail TEXT, password TEXT)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(sqLiteDatabase);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(sqLiteDatabase);
    }
}
