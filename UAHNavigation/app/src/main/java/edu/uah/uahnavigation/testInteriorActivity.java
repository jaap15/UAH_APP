package edu.uah.uahnavigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class testInteriorActivity extends AppCompatActivity {

    Button find;
    EditText sourceEditText, destinationEditText, buildingEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_interior);

        find = (Button)findViewById(R.id.buttonFind);
        sourceEditText = (EditText)findViewById(R.id.editTextStart);
        destinationEditText = (EditText)findViewById(R.id.editTextDest);
        buildingEditText = (EditText)findViewById(R.id.editTextBuilding);


        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), InteriorNavigationActivity.class);
                String s = sourceEditText.getText().toString();
                String d = destinationEditText.getText().toString();
                String b = buildingEditText.getText().toString();
                if(!s.isEmpty() && !d.isEmpty()&& !b.isEmpty())
                {
                    i.putExtra("source", s);
                    i.putExtra("destination", d);
                    i.putExtra("building", b);
                }
                else {
                    i.putExtra("source", "E110");
                    i.putExtra("destination", "MSB100");
                    i.putExtra("building", "MSB");
                }
                startActivity(i);
            }
        });

    }
}
