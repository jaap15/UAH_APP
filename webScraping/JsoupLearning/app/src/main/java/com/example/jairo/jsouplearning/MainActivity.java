package com.example.jairo.jsouplearning;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MainActivity extends AppCompatActivity {
    TextView texx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texx = (TextView)findViewById(R.id.tex1);

        Button but = (Button)findViewById(R.id.bnt1);

        but.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){

                new doit().execute();
            }
        });
    }

    public class doit extends AsyncTask<Void,Void,Void>{
        String words;

        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
//                Document doc = Jsoup.connect("http://www.uah.edu/cgi-bin/schedule.pl?file=fall2016.html&segment=NDX").get();
                Document doc = Jsoup.connect("http://www.uah.edu/").get();

                words = doc.text();
            }catch(Exception e){e.printStackTrace();}

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            texx.setText(words);
        }
    }
}
