package com.example.uahmapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by Daniel on 11/5/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "student.db";
        public static final String TABLE_NAME = "student_table";
            public static final String COL_1 = "ID";
            public static final String COL_2 = "Name";
            public static final String COL_3 = "Surname";
            public static final String COL_4 = "Marks";
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
            public static final String TABLE_2_COL_6 = "title";
            public static final String TABLE_2_COL_7 = "credits";
            public static final String TABLE_2_COL_8 = "days";
            public static final String TABLE_2_COL_9 = "start";
            public static final String TABLE_2_COL_10 = "end";
            public static final String TABLE_2_COL_11 = "instructor";
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


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, SURNAME TEXT, MARKS INTEGER) ");
        this.createMajorsTable(db);
        this.createCoursesTable(db);
        this.createBuildingsTable(db);
        this.createRoomsTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_4);
        onCreate(db);
    }

    public void createMajorsTable(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, DESCRIPTION TEXT");
    }

    public void createCoursesTable(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, MAJORS_ID INTEGER, ROOMS_ID INTEGER, CRN INTEGER, COURSE TEXT, TITLE TEXT, CREDITS INTEGER, DAYS TEXT, START TEXT, END TEXT, INSTRUCTOR TEXT");
    }


    public void createBuildingsTable(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_3 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, DESCRIPTION TEXT, ADDRESS TEXT, PICTURE BLOB");
    }

    public void createRoomsTable(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_4 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, BUILDING_ID INTEGER, ROOM_NUMBER INTEGER");
    }

    // Insert into student_table
    public boolean insertData(String tableName, String name, String surname, String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, surname);
        contentValues.put(COL_4, marks);
        long result = db.insert(TABLE_NAME,null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    // Insert into Majors
    public boolean insertData(String tableName, String name, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_1_COL_2, name);
        contentValues.put(TABLE_1_COL_3, description);
        long result = db.insert(tableName,null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    // Insert into Courses
    public boolean insertData(String tableName, Integer major_id, Integer rooms_id, Integer crn, String course, String title, Integer credits, String days, String start, String end, String instructor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_2_COL_2, major_id);
        contentValues.put(TABLE_2_COL_3, rooms_id);
        contentValues.put(TABLE_2_COL_4, course);
        contentValues.put(TABLE_2_COL_5, title);
        contentValues.put(TABLE_2_COL_6, credits);
        contentValues.put(TABLE_2_COL_7, days);
        contentValues.put(TABLE_2_COL_8, start);
        contentValues.put(TABLE_2_COL_9, end);
        contentValues.put(TABLE_2_COL_10, instructor);
        long result = db.insert(tableName,null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    // Insert into Rooms
    public boolean insertData(String tableName, Integer buildings_id, Integer room_number) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_3_COL_2, buildings_id);
        contentValues.put(TABLE_3_COL_3, room_number);
        long result = db.insert(tableName,null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    // Insert into Buildings
    public boolean insertData(String tableName, String name, String description, String address, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_3_COL_2, name);
        contentValues.put(TABLE_3_COL_3, description);
        contentValues.put(TABLE_3_COL_4, address);
        contentValues.put(TABLE_3_COL_5, image);
        long result = db.insert(tableName,null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + tableName, null);
        return res;
    }
}
