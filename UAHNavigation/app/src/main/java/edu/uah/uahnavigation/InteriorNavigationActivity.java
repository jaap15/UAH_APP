package edu.uah.uahnavigation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class InteriorNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior_navigation);
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