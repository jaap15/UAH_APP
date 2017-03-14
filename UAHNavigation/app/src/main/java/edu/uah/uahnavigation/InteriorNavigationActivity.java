package edu.uah.uahnavigation;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InteriorNavigationActivity extends AppCompatActivity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior_navigation);

        AssetManager mngr = getAssets();
        try {
            InputStream in = mngr.open("InteriorNavigationResources/ENG/test.txt");

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            Toast.makeText(this,out.toString(), Toast.LENGTH_LONG).show();
            System.out.println(out.toString());   //Prints the string content read from input stream
            reader.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }


        image = (ImageView) findViewById(R.id.imageView1);



        try
        {
            // get input stream
            InputStream ims = getAssets().open("InteriorNavigationResources/ENG/ENG-Floor1.PNG");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            image.setImageDrawable(d);
            ims .close();
        }
        catch(IOException ex)
        {
            return;
        }
    }

    public void didTapGreetButton(View view) {
        EditText greetEditText =
                (EditText) findViewById(R.id.greetEditText);

        String name = greetEditText.getText().toString();
        String greeting = String.format("Hello, %s!", name);

        TextView messageTextView =
                (TextView) findViewById(R.id.messageTextView);

        messageTextView.setText(greeting);

    }
}