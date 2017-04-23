package edu.uah.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

import edu.uah.uahnavigation.R;

/**
 * Created by Daniel on 2/14/2017.
 */

public class BuildingsSpinAdapter extends ArrayAdapter<Buildings>{

    private Context context;
    // Custom values for the spinner (Courses)
    private Buildings[] values;

    public BuildingsSpinAdapter(Context context, int textViewResourceId, Buildings[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public void setCourses(Buildings[] values) {
        this.values = values;
    }

    public int getCount(){
        return values.length;
    }

    public Buildings getItem(int position){
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
//        View border = new View(context);
//        border.setBackgroundColor(Color.GRAY);
//        Drawable drawable = context.getResources().getDrawable(R.drawable.border);
//        label.setBackground(drawable);
        label.setTextColor(Color.BLACK);
        label.setTextSize(20);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (Majors class)
        label.setText(values[position].getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(20);
        label.setText(values[position].getName());

        return label;
    }

    static String[] removeDuplicates(String[] arrayWithDuplicates)
    {
        //Assuming all elements in input array are unique
        int noOfUniqueElements = arrayWithDuplicates.length;
        //Comparing each element with all other elements
        for (int i = 0; i < noOfUniqueElements; i++) {
            for (int j = i+1; j < noOfUniqueElements; j++) {
                //If any two elements are found equal
                if(arrayWithDuplicates[i] == arrayWithDuplicates[j]) {
                    //Replace duplicate element with last unique element
                    arrayWithDuplicates[j] = arrayWithDuplicates[noOfUniqueElements-1];
                    //Decrementing noOfUniqueElements
                    noOfUniqueElements--;
                    //Decrementing j
                    j--;
                }
            }
        }

        //Copying only unique elements of arrayWithDuplicates into arrayWithoutDuplicates
        String[] arrayWithoutDuplicates = Arrays.copyOf(arrayWithDuplicates, noOfUniqueElements);
        return  arrayWithoutDuplicates;
    }
}