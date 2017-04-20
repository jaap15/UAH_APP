package edu.uah.uahnavigation;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import edu.uah.model.Courses;
import edu.uah.model.CoursesSpinAdapter;
import edu.uah.model.Majors;
import edu.uah.model.MajorsSpinAdapter;
public class MainActivity  extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("myMessage", "Debug 1 " + BuildConfig.CONFIG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button classBtn = (Button) findViewById(R.id.classbtn);
        final Button BlgdBtn = (Button) findViewById(R.id.buildingbtn);
        classBtn.setOnClickListener(this);
        BlgdBtn.setOnClickListener(this);

        int sessionID = 98765;
        //i.putExtra("EXTRA_SESSION_ID", sessionID);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.classbtn){
            Intent i = new Intent(getBaseContext(), ClassTabActivity.class);
            i.putExtra("test", "test");
            startActivity(i);
            finish();
        }
        else if(v.getId() == R.id.buildingbtn){
            Intent i = new Intent(getBaseContext(), BuildingTabActivity.class);
            i.putExtra("test", "test");
            startActivity(i);
            finish();
        }
    }

}
