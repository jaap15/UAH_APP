package edu.uah.uahnavigation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.uah.model.*;

import static android.database.DatabaseUtils.dumpCursorToString;

/**
 * Created by Daniel on 1/16/2017.
 */

public class DatabaseSource {

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;
    private static final String LOGTAG = "DBCREATE";
    private boolean first_open = false;

    private static final String[] majorsColumns = {
            DatabaseManager.TABLE_1_COL_1,
            DatabaseManager.TABLE_1_COL_2,
            DatabaseManager.TABLE_1_COL_3
    };

    private static final String[] coursesColumns = {
            DatabaseManager.TABLE_2_COL_1,
            DatabaseManager.TABLE_2_COL_2,
            DatabaseManager.TABLE_2_COL_3,
            DatabaseManager.TABLE_2_COL_4,
            DatabaseManager.TABLE_2_COL_5,
            DatabaseManager.TABLE_2_COL_6,
            DatabaseManager.TABLE_2_COL_7,
            DatabaseManager.TABLE_2_COL_8,
            DatabaseManager.TABLE_2_COL_9,
            DatabaseManager.TABLE_2_COL_10,
            DatabaseManager.TABLE_2_COL_11,
            DatabaseManager.TABLE_2_COL_12,
    };

    private static final String[] buildingsColumns = {
            DatabaseManager.TABLE_3_COL_1,
            DatabaseManager.TABLE_3_COL_2,
            DatabaseManager.TABLE_3_COL_3,
            DatabaseManager.TABLE_3_COL_4
    };

    private static final String[] roomsColumns = {
            DatabaseManager.TABLE_4_COL_1,
            DatabaseManager.TABLE_4_COL_2,
            DatabaseManager.TABLE_4_COL_3
    };

    public DatabaseSource(Context context) {
        dbhelper = new DatabaseManager(context);
    }

    public void open() {
        Log.i(LOGTAG, "Databased Opened");
        database = dbhelper.getWritableDatabase();
    }

