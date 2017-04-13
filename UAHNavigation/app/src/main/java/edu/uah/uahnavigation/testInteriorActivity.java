package edu.uah.uahnavigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class testInteriorActivity extends AppCompatActivity {

    Button find;
    EditText sourceEditText, destinationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_interior);

        find = (Button)findViewById(R.id.buttonFind);
        sourceEditText = (EditText)findViewById(R.id.editTextStart);
        destinationEditText = (EditText)findViewById(R.id.editTextDest);


        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), InteriorNavigationActivity.class);
                String s = sourceEditText.getText().toString();
                String d = destinationEditText.getText().toString();
                if(!s.isEmpty() && !d.isEmpty())
                {
                    i.putExtra("source", s);
                    i.putExtra("destination", d);
                }
                else {
                    i.putExtra("source", "E102");
                    i.putExtra("destination", "ENG246");
                }
                startActivity(i);
            }
        });

    }
}
