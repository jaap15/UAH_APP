package edu.uah.uahnavigation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

/**
 * Created by Daniel on 1/11/2017.
 */

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    // Each database will have these four tables in them.
    public static final String TABLE_1 = "majors";
        public static final String TABLE_1_COL_1 = "id";
        public static final String TABLE_1_COL_2 = "name";
        public static final String TABLE_1_COL_3 = "description";
    public static final String TABLE_2 = "courses";
        public static final String TABLE_2_COL_1 = "id";
        public static final String TABLE_2_COL_2 = "major_id";
        public static final String TABLE_2_COL_3 = "rooms_id";
        public static final String TABLE_2_COL_4 = "crn";
        public static final String TABLE_2_COL_5 = "course";
        public static final String TABLE_2_COL_6 = "section";
        public static final String TABLE_2_COL_7 = "title";
        public static final String TABLE_2_COL_8 = "credits";
        public static final String TABLE_2_COL_9 = "days";
        public static final String TABLE_2_COL_10 = "start";
        public static final String TABLE_2_COL_11 = "end";
        public static final String TABLE_2_COL_12 = "instructor";
    public static final String TABLE_3 = "buildings";
        public static final String TABLE_3_COL_1 = "id";
        public static final String TABLE_3_COL_2 = "name";
        public static final String TABLE_3_COL_3 = "description";
        public static final String TABLE_3_COL_4 = "address";
        public static final String TABLE_3_COL_5 = "picture";
    public static final String TABLE_4 = "rooms";
        public static final String TABLE_4_COL_1 = "id";
        public static final String TABLE_4_COL_2 = "building_id";
        public static final String TABLE_4_COL_3 = "room_number";

    private static final String LOGTAG = "QWER";

    private static final String MAJORS_TABLE_CREATE =
            "CREATE TABLE " + TABLE_1 + " (" +
                    TABLE_1_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TABLE_1_COL_2 + " VARCHAR, " +
                    TABLE_1_COL_3 + " VARCHAR" +
                    ")";

    private static final String COURSES_TABLE_CREATE =
            "CREATE TABLE " + TABLE_2 + " (" +
                    TABLE_2_COL_1  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TABLE_2_COL_2  + " INTEGER, " +
                    TABLE_2_COL_3  + " INTEGER, " +
                    TABLE_2_COL_4  + " INTEGER, " +
                    TABLE_2_COL_5  + " VARCHAR, " +
                    TABLE_2_COL_6  + " VARCHAR, " +
                    TABLE_2_COL_7  + " VARCHAR, " +
                    TABLE_2_COL_8  + " INTEGER, " +
                    TABLE_2_COL_9  + " VARCHAR, " +
                    TABLE_2_COL_10 + " VARCHAR, " +
                    TABLE_2_COL_11 + " VARCHAR, " +
                    TABLE_2_COL_12 + " VARCHAR" +
                    ")";

    private static final String BUILDINGS_TABLE_CREATE =
            "CREATE TABLE " + TABLE_3 + " (" +
                    TABLE_3_COL_1  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TABLE_3_COL_2  + " VARCHAR, " +
                    TABLE_3_COL_3  + " VARCHAR, " +
                    TABLE_3_COL_4  + " VARCHAR, " +
                    TABLE_3_COL_5  + " PIXMAP" +
                    ")";

    private static final String ROOMS_TABLE_CREATE =
            "CREATE TABLE " + TABLE_4 + " (" +
                    TABLE_4_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TABLE_4_COL_2 + " INTEGER, " +
                    TABLE_4_COL_3 + " INTEGER" +
                    ")";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MAJORS_TABLE_CREATE);
        db.execSQL(COURSES_TABLE_CREATE);
        db.execSQL(BUILDINGS_TABLE_CREATE);
        db.execSQL(ROOMS_TABLE_CREATE);
        Log.i(LOGTAG, "Database has been created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_4);
        File dbFile = new File("data/data/edu.uah.uahnavigation/database/database.db");
        db.deleteDatabase(dbFile);
        onCreate(db);
        Log.i(LOGTAG, "Table has been updated");
    }
}
