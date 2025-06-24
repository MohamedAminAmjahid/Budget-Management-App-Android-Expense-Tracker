package esisa.ac.ma.budget.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Vector;

import esisa.ac.ma.budget.dal.Database;
import esisa.ac.ma.budget.entities.User;

public class UsersDao {
    Database database;
    SQLiteDatabase sqLiteDatabase;

    public UsersDao(Context context) {
        database = new Database(context);
        sqLiteDatabase = database.getWritableDatabase();
    }

    public long insert(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("mail", user.getMail());
        contentValues.put("password", user.getPassword());
        return sqLiteDatabase.insert(Database.TABLE_USERS, null, contentValues);
    }

    public Vector<User> getAll() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Database.TABLE_USERS, null);
        User user;
        Vector<User> usersVector = new Vector<>();
        while (cursor.moveToNext()) {
            user = User.builder()
                    .mail(cursor.getString(1))
                    .id(cursor.getInt(0))
                    .password(cursor.getString(2)).build();
            usersVector.add(user);
        }
        return usersVector;
    }

    public User get(String mail) {
        String selection =  "mail=?";
        String[] selectionArgs = {mail};
        Cursor cursor = sqLiteDatabase.query(Database.TABLE_USERS, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToNext()) {
            User user = User.builder()
                    .id(cursor.getInt(0))
                    .mail(cursor.getString(1))
                    .password(cursor.getString(2))
                    .build();
            cursor.close();
            return user;
        } else {
            cursor.close();
            return null;
        }
    }
}
