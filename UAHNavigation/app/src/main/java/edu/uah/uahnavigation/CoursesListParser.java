package edu.uah.uahnavigation;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import edu.uah.model.Courses;

/**
 * Created by Jairo on 3/15/2017.
 */

public class CoursesListParser {

    private Context context;
    private String dirName;
    private File directory;

    public CoursesListParser()
    {

    }

    public CoursesListParser(Context context, String dirName) throws Exception {
        this.context = context;
        this.dirName = dirName;

        directory = new File(this.context.getFilesDir()+"/"+this.dirName);

        if(!directory.exists()) {
            throw new RuntimeException(directory.getAbsolutePath() + " Doesn't exist!!");
//            throw new Exception(directory.getAbsolutePath() + " Doesn't exist!!");
        }
    }


}