    public boolean isOpen() {
        if (database != null) {
            if (database.isOpen()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void close() {
        Log.i(LOGTAG, "Databased Closed");
        dbhelper.close();
    }

    public List<Majors> GetFromMajors(String selection, String[] selectionArgs, String orderBy) {
        Cursor cursor = database.query(DatabaseManager.TABLE_1, majorsColumns, selection, selectionArgs, null, null, orderBy);
        Log.i(LOGTAG , "Returned " + cursor.getCount() + " rows");
        Log.i(LOGTAG , dumpCursorToString(cursor));
        List<Majors> majors = new ArrayList<Majors>();
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                Majors major = new Majors();
                major.setId(cursor.getLong(cursor.getColumnIndex(DatabaseManager.TABLE_1_COL_1)));
                major.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseManager.TABLE_1_COL_2)));
                major.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseManager.TABLE_1_COL_3)));
                majors.add(major);
            }
        }
        return majors;
    }

    public List<Courses> GetFromCourses(String selection, String[] selectionArgs, String orderBy) {
        Cursor cursor = database.query(DatabaseManager.TABLE_2, coursesColumns, selection, selectionArgs, null, null, orderBy);
        Log.i(LOGTAG , "Returned " + cursor.getCount() + " rows");
        Log.i(LOGTAG , dumpCursorToString(cursor));
        List<Courses> courses = new ArrayList<Courses>();
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                Courses course = new Courses();
                course.setId(cursor.getLong(cursor.getColumnIndex(DatabaseManager.TABLE_2_COL_1)));
                course.setMajor(cursor.getInt(cursor.getColumnIndex(DatabaseManager.TABLE_2_COL_2)));
                course.setRoom(cursor.getInt(cursor.getColumnIndex(DatabaseManager.TABLE_2_COL_3)));
                course.setCRN(cursor.getString(cursor.getColumnIndex(DatabaseManager.TABLE_2_COL_4)));
                course.setCourse(cursor.getString(cursor.getColumnIndex(DatabaseManager.TABLE_2_COL_5)));
                course.setSection(cursor.getString(cursor.getColumnIndex(DatabaseManager.TABLE_2_COL_6)));
                course.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseManager.TABLE_2_COL_7)));
                course.setCredits(cursor.getString(cursor.getColumnIndex(DatabaseManager.TABLE_2_COL_8)));
                course.setDays(cursor.getString(cursor.getColumnIndex(DatabaseManager.TABLE_2_COL_9)));
                course.setStart(cursor.getString(cursor.getColumnIndex(DatabaseManager.TABLE_2_COL_10)));
                course.setEnd(cursor.getString(cursor.getColumnIndex(DatabaseManager.TABLE_2_COL_11)));
                course.setInstructor(cursor.getString(cursor.getColumnIndex(DatabaseManager.TABLE_2_COL_12)));
                courses.add(course);
            }
        }
        return courses;
    }

    public List<Buildings> GetFromBuildings(String selection, String[] selectionArgs, String orderBy) throws IndexOutOfBoundsException {
        Cursor cursor = database.query(DatabaseManager.TABLE_3, buildingsColumns, selection, selectionArgs, null, null, orderBy);
        Log.i(LOGTAG , "Returned " + cursor.getCount() + " rows");
        Log.i(LOGTAG , dumpCursorToString(cursor));
        List<Buildings> buildings = new ArrayList<Buildings>();
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                Buildings bldg = new Buildings();
                bldg.setId(cursor.getLong(cursor.getColumnIndex(DatabaseManager.TABLE_3_COL_1)));
                bldg.setName(cursor.getString(cursor.getColumnIndex(DatabaseManager.TABLE_3_COL_2)));
                bldg.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseManager.TABLE_3_COL_3)));
                bldg.setAddress(cursor.getString(cursor.getColumnIndex(DatabaseManager.TABLE_3_COL_4)));
                buildings.add(bldg);
            }
        }
        return buildings;
    }

    public List<Rooms> GetFromRooms(String selection, String[] selectionArgs, String orderBy) {
        Cursor cursor = database.query(DatabaseManager.TABLE_4, roomsColumns, selection, selectionArgs, null, null, orderBy);
        Log.i(LOGTAG , "Returned " + cursor.getCount() + " rows");
        Log.i(LOGTAG , dumpCursorToString(cursor));
        List<Rooms> rooms = new ArrayList<Rooms>();
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                Rooms room = new Rooms();
                room.setId(cursor.getLong(cursor.getColumnIndex(DatabaseManager.TABLE_4_COL_1)));
                room.setBuilding(cursor.getInt(cursor.getColumnIndex(DatabaseManager.TABLE_4_COL_2)));
                room.setRoom(cursor.getString(cursor.getColumnIndex(DatabaseManager.TABLE_4_COL_3)));
                rooms.add(room);
            }
        }
        return rooms;
    }

    /**
     *
     * @param name
     * @param desc
     * @return
     */
    public boolean InsertIntoMajors(String name, String desc) {
        Majors majors = new Majors();
        majors.setAll(name, desc);

        ContentValues values = new ContentValues();
        values.put(DatabaseManager.TABLE_1_COL_2, majors.getTitle());
        values.put(DatabaseManager.TABLE_1_COL_3, majors.getDescription());
        long insertid = database.insert(DatabaseManager.TABLE_1, null, values);
        if (insertid == -1) {
            return false;
        } else {
            majors.setId(insertid);
            return true;
        }
    }

    /**
     *
     * @param major_str
     * @param bldg_str
     * @param room_str
     * @param crn
     * @param course
     * @param title
     * @param credits
     * @param days
     * @param start
     * @param end
     * @param instructor
     * @return
     */
    public boolean InsertIntoCourses(String major_str, String bldg_str, String room_str, String crn, String course, String title,
                                     String credits, String days, String start, String end, String instructor) {
        Courses courses = new Courses();

        String[] selectArgs = new String[] {major_str};
        long major_id;
        try {
            major_id = GetFromMajors("name==?", selectArgs, null).get(0).getId();
        } catch (final IndexOutOfBoundsException e) {
            appendLog("Trouble linking major_id: " + courses.getMajor());
            major_id = 999;
        }

        selectArgs = new String[] {bldg_str};
        Log.d(LOGTAG, "bldg_str: " + selectArgs[0]);

        long bldg_id;
        try {
            bldg_id = GetFromBuildings("description==?", selectArgs, null).get(0).getId();
        } catch (final IndexOutOfBoundsException e) {
            Log.d(LOGTAG, "Trouble linking building_id for room_id: " + courses.getRoom());
            bldg_id = 999;
        }
        String bldg_id_str = String.valueOf(bldg_id);

        selectArgs = new String[] {room_str, bldg_id_str};
        Log.d(LOGTAG, "room_str: " + selectArgs[0]);
        long room_id;
        try {
            room_id = GetFromRooms("room_number==? AND building_id==?", selectArgs, null).get(0).getId();
        } catch (final IndexOutOfBoundsException e) {
            Log.d(LOGTAG, "Trouble linking room_id: " + courses.getRoom());
            room_id = 999;
        }

        String[] tokensVal = course.split("\\s+");

        courses.setAll(major_id, room_id, crn, tokensVal[0], tokensVal[1], title, credits, days, start, end, instructor);

        ContentValues values = new ContentValues();
        values.put(DatabaseManager.TABLE_2_COL_2, courses.getMajor());
        values.put(DatabaseManager.TABLE_2_COL_3, courses.getRoom());
        values.put(DatabaseManager.TABLE_2_COL_4, courses.getCRN());
        values.put(DatabaseManager.TABLE_2_COL_5, courses.getCourse());
        values.put(DatabaseManager.TABLE_2_COL_6, courses.getSection());
        values.put(DatabaseManager.TABLE_2_COL_7, courses.getTitle());
        values.put(DatabaseManager.TABLE_2_COL_8, courses.getCredits());
        values.put(DatabaseManager.TABLE_2_COL_9, courses.getDays());
        values.put(DatabaseManager.TABLE_2_COL_10, courses.getStart());
        values.put(DatabaseManager.TABLE_2_COL_11, courses.getEnd());
        values.put(DatabaseManager.TABLE_2_COL_12, courses.getInstructor());
        long insertid = database.insert(DatabaseManager.TABLE_2, null, values);

        Log.i(LOGTAG, "Created Courses entry");
        if (insertid == -1) {
            return false;
        } else {
            courses.setId(insertid);
            return true;
        }
    }

    public boolean InsertIntoBuildings(Buildings buildings) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.TABLE_3_COL_2, buildings.getName());
        values.put(DatabaseManager.TABLE_3_COL_3, buildings.getDescription());
        values.put(DatabaseManager.TABLE_3_COL_4, buildings.getAddress());
        long insertid = database.insert(DatabaseManager.TABLE_3, null, values);
        if (insertid == -1) {
            return false;
        } else {
            buildings.setId(insertid);
            return true;
        }
    }

    public boolean InsertIntoRooms(Rooms rooms) {
        ContentValues values = new ContentValues();
        values.put(DatabaseManager.TABLE_4_COL_2, rooms.getBuilding());
        values.put(DatabaseManager.TABLE_4_COL_3, rooms.getRoom());
        long insertid;
        try {
            insertid = database.insert(DatabaseManager.TABLE_4, null, values);
        } catch (final IndexOutOfBoundsException e) {
            appendLog("Trouble inserting into rooms : " + rooms.print());
            insertid = 999;
        }
        if (insertid == -1) {
            return false;
        } else {
            rooms.setId(insertid);
            return true;
        }
    }

    public void appendLog(String text)
    {
        File logFile = new File("sdcard/log.file");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
