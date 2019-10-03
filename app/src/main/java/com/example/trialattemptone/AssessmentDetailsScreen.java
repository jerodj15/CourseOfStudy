package com.example.trialattemptone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.trialattemptone.Creators.Assessment;
import com.example.trialattemptone.Creators.AssessmentType;
import com.example.trialattemptone.Creators.Course;
import com.example.trialattemptone.database.DataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AssessmentDetailsScreen extends AppCompatActivity {

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    EditText mAssTitle;
    EditText mAssDueDate;
    Button mDueDatePicker;
    Button mAssessmentSave;
    Button mAssessmentOkay;
    DataSource mDataSource;
    Spinner typeSelectionSpinner;
    public static String selectedAssessmentTitle;
    int selectedAssessmentType;
    int selectedTypeIndex;
    public static String dueDate;
    public static int mSelectedAssId;
    int mSelectedCourseID;
    String assessmentTitle;
    CalendarView calView;
    List<AssessmentType> assessmentTypeList;
    List<String> assTypeTitles = new ArrayList<>();
    List<Integer> assTypesID = new ArrayList<>();
    public static int assTypeIDSelection;
    Course currentCourse;
    Date courseStart, courseEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details_screen);
        mDataSource = new DataSource(this);
        mDataSource.open();
        mSelectedAssId = CourseDetailsScreen.selectedAssId;
        mSelectedCourseID = CourseDetailsScreen.selCourseID;
        Assessment assessment = mDataSource.getAssessmentByAssessmentID(mSelectedAssId);
        selectedAssessmentType = assessment.getAssTypeID();
        selectedAssessmentTitle = assessment.getAssTitle();
        assessmentTypeList = mDataSource.getAllAssessmentTypes();
        for (AssessmentType assessmentType : assessmentTypeList)
        {
            assTypeTitles.add(assessmentType.getAssessmentType());
            assTypesID.add(assessmentType.getAssessmentTypeID());
        }
        // Setup the spinner
        setupSpinners();
        mAssTitle = findViewById(R.id.assessmentTiitleET);
        mAssTitle.setText(assessment.getAssTitle());
        mAssDueDate = findViewById(R.id.assessmentDueET);
        mAssDueDate.setText(assessment.getAssEnd());
        mDueDatePicker = findViewById(R.id.assessmentDueDateButton);
        mAssessmentSave = findViewById(R.id.assessmentSaveChangesButton);
        mAssessmentOkay = findViewById(R.id.okayButton);
        getSupportActionBar().setTitle("Details for " + assessment.getAssTitle());

    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.assessmentdetails_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.edit_assessment:
                mDueDatePicker.setVisibility(View.VISIBLE);
                mAssTitle.setEnabled(true);
                mDueDatePicker.setVisibility(View.VISIBLE);
                mAssessmentOkay.setText("Cancel");
                mAssessmentSave.setVisibility(View.VISIBLE);
                typeSelectionSpinner.setEnabled(true);
                return true;
            case R.id.set_assessmentAlert:
                Intent courseIntent = new Intent(this, AssessmentAlertScreen.class);
                startActivity(courseIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void okayButtonPressed(View view)
    {
        this.finish();
        Intent intent = new Intent(this, CourseDetailsScreen.class);
        startActivity(intent);
    }
    public void saveAssButtonPressed(View view)
    {
        EditText newTitle = findViewById(R.id.assessmentTiitleET);
        EditText newDueDate = findViewById(R.id.assessmentDueET);
        String newTitleString = newTitle.getText().toString();
        String newDueDateString = newDueDate.getText().toString();
        Assessment assessment = new Assessment(newTitleString, newDueDateString, mSelectedCourseID, assTypeIDSelection);
        mDataSource.updateAssessment(assessment, mSelectedAssId);
        Toast.makeText(this, assessment.getAssTitle(), Toast.LENGTH_SHORT).show();
        this.finish();
        Intent intent = new Intent(this, CourseDetailsScreen.class);
        startActivity(intent);
    }

    public void dueDateButtonPressed(View view)
    {
            currentCourse = mDataSource.getCourseByID(mSelectedCourseID);
        try
        {
            courseStart = sdf.parse(currentCourse.getCourseStart());
            courseEnd = sdf.parse(currentCourse.getCourseEnd());
        }catch (ParseException ex)
        {
            System.out.println(ex.getLocalizedMessage());
        }
            EditText titleEditT = findViewById(R.id.assessmentTiitleET);
            if (titleEditT.getText().toString().isEmpty() == false)
            {
                assessmentTitle = titleEditT.getText().toString();
            }
            setContentView(R.layout.activity_date_picker);
            getSupportActionBar().setTitle("Pick a Due Date");
            mDueDatePicker = findViewById(R.id.selectButton);
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
        setContentView(R.layout.activity_assessment_details_screen);
        EditText tTitle = findViewById(R.id.assessmentTiitleET);
        EditText dueEdit = findViewById(R.id.assessmentDueET);
        Button cButton = findViewById(R.id.okayButton);
        Button sButton = findViewById(R.id.assessmentSaveChangesButton);
        Button datePick = findViewById(R.id.assessmentDueDateButton);
        cButton.setText("Cancel");
        sButton.setVisibility(View.VISIBLE);
        dueEdit.setText(dueDate);
        tTitle.setText(assessmentTitle);
        datePick.setVisibility(View.VISIBLE);
        tTitle.setEnabled(true);
        setupSpinners();
        Spinner typeSpin = findViewById(R.id.detailTypeSpinner);
        typeSpin.setEnabled(true);

    }
    public void cancelDatePick(View view)
    {
        setContentView(R.layout.activity_assessment_details_screen);
    }

    public void setupSpinners()
    {
        ArrayAdapter typesAdapter = new ArrayAdapter(
                this, android.R.layout.simple_spinner_item, assTypeTitles
        );
        typeSelectionSpinner = findViewById(R.id.detailTypeSpinner);
        typeSelectionSpinner.setEnabled(false);
        typeSelectionSpinner.setAdapter(typesAdapter);
        for (int i = 0; i < assTypeTitles.size(); i++)
        {
            if (assTypesID.get(i) == selectedAssessmentType)
            {
                selectedTypeIndex = i;
            }
        }
        typeSelectionSpinner.setSelection(selectedTypeIndex);
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
