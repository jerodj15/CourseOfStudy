package com.example.trialattemptone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trialattemptone.Creators.Alert;
import com.example.trialattemptone.Creators.Assessment;
import com.example.trialattemptone.Creators.Course;
import com.example.trialattemptone.database.DataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AssessmentAlertScreen extends AppCompatActivity {

    DataSource mDataSource;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    public static Course selectCourse;
    public static int mSelectedCourseID;
    public static int mSelectedAssID;
    public static int alertID;
    public static int alHour;
    public static String dueDate;
    public static String mAlertTitle;
    public static String mAlertDate;
    public static String mAlertHour;
    public static boolean isEnabled;
    public static boolean isExist;
    public static Alert assessmentAlert;
    EditText alertTitle;
    EditText alertDate;
    EditText alertHour;
    Button mDueDatePicker;
    CheckBox alertEnabledBox;
    CalendarView calView;
    Date alDate;
    Date courseStart, courseEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_alert_screen);
        mDataSource = new DataSource(this);
        mDataSource.open();
        selectCourse = CourseDetailsScreen.selectedCourse;
        mSelectedAssID = AssessmentDetailsScreen.mSelectedAssId;
        mSelectedCourseID = selectCourse.getCourseId();
        alertTitle = findViewById(R.id.alertAssTitleET);
        alertDate = findViewById(R.id.alertAssDateET);
        alertHour = findViewById(R.id.alertAssHourET);
        mDueDatePicker = findViewById(R.id.alertAssDateButton);
        alertEnabledBox = findViewById(R.id.alertAssEnabledCheckBox);
        assessmentAlert = mDataSource.getAssessmentAlertByAssID(mSelectedAssID);
        Assessment assessment = mDataSource.getAssessmentByAssessmentID(CourseDetailsScreen.selectedAssId);
        getSupportActionBar().setTitle("Alert for " + assessment.getAssTitle());
        populateAlert();
    }

    // setup the save button
    public void saveAlertButtonPressed(View view)
    {
        CheckBox alertEnabledBox = findViewById(R.id.alertAssEnabledCheckBox);
        EditText aTitle = findViewById(R.id.alertAssTitleET);
        EditText aDate = findViewById(R.id.alertAssDateET);
        EditText aHour = findViewById(R.id.alertAssHourET);
        String titleString = aTitle.getText().toString();
        String dateString = aDate.getText().toString();
        int hourInt = Integer.parseInt(aHour.getText().toString());
        try
        {
            alDate = sdf.parse(dateString);
        }catch (ParseException ex)
        {
            System.out.println(ex.getLocalizedMessage());
        }


        if (alertEnabledBox.isChecked())
        {
            isEnabled = true;
        }
        else
        {
            isEnabled = false;
        }

        if (isExist)
        {
            if (isEnabled)
            {
                // 0 = active

                int alertID = assessmentAlert.getTypeId();
                Alert alert = new Alert(titleString, dateString, hourInt, 0, mSelectedCourseID, mSelectedAssID);
                mDataSource.updateAssessmentAlert(alert, alertID);
                BootReceiver bootReceiver = new BootReceiver();
                bootReceiver.scheduleNotification(this, alert, selectCourse.getCourseTitle());
            }
            if (!isEnabled)
            {
                int alertID = assessmentAlert.getTypeId();
                Alert alert = new Alert(titleString, dateString, hourInt, 1, mSelectedCourseID, mSelectedAssID);
                mDataSource.updateCourseAlert(alert, alertID);
                BootReceiver bootReceiver = new BootReceiver();
                bootReceiver.scheduleNotification(this, alert, selectCourse.getCourseTitle());
            }
        }
        if (!isExist)
        {
            if (isEnabled)
            {
                // 0 = active
                Alert alert = new Alert(titleString, dateString, hourInt, 0, mSelectedCourseID, mSelectedAssID);
                mDataSource.addCourseAlert(alert);
                BootReceiver bootReceiver = new BootReceiver();
                bootReceiver.scheduleNotification(this, alert, selectCourse.getCourseTitle());
            }
            if (!isEnabled)
            {
                // 1 = inactive
                Alert alert = new Alert(titleString, dateString, hourInt, 1, mSelectedCourseID, mSelectedAssID);
                mDataSource.addCourseAlert(alert);
                BootReceiver bootReceiver = new BootReceiver();
                bootReceiver.scheduleNotification(this, alert, selectCourse.getCourseTitle());
            }
        }
        mDataSource.close();
        this.finish();
        Intent intent = new Intent(this, CourseDetailsScreen.class);
        startActivity(intent);


    }


    // setup the cancel button
    public void cancelAlertButtonPressed(View view)
    {
        this.finish();
        Intent intent = new Intent(this, AssessmentDetailsScreen.class);
        startActivity(intent);
    }

    public void checkBoxClicked(View view)
    {
        isEnabled = alertEnabledBox.isChecked();
        if (isEnabled)
        {
            isEnabled = true;
        }
        else
        {
            isEnabled = false;
        }
    }

    public void populateAlert()
    {
        if (assessmentAlert.getTitle() == null)
        {
            isExist = false;
        }
        else
        {
            isExist = true;
            EditText aTitle = findViewById(R.id.alertAssTitleET);
            EditText aDate = findViewById(R.id.alertAssDateET);
            EditText aHour = findViewById(R.id.alertAssHourET);
            CheckBox aEnabledCB = findViewById(R.id.alertAssEnabledCheckBox);
            alertID = assessmentAlert.getAlertID();
            String titleString = assessmentAlert.getTitle();
            String dateString = assessmentAlert.getDate();
            int hourInt = assessmentAlert.getHour();
            int alertAct = assessmentAlert.getActive();
            if (alertAct == 0)
            {
                aEnabledCB.setChecked(true);
            }
            else
            {
                aEnabledCB.setChecked(false);
            }
            aTitle.setText(titleString);
            aDate.setText(dateString);
            aHour.setText(String.valueOf(hourInt));
        }
    }


    // Setup the datepick
    public void dueDateButtonPressed(View view)
    {

        try
        {
            courseStart = sdf.parse(selectCourse.getCourseStart());
            courseEnd = sdf.parse(selectCourse.getCourseEnd());
        }catch (ParseException ex)
        {
            System.out.println(ex.getLocalizedMessage());
        }
        EditText titleEditT = findViewById(R.id.alertAssTitleET);
        EditText hourEditT = findViewById(R.id.alertAssDateET);
        if (titleEditT.getText().toString().isEmpty() == false)
        {
            mAlertTitle = titleEditT.getText().toString();
            titleEditT.setText(mAlertTitle);
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
        setContentView(R.layout.activity_assessment_alert_screen);
        EditText tTitle = findViewById(R.id.alertAssTitleET);
        EditText dueEdit = findViewById(R.id.alertAssDateET);
        EditText dueHour = findViewById(R.id.alertAssHourET);
        Button cButton = findViewById(R.id.alertAssCancelButton);
        Button sButton = findViewById(R.id.alertAssSaveButton);
        Button datePick = findViewById(R.id.alertAssDateButton);
        cButton.setText("Cancel");
        sButton.setVisibility(View.VISIBLE);
        dueEdit.setText(dueDate);
        dueHour.setText(dueHour.getText().toString());
        tTitle.setText(mAlertTitle);
        datePick.setVisibility(View.VISIBLE);
        tTitle.setEnabled(true);

    }
    public void cancelDatePick(View view)
    {
        setContentView(R.layout.activity_assessment_alert_screen);
    }
}
