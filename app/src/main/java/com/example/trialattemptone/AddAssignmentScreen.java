package com.example.trialattemptone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.trialattemptone.Creators.Assessment;
import com.example.trialattemptone.Creators.AssessmentType;
import com.example.trialattemptone.Creators.Course;
import com.example.trialattemptone.database.DataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddAssignmentScreen extends AppCompatActivity {

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    Button mCancel;
    Button mSave;
    Button mDueDate;
    EditText mTitle;
    EditText mDate;
    DataSource mDataSource;
    Button selectDateButton;
    CalendarView calView;
    public static String assessmentTitle;
    public static String dueDate;
    public static int assTypeIDSelection;
    public static int selCourseID;
    public static Course currentCourse;
    Date courseStart, courseEnd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment_screen);
        mDataSource = new DataSource(this);
        mDataSource.open();
        // Get all the elements from the screen layout
        mCancel = findViewById(R.id.assCancelButton);
        mSave = findViewById(R.id.assSaveButton);
        mDueDate = findViewById(R.id.dueDateButton);
        mTitle = findViewById(R.id.assTitleET);
        mDate = findViewById(R.id.assDueDateET);
        selCourseID = CourseDetailsScreen.selCourseID;
        setupSpinners();
        getSupportActionBar().setTitle("Add New Assessment");



    }
    // Method for canceling
    public void cancelButtonPressed(View view)
    {
        mDataSource.close();
        finish();
        Intent intent = new Intent(this, CourseDetailsScreen.class);
        startActivity(intent);
    }
    // Method for saving
    public void saveButtonPressed(View view)
    {
        EditText titleEdit = findViewById(R.id.assTitleET);
        EditText dueEdit = findViewById(R.id.assDueDateET);
        String aTitle = titleEdit.getText().toString();
        String aDueDate = dueEdit.getText().toString();
        Assessment assessment = new Assessment(aTitle, aDueDate, selCourseID, assTypeIDSelection);
        System.out.println(aDueDate);
        mDataSource.createAssessment(assessment);
        finish();
        Intent intent = new Intent(this, CourseDetailsScreen.class);
        startActivity(intent);
    }

    // Method for selecting the dueDate
    public void assDueDateButtonPressed(View view)
    {
        currentCourse = mDataSource.getCourseByID(selCourseID);
        try
        {
            courseStart = sdf.parse(currentCourse.getCourseStart());
            courseEnd = sdf.parse(currentCourse.getCourseEnd());
        }catch (ParseException ex)
        {
            System.out.println(ex.getLocalizedMessage());
        }
        EditText titleEditT = findViewById(R.id.assTitleET);
        if (titleEditT.getText().toString().isEmpty() == false)
        {
            assessmentTitle = titleEditT.getText().toString();
        }
        setContentView(R.layout.activity_date_picker);
        getSupportActionBar().setTitle("Pick a Due Date");
        selectDateButton = findViewById(R.id.selectButton);
        calView = findViewById(R.id.calendarView);
        calView.setMinDate(courseStart.getTime());
        calView.setMaxDate(courseEnd.getTime());
        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String selMonth = String.valueOf(month + 1);
                String selYear = String.valueOf(year);
                String selDay = String.valueOf(dayOfMonth);
                String newMonth = selMonth;
                String newDay = selDay;
                String newYear = selYear;
                if (month < 10)
                {
                    newMonth = "0" + selMonth;

                }
                if (dayOfMonth < 10)
                {
                    newDay = "0" + selDay;
                }

                dueDate = newMonth + "/" + newDay + "/" + newYear;
                System.out.println(dueDate);
            }
        });
    }
    public void selectButtonPressed(View view)
    {
            setContentView(R.layout.activity_add_assignment_screen);
            EditText dueEdit = findViewById(R.id.assDueDateET);
            dueEdit.setText(dueDate);
            EditText tTitle = findViewById(R.id.assTitleET);
            tTitle.setText(assessmentTitle);
            setupSpinners();
    }
    public void cancelDatePick(View view)
    {
        setContentView(R.layout.activity_add_assignment_screen);
    }
    public void setupSpinners()
    {
        // Setup the assessmentType spinner
        List<String> assTypeTitles = new ArrayList<>();
        List<Integer> assTypesID = new ArrayList<>();
        List<AssessmentType> assessmentTypeList = mDataSource.getAllAssessmentTypes();
        for (AssessmentType assessmentType : assessmentTypeList)
        {
            assTypeTitles.add(assessmentType.getAssessmentType());
            assTypesID.add(assessmentType.getAssessmentTypeID());
        }

        // Setup the spinner
        Spinner typeSelectionSpinner = findViewById(R.id.typeSpinner);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, assTypeTitles
        );
        System.out.println(assTypeTitles.toString());
        typeSelectionSpinner.setAdapter(statusAdapter);
        typeSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                assTypeIDSelection = mDataSource.getAssessmentTypeID((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
