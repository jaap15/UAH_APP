package edu.uah.model;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Daniel on 2/14/2017.
 */

public class CoursesSpinAdapter extends ArrayAdapter<Courses>{

    private Context context;
    // Custom values for the spinner (Courses)
    private Courses[] values;

    public CoursesSpinAdapter(Context context, int textViewResourceId, Courses[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public void setCourses(Courses[] values) {
        this.values = values;
    }

    public int getCount(){
        return values.length;
    }

    public Courses getItem(int position){
        return values[position];
    }

    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (Majors class)
        label.setText(values[position].getCourse());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);

        label.setText(values[position].getCourse());

        return label;
    }
}