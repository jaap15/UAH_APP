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
        }
        else if(v.getId() == R.id.buildingbtn){
            Intent i = new Intent(getBaseContext(), BuildingTabActivity.class);
            i.putExtra("test", "test");
            startActivity(i);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.Swap_Semesters: return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
