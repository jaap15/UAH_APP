package edu.uah.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

        Courses time = getItem(position);
        TextView timeText = (TextView) customView.findViewById(R.id.tvTime);
        TextView profText = (TextView) customView.findViewById(R.id.tvProf);
        TextView crnText = (TextView) customView.findViewById(R.id.tvCRN);

        timeText.setText(time.getStart() + " - " + time.getEnd());
        profText.setText(time.getInstructor());
        crnText.setText(time.getCRN());
        return customView;
    }
}
