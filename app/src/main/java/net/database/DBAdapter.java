package net.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import net.entity.Table_Favourite_Images_model;

public class DBAdapter {

    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "Suitdb.sql";

    // mainexercise table
    private static final String TABLE_NAME = "Change_fg_bg";
    private static final String ID = "id";
    private static final String FG_BG = "fg_bg";
    private static final String IMG_PATH = "image_path";

    private static final String TABLE_FAVOURITE ="Favourite_Images";

    private static final String KEY_ID = "Id";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_IMAGE_PATH = "image_path";

    // private static final String PHONE = "phone";
    //private static final String DOB = "dob";
    //private static final String IMAGE = "image";

   /* private static final String TABLE_NAME_MESSAGE = "message_table";
    private static final String TEXT_OR_IMAGE = "text_or_image";
    private static final String IMAGE_URL = "image_url";
    private static final String USER_ID = "user_id";
    private static final String MESSAGE_STATUS = "message_status";
    private static final String USER_TYPE = "user_type";
    private static final String SENDER_MESSAGE = "sender_message";
    private static final String RECEIVER_MESSAGE = "receiver_message";
    private static final String MESSAGE_TIME = "message_time";*/

    private final Context context;
    public static DatabaseHelper DBHelper;
    private static SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            /*final String Create_Reg =
                    "CREATE TABLE IF NOT EXISTS detail ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "name TEXT,"
                            + "address TEXT,"
                            + "phone TEXT);";
            db.execSQL(Create_Reg);*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    // ---opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // ---closes the database---
    public void close() {
        DBHelper.close();
    }

    public void saveData_Change_fg_bg(String fg_bg, String img_path) {
        try {

            db = DBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(FG_BG, fg_bg);
            values.put(IMG_PATH, img_path);
            //values.put(PHONE, phone);
            //values.put(DOB, dob);
            //values.put(IMAGE, image);

            db.insert(TABLE_NAME, null, values);
            db.close();

        } catch (Throwable t) {
            Log.i("Database", "Exception caught: " + t.getMessage(), t);
        }
    }

    public Cursor getdata_Change_fg_bg(String f_b) {

        //String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + FG_BG + " = " + fg_bg;

        db = DBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_NAME + " WHERE " + FG_BG + " = " + f_b, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        close();

       /* if (cursor.moveToFirst()) {
            do {
            } while (cursor.moveToNext());
        }*/
        return cursor;
    }

    public Cursor getdata() {

        String selectQuery = "SELECT  * FROM " + TABLE_NAME  ;

        db = DBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

       /* if (cursor != null) {
            cursor.moveToFirst();
        }
        close();*/

       if (cursor.moveToFirst()) {
            do {
            } while (cursor.moveToNext());
        }
        return cursor;
    }
    /*
    public void saveProfiledata(String status_time, String profile_time, String name, byte[] link) {
        try {

            db = DBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(STATUS_TIME, status_time);
            values.put(PROFILE_TIME, profile_time);
            values.put(NAME, name);
            values.put(PROFILE_IMAGE, link);

            db.insert(TABLE_NAME, null, values);
            db.close();
        } catch (Throwable t) {
            Log.i("Database", "Exception caught: " + t.getMessage(), t);
        }
    }
    */
    public void updatedata_Change_fg_bg(String fg_bg, String img_path) {

        try {
            db = DBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(FG_BG, fg_bg);
            values.put(IMG_PATH, img_path);
            // values.put(PHONE, phone);
             //values.put(DOB, dob);
            //values.put(IMAGE, image);

            db.update(TABLE_NAME, values, "fg_bg" + fg_bg, null);
            db.close();

        } catch (Throwable t) {
            Log.e("Database", "Exception caught: " + t.getMessage(), t);
        }
    }

    public void saveMessageData(byte[] image_url, String image_path) {

        try {

             db = DBHelper.getReadableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_IMAGE_URL, image_url);
            values.put(KEY_IMAGE_PATH, image_path);

            db.insert(TABLE_FAVOURITE, null, values);
            Log.e("added","added");
            db.close();

        } catch (Throwable t) {
            Log.e("Database", "Exception caught: " + t.getMessage(), t);
        }
    }

    public Table_Favourite_Images_model getFavouritr(String image_path) {

        db = DBHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVOURITE, new String[]{KEY_ID, KEY_IMAGE_URL,KEY_IMAGE_PATH}, KEY_IMAGE_PATH + "=? ", new String[]{image_path}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Table_Favourite_Images_model bullets1_model = new Table_Favourite_Images_model(cursor.getInt(0), cursor.getBlob(1),cursor.getString(2));
        return bullets1_model;
    }

    public long GetRowCountofTable() {

        db = DBHelper.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM " + TABLE_FAVOURITE;
        SQLiteStatement statement = db.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    /* public void saveDrawData(String msl_number,String mfacts) {
         try {

             SQLiteDatabase db = this.getReadableDatabase();
             ContentValues values = new ContentValues();

             values.put(KEY_MSL_NUMBER, msl_number);
             values.put(KEY_MFACTS, mfacts);


             db.insert(TABLE_FACTS, null, values);
             db.close();
             Log.e("inserted","inserted");
         } catch (Throwable t) {
             Log.i("Database", "Exception caught: " + t.getMessage(), t);
         }
     }*/
/**/

    public String getSingleFavData(String name) {

        String data;
        db = DBHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVOURITE, new String[]{KEY_ID, KEY_IMAGE_URL,KEY_IMAGE_PATH}, KEY_IMAGE_PATH + "=? ", new String[]{name}, null, null, null, null);
        if (cursor.moveToFirst()) {

            data = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH));
        }else{
            data=null;
        }

        /*Cursor cursor = db.rawQuery("select * FROM " + TABLE_FACTS + " where mfacts='" + name + "'", null);*/
        //Log.e("getdatafav",cursor.getString(1));



        // looping through all rows and adding to list
       /* if (cursor.moveToFirst()) {
            do {
                String name1 = cursor.getString(0);

            } while (cursor.moveToNext());
        }*/
        return data;
    }

    public void deleteDrawDetails(String image_path) {
        db = DBHelper.getReadableDatabase();

        try {

            db.delete(TABLE_FAVOURITE, KEY_IMAGE_PATH + " = ?", new String[]{image_path});
            Log.e("deleted", "deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cursor getFavData() {

        db = DBHelper.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITE;

        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    /*   public Cursor getFavData() {

           String selectQuery = "SELECT  * FROM " + TABLE_FACTS;
           SQLiteDatabase db = this.getReadableDatabase();

           Cursor cursor = db.rawQuery(selectQuery, null);

          *//* // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String link = cursor.getString(1);

            } while (cursor.moveToNext());
        }
*//*
        return cursor;
    }

*/

    public void deleteFavData() {

        db = DBHelper.getReadableDatabase();
        try {

            db.delete(TABLE_FAVOURITE, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}