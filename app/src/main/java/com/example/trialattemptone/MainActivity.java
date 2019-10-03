package com.example.trialattemptone;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trialattemptone.Creators.Assessment;
import com.example.trialattemptone.Creators.Course;
import com.example.trialattemptone.Creators.CourseNote;
import com.example.trialattemptone.Creators.Term;
import com.example.trialattemptone.database.DBHelper;
import com.example.trialattemptone.database.DataSource;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    DataSource mDataSource;
    List<String> courseNames = new ArrayList<>();
    List<Course> currentCourses = new ArrayList<>();
    List<Integer> courseIDList = new ArrayList<>();
    ListView listView;
    public static int selectedCourseID;
    private static final String[] courseStats = {"In Progress", "Completed", "Dropped", "Plan To Take"};
    public static final List<String> courseStatusList = Arrays.asList(courseStats);
    private static final String[] assessType = {"Objective", "Performance"};
    public static final List<String> assessmentTypesList = Arrays.asList(assessType);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataSource = new DataSource(this);
        mDataSource.open();
        mDataSource.seedDataBase(courseStatusList, assessmentTypesList);
        Date currentTime = Calendar.getInstance().getTime();
        String currentDate = sdf.format(currentTime);
        int curTerm = mDataSource.getCurrentTermID(currentDate);
        try {
            List<Course> coursseListFromDB = mDataSource.getTermsCourses(curTerm);
            for (Course course: coursseListFromDB)
            {
                courseNames.add(course.getCourseTitle() + "\nStart: " + course.getCourseStart() + "\nEnd: " + course.getCourseEnd() + "\nStatus: " + mDataSource.getCourseStatusByStatusID(course.getCourseStatusId()));
                currentCourses.add(course);
                courseIDList.add(course.getCourseId());

            }
            Collections.sort(courseNames);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1, courseNames
            );

            this.listView = findViewById(R.id.courseListView);
            this.listView.setAdapter(adapter);
            this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(view.getContext(), "Please use the menu to get or add more information about your course of study", Toast.LENGTH_LONG).show();
                }
            });

        }
        catch (Exception e)
        {
            System.out.println(e.getLocalizedMessage());
        }

        getSupportActionBar().setTitle("Course of Study Planner");

        BootReceiver bootReceiver = new BootReceiver();
        bootReceiver.testingNotification(this, 1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.term_item:
                Intent termIntent = new Intent(this, TermsScreen.class);
                startActivity(termIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        mDataSource.close();
    }
    @Override
    protected void onResume() {

        super.onResume();
        mDataSource.open();
    }
}
