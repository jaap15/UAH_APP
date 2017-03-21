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
    private String webscraperFolder = null;
    private ArrayList courseList;
    private DatabaseSource db;

    public CoursesListParser(Context context, String dirName)
    {
        this.context = context;
        this.dirName = dirName;
        this.webscraperFolder = Util.getProperty("WEBSCRAPER_FOLDER", this.context);
        this.db = new DatabaseSource(this.context);
        this.db.open();

        this.directory = new File(this.context.getFilesDir()+ "/" + this.webscraperFolder + "/"+this.dirName);

        if(!directory.exists()) {
            throw new RuntimeException(directory.getAbsolutePath() + " Doesn't exist!!");
        }
        this.courseList = new ArrayList();

        File files[] = this.directory.listFiles();
        for(File file: files)
        {
            this.courseList.add(file.getName());
            Log.d("myMessage", "Files: " + file.getName());
        }
    }

    public CoursesListParser(Context context, String dirName, ArrayList courseList) throws Exception {
        this.context = context;
        this.dirName = dirName;
        this.webscraperFolder = Util.getProperty("WEBSCRAPER_FOLDER", this.context);
        this.courseList = courseList;

        this.directory = new File(this.context.getFilesDir()+ "/" + this.webscraperFolder + "/"+this.dirName);

        if(!directory.exists()) {
            throw new RuntimeException(directory.getAbsolutePath() + " Doesn't exist!!");
//            throw new Exception(directory.getAbsolutePath() + " Doesn't exist!!");
        }
    }


    public void parseSemester()
    {

        int i = 0;

        while(i < courseList.size())
        {
            parseCourseFile(this.directory.getAbsolutePath() + "/" + courseList.get(i));
            i++;
        }

    }

    public void parseCourseFile(String absoluteFilePath)
    {

//        this.db.open();

        BufferedReader reader = null;
        String description = null;
        String major = null;
        Log.d("myMessage", "Debug 1 ");
        Log.d("mmyMessage", "File: " + absoluteFilePath);
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
            String tmp = reader.readLine();
            Log.d("myMessage", "Major/Description: " + tmp);
            major = tmp.substring(0, 3);
            Log.d("myMessage", "Major: " + major);
            description = tmp.substring(tmp.lastIndexOf("/") + 1);
            Log.d("myMessage", "Description: " + description);

            this.db.InsertIntoMajors(major, description);
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
            String line;
            Log.d("notInserted", "InFile: " + absoluteFilePath);
            while ((line = reader.readLine()) != null) {

//                boolean validClass = true;
                System.out.println(line);
                Log.d("myMessage", "Line: " + line);


                if(line.trim().isEmpty() || line.contains("Sec Typ:"))
                {
                    Log.d("myMessage", "No more courses in file: " + line);
                    break;
                }

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


                boolean invalidClass = classIsTBA(major);
                boolean invalidClass1 = classIsTBA(bldg);
                boolean invalidClass2 = classIsTBA(room);
                boolean invalidClass3 = classIsTBA(crn);
                boolean invalidClass4 = classIsTBA(course);
                boolean invalidClass5 = classIsTBA(title);
                boolean invalidClass6 = classIsTBA(credit);
                boolean invalidClass7 = classIsTBA(days);
                boolean invalidClass8 = classIsTBA(startTime);
                boolean invalidClass9 = classIsTBA(endTime);


                if(!invalidClass && !invalidClass1&& !invalidClass2 && !invalidClass3 && !invalidClass4 && !invalidClass5 && !invalidClass6 && !invalidClass7 && !invalidClass8 && !invalidClass9) {
                    Log.d("myMessage", "Trying to insert to db: " + major +" , "+bldg+" , "+room+" , "+crn+" , "+course+" , "+title+" , "+credit+" , "+days+" , "+startTime+" , "+endTime+" , "+instructor);
                    this.db.InsertIntoCourses(major, bldg.trim(), room.trim(), crn, course, title, credit, days, startTime, endTime, instructor);
                    Log.d("myMessage", "Inserted to db: " + major +" , "+bldg+" , "+room+" , "+crn+" , "+course+" , "+title+" , "+credit+" , "+days+" , "+startTime+" , "+endTime+" , "+instructor);
                }
                else
                {
                    Log.d("notInserted", "Not inserted to db: " + major +" , "+bldg+" , "+room+" , "+crn+" , "+course+" , "+title+" , "+credit+" , "+days+" , "+startTime+" , "+endTime+" , "+instructor);
                }

            }
            Log.d("myMessage", "Closing file");
            reader.close();
//            Log.d("myMessage", "Closing db");
//            this.db.close();
        } catch (IOException ex) {
            System.out.println("Unable to read file");
            Log.d("myMessage", "Debug 4 Unable to read file");
        }

    }

    private boolean classIsTBA(String parsed)
    {
        boolean containsTBA = false;

        if(parsed.contains("TBA") || parsed.contains("Canceled") || parsed.contains("ONLINE"))
        {
            containsTBA = true;
        }

        return containsTBA;
    }

}
