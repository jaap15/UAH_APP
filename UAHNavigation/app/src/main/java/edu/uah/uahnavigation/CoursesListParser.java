package edu.uah.uahnavigation;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import edu.uah.model.Courses;

/**
 * Created by Jairo on 3/15/2017.
 */

public class CoursesListParser {

    private static int START_LINE = 2;
    private Context context;
    private String dirName;
    private File directory;
    private ArrayList courseList;

    public CoursesListParser(Context context)
    {
        this.context = context;
    }

    public CoursesListParser(Context context, String dirName, ArrayList courseList) throws Exception {
        this.context = context;
        this.dirName = dirName;
        this.courseList = courseList;

        directory = new File(this.context.getFilesDir()+"/"+this.dirName);

        if(!directory.exists()) {
            throw new RuntimeException(directory.getAbsolutePath() + " Doesn't exist!!");
//            throw new Exception(directory.getAbsolutePath() + " Doesn't exist!!");
        }
    }


    public void parseCourseFile(String absoluteFilePath)
    {

        BufferedReader reader = null;
        String description = null;
        Log.d("myMessage", "Debug 1 ");
        try {
            System.out.println("Opening file");
            Log.d("myMessage", "Opening file");
            reader = new BufferedReader(new FileReader(absoluteFilePath));
        } catch (FileNotFoundException ex) {

            System.out.println("Unable to open file");
            Log.d("myMessage", "Unable to open file");
//            Logger.getLogger(JsoupLearningNetbeans.class.getName()).log(Level.SEVERE, null, ex);
        }


        Log.d("myMessage", "Debug 2");
        try {
            description = reader.readLine();
//            description.substring(description.lastIndexOf("/") + 1)
            Log.d("myMessage", "Description: " + description.substring(description.lastIndexOf("/") + 1));
            System.out.println("Skipping lines");
            Log.d("myMessage", "Skipping lines");
            for(int i = 0; i < START_LINE; i++)
            {
                reader.readLine();
            }
            String[] tokens = reader.readLine().split(" ");

            System.out.println("Printing Token sizes Token length: " + tokens.length);
            Log.d("myMessage", "Printing Token sizes Token length: " + tokens.length);
            for(int i = 0; i < tokens.length; i++)
            {
                System.out.println(tokens[i]);
                System.out.println(tokens[i].length());
            }

            System.out.println("Reading file");
            Log.d("myMessage", "Reading file");
            String line = reader.readLine();
            System.out.println(line);
            Log.d("myMessage", "Line: " + line);

            int min = 0;
            int max = tokens[0].length();

            System.out.println("Sec Type");
            String secType = line.substring(min, max);
            System.out.println(secType);
            Log.d("myMessage", "Sec Type: " + secType);

            min = min + tokens[0].length() + 1;
            max = min + tokens[1].length();

            System.out.println("CRN");
            String crn = line.substring(min, max);
            System.out.println(crn);
            Log.d("myMessage", "CRN: " + crn);

            min = max + 1;
            max = min + tokens[2].length();

            System.out.println("Course");
            String course = line.substring(min, max);
            System.out.println(course);
            Log.d("myMessage", "Course: " + course);

            min = max + 1;
            max = min + tokens[3].length();

            System.out.println("Title");
            String title = line.substring(min, max);
            System.out.println(title);
            Log.d("myMessage", "Title: " + title);


            min = max + 1;
            max = min + tokens[4].length();

            System.out.println("Credit");
            String credit = line.substring(min, max);
            System.out.println(credit);
            Log.d("myMessage", "Credit: " + credit);

            min = max + 1;
            max = min + tokens[5].length();

            System.out.println("Max Enrl");
            String maxEnrl = line.substring(min, max);
            System.out.println(maxEnrl);
            Log.d("myMessage", "Max Enrl: " + maxEnrl);

            min = max + 1;
            max = min + tokens[6].length();

            System.out.println("Enrl");
            String enrl = line.substring(min, max);
            System.out.println(enrl);
            Log.d("myMessage", "Enrl: " + enrl);

            min = max + 1;
            max = min + tokens[7].length();

            System.out.println("Avail");
            String avail = line.substring(min, max);
            System.out.println(avail);
            Log.d("myMessage", "Avail: " + avail);

            min = max + 1;
            max = min + tokens[8].length();

            System.out.println("Wait List");
            String waitList = line.substring(min, max);
            System.out.println(waitList);
            Log.d("myMessage", "Wait List: " + waitList);

            min = max + 1;
            max = min + tokens[9].length();

            System.out.println("Days");
            String days = line.substring(min, max);
            System.out.println(days);
            Log.d("myMessage", "Days: " + days);

            min = max + 1;
            max = min + tokens[10].length();

            System.out.println("Start");
            String startTime = line.substring(min, max);
            System.out.println(startTime);
            Log.d("myMessage", "Start: " + startTime);

            min = max + 1;
            max = min + tokens[11].length();

            System.out.println("End");
            String endTime = line.substring(min, max);
            System.out.println(endTime);
            Log.d("myMessage", "End: " + endTime);

            min = max + 1;
            max = min + tokens[12].length();

            System.out.println("Bldg");
            String bldg = line.substring(min, max);
            System.out.println(bldg);
            Log.d("myMessage", "Bldg: " + bldg);

            min = max + 1;
            max = min + tokens[13].length();

            System.out.println("Room");
            String room = line.substring(min, max);
            System.out.println(room);
            Log.d("myMessage", "Room: " + room);

            min = max + 1;
            max = min + tokens[3].length();

            System.out.println("Instructor");
            String instructor = line.substring(min, max);
            System.out.println(instructor);
            Log.d("myMessage", "Instructor: " + instructor);

        } catch (IOException ex) {
            System.out.println("Unable to read file");
            Log.d("myMessage", "Debug 4 Unable to read file");
//            Logger.getLogger(JsoupLearningNetbeans.class.getName()).log(Level.SEVERE, null, ex);
        }

    }



}
