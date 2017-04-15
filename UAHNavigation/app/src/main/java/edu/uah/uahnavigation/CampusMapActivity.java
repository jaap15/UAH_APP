package edu.uah.uahnavigation;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class CampusMapActivity extends AppCompatActivity {

    private Button closeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_map);

        ImageView t = (ImageView)findViewById(R.id.imageViewCampusMap);

        AssetManager assetManager = getAssets();
        InputStream inStream = null;
        try{
            Log.d("iMessage", "Trying to load campus map image");
            inStream = assetManager.open("campus_map.png");
            Log.d("iMessage", "Loaded campus map image");
        }catch (IOException e){
            Log.d("iMessage", "Unable to Load campus map image");
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inStream);
        t.setImageBitmap(bitmap);

        closeButton = (Button)findViewById(R.id.buttonCloseCampusMap);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();

            }
        });

    }
}
