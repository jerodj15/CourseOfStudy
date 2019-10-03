package com.example.trialattemptone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.trialattemptone.Creators.Alert;
import com.example.trialattemptone.Creators.Course;
import com.example.trialattemptone.database.DataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CourseAlertScreen extends AppCompatActivity {

    DataSource mDataSource;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    public static Course selectCourse;
    public static int mSelectedCourseID;
    public static int alertID;
    public static int alHour;
    public static String dueDate;
    public static String mAlertTitle;
    public static String mAlertDate;
    public static String mAlertHour;
    public static boolean isEnabled;
    public static boolean isExist;
    public static Alert courseAlert;
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
        setContentView(R.layout.activity_course_alert_screen);
        mDataSource = new DataSource(this);
        mDataSource.open();
        selectCourse = CourseDetailsScreen.selectedCourse;
        mSelectedCourseID = selectCourse.getCourseId();
        courseAlert = mDataSource.getCourseAlertByCourseID(mSelectedCourseID);
        getSupportActionBar().setTitle("Set Alert for " + selectCourse.getCourseTitle());
        alertTitle = findViewById(R.id.alertCourseTitleET);
        alertDate = findViewById(R.id.alertCourseStartET);
        alertHour = findViewById(R.id.alertStartHourET);
        alertEnabledBox = findViewById(R.id.enabledCheckBox);
        populateAlert();
    }


    // setup the cancel button
    public void cancelAlertButtonPressed(View view)
    {
        this.finish();
        Intent intent = new Intent(this, CourseDetailsScreen.class);
        startActivity(intent);
    }

    // setup the save button
    public void saveAlertButtonPressed(View view)
    {
        CheckBox alertEnabledBox = findViewById(R.id.enabledCheckBox);
        EditText aTitle = findViewById(R.id.alertCourseTitleET);
        EditText aDate = findViewById(R.id.alertCourseStartET);
        EditText aHour = findViewById(R.id.alertStartHourET);
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

                int alertID = courseAlert.getCourseID();
                Alert alert = new Alert(titleString, dateString, hourInt, 0, mSelectedCourseID, 0);
                mDataSource.updateCourseAlert(alert, alertID);
                BootReceiver bootReceiver = new BootReceiver();
                bootReceiver.scheduleNotification(this, alert, selectCourse.getCourseTitle());
            }
            if (!isEnabled)
            {
                int alertID = courseAlert.getCourseID();
                Alert alert = new Alert(titleString, dateString, hourInt, 1, mSelectedCourseID, 0);
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
                Alert alert = new Alert(titleString, dateString, hourInt, 0, mSelectedCourseID, 0);
                mDataSource.addCourseAlert(alert);
                BootReceiver bootReceiver = new BootReceiver();
                bootReceiver.scheduleNotification(this, alert, selectCourse.getCourseTitle());
            }
            if (!isEnabled)
            {
                // 1 = inactive
                Alert alert = new Alert(titleString, dateString, hourInt, 1, mSelectedCourseID, 0);
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
        if (courseAlert.getTitle() == null)
        {
            Toast.makeText(this,"Alert doesn't exist", Toast.LENGTH_SHORT).show();
            isExist = false;
        }
        else
        {
            isExist = true;
            EditText aTitle = findViewById(R.id.alertCourseTitleET);
            EditText aDate = findViewById(R.id.alertCourseStartET);
            EditText aHour = findViewById(R.id.alertStartHourET);
            CheckBox aEnabledCB = findViewById(R.id.enabledCheckBox);
            alertID = courseAlert.getAlertID();
            String titleString = courseAlert.getTitle();
            String dateString = courseAlert.getDate();
            int hourInt = courseAlert.getHour();
            int alertAct = courseAlert.getActive();
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
        EditText titleEditT = findViewById(R.id.alertCourseTitleET);
        EditText hourEditT = findViewById(R.id.alertStartHourET);
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
        setContentView(R.layout.activity_course_alert_screen);
        EditText tTitle = findViewById(R.id.alertCourseTitleET);
        EditText dueEdit = findViewById(R.id.alertCourseStartET);
        EditText dueHour = findViewById(R.id.alertStartHourET);
        Button cButton = findViewById(R.id.alertCancelButton);
        Button sButton = findViewById(R.id.alertSaveButton);
        Button datePick = findViewById(R.id.alertStartDatePcik);
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
        setContentView(R.layout.activity_course_alert_screen);
    }
}
