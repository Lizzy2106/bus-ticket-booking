package com.example.liaison.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.liaison.R;
import com.example.liaison.models.User;
import com.example.liaison.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    // here we create our table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACT_TABLE = "CREATE TABLE "+Util.TABLE_NAME+"(" +
                Util.KEY_ID+" INTEGER PRIMARY KEY,"+Util.KEY_NAME+" TEXT,"+
                Util.KEY_MAIL+" TEXT,"+Util.KEY_MDP+" TEXT,"+Util.KEY_TYPE+" TEXT"+ ")";
        db.execSQL(CREATE_CONTACT_TABLE);// run my script SQL
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop exist table
        String DROP_TABLE = String.valueOf(R.string.drop_table);
        db.execSQL(DROP_TABLE, new String [] {Util.DATABASE_NAME});

        //create table again
        onCreate(db);
    }

    // Implementation of our CRUD methods

    //Add user
    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, user.getName());
        values.put(Util.KEY_MAIL, user.getMail());
        values.put(Util.KEY_MDP, user.getMdp());
        values.put(Util.KEY_TYPE, user.getType());
        db.insert(Util.TABLE_NAME, null, values);
        db.close();
    }

    //Get user
    public User getUser(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME,
                new String[]{Util.KEY_ID, Util.KEY_NAME, Util.KEY_MAIL, Util.KEY_MDP, Util.KEY_TYPE},
                Util.KEY_ID+"=?", new String[]{String.valueOf(id)},
                null, null, null);
        //regarder si ce qui est retourné est nul sinon renvoyer le premier user
        if (cursor !=null){
            cursor.moveToFirst();
        }
        return new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
    }

    //Reserch User
    public User searchUser(String mail){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME,
                new String[]{Util.KEY_ID, Util.KEY_NAME, Util.KEY_MAIL, Util.KEY_MDP, Util.KEY_TYPE},
                Util.KEY_MAIL+"=?", new String[]{String.valueOf(mail)},
                null, null, null);
        //regarder si ce qui est retourné est nul sinon renvoyer le premier user
        if (cursor.moveToFirst()){
            return new User(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        }else {
            return null;
        }
    }

    //Get all Users
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        //Select all users
        String selectAll = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);
        //Loop through our data
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setName(cursor.getString(1));
                user.setMail(cursor.getString(2));
                user.setMdp(cursor.getString(3));
                user.setType(cursor.getString(4));

                //add user objects to our list
                userList.add(user);
            }while (cursor.moveToNext());
        }
        return userList;
    }

    //Update user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, user.getName());
        values.put(Util.KEY_MAIL, user.getMail());
        values.put(Util.KEY_MDP, user.getMdp());
        values.put(Util.KEY_TYPE, user.getType());
        return db.update(Util.TABLE_NAME, values, Util.KEY_ID + "=?",
                new String[]{String.valueOf(user.getId())});
    }

    //Delete single user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.TABLE_NAME, Util.KEY_ID + "=?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    //Get users count
    public int getCount() {
        String countQuery = "SELECT * FROM " + Util.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }
}
