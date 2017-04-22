package edu.uah.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.uah.uahnavigation.DatabaseSource;
import edu.uah.uahnavigation.DialogException;
import edu.uah.uahnavigation.R;

/**
 * Created by Daniel on 3/24/2017.
 */

public class CoursesListAdapter extends ArrayAdapter<Courses> {

    private Context context;
    // Custom values for the spinner (Courses)
    private Courses[] values;

    public CoursesListAdapter(Context context, Courses[] courses) {
        super(context, R.layout.courses_row, courses);
        this.context = context;
        this.values = courses;
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


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.courses_row, parent, false);

        DatabaseSource dbSource = new DatabaseSource(context);
        dbSource.open();

        Courses course = getItem(position);
        TextView topText = (TextView) customView.findViewById(R.id.tvTop);
        TextView middleText = (TextView) customView.findViewById(R.id.tvMiddle);
        TextView bottomText = (TextView) customView.findViewById(R.id.tvBottom);

        List<Rooms> room = null;
        List<Buildings> bldg = null;

        try {
            room = dbSource.GetFromRooms("id=?", new String[]{String.valueOf(course.getRoom())}, null);
        } catch (IndexOutOfBoundsException e) {

        }

        try {
            bldg = dbSource.GetFromBuildings("id=?", new String[]{String.valueOf(room.get(0).getBuilding())}, null);
        } catch (IndexOutOfBoundsException e) {

        }

        if (room != null && bldg != null) {
            topText.setText(bldg.get(0).getDescription() + ": " + room.get(0).getRoom());
            middleText.setText(course.getDays() + ": " + course.getStart() + " - " + course.getEnd());
            bottomText.setText(course.getInstructor());
        } else {
            topText.setText("No room entry found!");
            middleText.setText(course.getDays() + ": " + course.getStart() + " - " + course.getEnd());
            bottomText.setText(course.getInstructor());
        }
        return customView;
    }
}
