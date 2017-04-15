package edu.uah.uahnavigation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CampusMapActivity extends AppCompatActivity {

    private Button closeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_map);

        Bitmap b = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.campus_map2);
        ImageView t = (ImageView)findViewById(R.id.imageViewCampusMap);
        t.setImageBitmap(b);

        closeButton = (Button)findViewById(R.id.buttonCloseCampusMap);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();

            }
        });

    }
}
