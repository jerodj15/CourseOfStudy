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
import android.widget.Toast;

import com.example.trialattemptone.Creators.Course;
import com.example.trialattemptone.Creators.CourseStatus;
import com.example.trialattemptone.Creators.Mentor;
import com.example.trialattemptone.Creators.Term;
import com.example.trialattemptone.database.DataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class EditCourseScreen extends AppCompatActivity {

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    DataSource mDataSource;
    public static int selectedCourseForEdit;
    public static Term currentTerm;
    public static Course selectedEditCourse;
    public static String courseTitle;
    public int startOrEnd;
    CalendarView calView;
    public static String startingDate;
    public static String endingDate = "";
    Button selectDateButton;
    Date termStart;
    Date termEnd;
    public static int termIDSelection;
    public static int statusIDSelection;
    public static int mentorIDSelection;
    public static int selectedTermSpinnerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course_screen);
        selectedCourseForEdit = CourseDetailsScreen.selCourseID;
        mDataSource = new DataSource(this);
        mDataSource.open();
        selectedEditCourse = mDataSource.getCourseByID(selectedCourseForEdit);
        EditText ecTitle = findViewById(R.id.eCourseTitleET);
        EditText ecStartDate = findViewById(R.id.eStartDateET);
        EditText ecEndDate = findViewById(R.id.eEndDateET);
        ecTitle.setText(selectedEditCourse.getCourseTitle());
        ecStartDate.setText(selectedEditCourse.getCourseStart());
        ecEndDate.setText(selectedEditCourse.getCourseEnd());
        Spinner ectSpinner = findViewById(R.id.eTermSpinner);
        Spinner ecsSpinner = findViewById(R.id.eStatusSpinner);
        Spinner ecmSpinner = findViewById(R.id.eMentorSpinner);
        setupSpinners();
        getSupportActionBar().setTitle("Edit " + selectedEditCourse.getCourseTitle());

    }

    // Methods for the addcourse_layout
    public void startDatePickPressed(View view)
    {
        currentTerm = mDataSource.getTermByTermID(termIDSelection);
        try
        {
            termStart = sdf.parse(currentTerm.getStartDate());
            termEnd = sdf.parse(currentTerm.getEndDate());
        }catch (ParseException ex)
        {
            System.out.println(ex.getLocalizedMessage());
        }
        EditText titleEditT = findViewById(R.id.eCourseTitleET);
        if (titleEditT.getText().toString().isEmpty() == false)
        {
            courseTitle = titleEditT.getText().toString();

        }
        startOrEnd =0;
        setContentView(R.layout.activity_date_picker);
        getSupportActionBar().setTitle("Pick a Starting Date");
        calView = findViewById(R.id.calendarView);
        long date = calView.getDate();
        // Setup the starting date
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.setTime(new Date(calView.getDate()));
        startingDate = sdf.format(new Date(calendar.getTime().getTime()));
        selectDateButton = findViewById(R.id.selectButton);
        calView = findViewById(R.id.calendarView);
        calView.setMinDate(termStart.getTime());
        calView.setMaxDate(termEnd.getTime());
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

                startingDate = newMonth + "/" + newDay + "/" + newYear;
                System.out.println(startingDate);
            }
        });

    }
    public void endDatePickPressed(View view)
    {
        currentTerm = mDataSource.getTermByTermID(termIDSelection);
        try
        {
            termStart = sdf.parse(currentTerm.getStartDate());
            termEnd = sdf.parse(currentTerm.getEndDate());
        }catch (ParseException ex)
        {
            System.out.println(ex.getLocalizedMessage());
        }
        EditText titleEditT = findViewById(R.id.eCourseTitleET);
        if (titleEditT.getText().toString().isEmpty() == false)
        {
            courseTitle = titleEditT.getText().toString();
        }
        startOrEnd = 1;
        setContentView(R.layout.activity_date_picker);
        getSupportActionBar().setTitle("Pick a Ending Date");
        selectDateButton = findViewById(R.id.selectButton);
        calView = findViewById(R.id.calendarView);
        calView.setMinDate(termStart.getTime());
        calView.setMaxDate(termEnd.getTime());
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

                endingDate = newMonth + "/" + newDay + "/" + newYear;
                System.out.println(endingDate);
            }
        });
    }
    public void createMentorButtonPressed(View view)
    {
        setContentView(R.layout.addmentor_layout);
    }

    public void cancelButtonPressed(View view)
    {
        finish();
        Intent intent = new Intent(this, CourseDetailsScreen.class);
        startActivity(intent);
    }

    public void saveButtonPressed(View view)
    {

        EditText titleEditT = findViewById(R.id.eCourseTitleET);
        EditText esdET = findViewById(R.id.eStartDateET);
        EditText eedET = findViewById(R.id.eEndDateET);
        String editStartDate = esdET.getText().toString();
        String editEndDate = eedET.getText().toString();
        String courseTitle = titleEditT.getText().toString();
        Course course = new Course(courseTitle, editStartDate, editEndDate, termIDSelection, statusIDSelection, mentorIDSelection);
        mDataSource.updateCourse(course, selectedCourseForEdit);
        Toast.makeText(this, "Course Edited", Toast.LENGTH_SHORT).show();
        this.finish();
        Intent intent = new Intent(this, CourseDetailsScreen.class);
        startActivity(intent);

    }

    // Create methods for date selection layout
    public void selectButtonPressed(View view)
    {
        if (startOrEnd == 0)
        {
            setContentView(R.layout.activity_edit_course_screen);
            EditText startEdit = findViewById(R.id.eStartDateET);
            startEdit.setText(startingDate);
            EditText tTitle = findViewById(R.id.eCourseTitleET);
            tTitle.setText(courseTitle);
            setupSpinners();
            Spinner spinnerTerm = findViewById(R.id.eTermSpinner);
            spinnerTerm.setSelection(selectedTermSpinnerItem);

        }
        else if (startOrEnd == 1)
        {
            setContentView(R.layout.activity_edit_course_screen);
            EditText startEdit = findViewById(R.id.eStartDateET);
            startEdit.setText(endingDate);
            EditText tTitle = findViewById(R.id.eCourseTitleET);
            tTitle.setText(courseTitle);
            setupSpinners();
            Spinner spinnerTerm = findViewById(R.id.eTermSpinner);
            spinnerTerm.setSelection(selectedTermSpinnerItem);
        }
        if (startingDate.isEmpty() == false && endingDate.isEmpty() == false)
        {
            setContentView(R.layout.activity_edit_course_screen);
            EditText startEditText = findViewById(R.id.eStartDateET);
            EditText endEditText = findViewById(R.id.eEndDateET);
            startEditText.setText(startingDate);
            endEditText.setText(endingDate);
            EditText tTitle = findViewById(R.id.eCourseTitleET);
            tTitle.setText(courseTitle);
            setupSpinners();
            Spinner spinnerTerm = findViewById(R.id.eTermSpinner);
            spinnerTerm.setSelection(selectedTermSpinnerItem);
        }


    }
    public void cancelDatePick(View view)
    {
        setContentView(R.layout.addcourse_layout);
        setupSpinners();
    }

    // methods for addmentor_layout

    public void cancelMentorButton(View view)
    {
        setContentView(R.layout.activity_edit_course_screen);
        setupSpinners();
    }
    public void saveMentorButtonPressed(View view)
    {
        EditText firstNameET = findViewById(R.id.fNameET);
        String first = firstNameET.getText().toString();
        EditText lastNameET = findViewById(R.id.lNameET);
        String last = lastNameET.getText().toString();
        EditText phoneNumberET = findViewById(R.id.phoneET);
        String phone = phoneNumberET.getText().toString();
        EditText emailAddressET = findViewById(R.id.emailET);
        String mail = emailAddressET.getText().toString();
        Mentor mentor = new Mentor(first, last, phone, mail);
        mDataSource.addMentor(mentor);
        Toast.makeText(this, "Mentor Added", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.addcourse_layout);
        setupSpinners();
        EditText startEditText = findViewById(R.id.startDateET);
        EditText endEditText = findViewById(R.id.endDateET);
        startEditText.setText(startingDate);
        endEditText.setText(endingDate);
        EditText tTitle = findViewById(R.id.courseTitleET);
        tTitle.setText(courseTitle);
    }


    // Other methods to avoid repetition
    public void setupSpinners() {

        List<CourseStatus> courseStatusList = mDataSource.getAllCourseStatus();
        List<Mentor> mentorsList = mDataSource.getAllMentors();
        List<String> courseStatusNames = new ArrayList<>();
        List<String> mentorNames = new ArrayList<>();

        for (CourseStatus courseStatus : courseStatusList) {
            courseStatusNames.add(courseStatus.getCourseStatus());
        }
        for (Mentor mentor : mentorsList) {
            mentorNames.add(mentor.getLastName() + ", " + mentor.getFirstName());
        }
        // Create and setup the spinner for terms
        List<Term> termsList = mDataSource.getAllTerms();
        List<String> termNamesList = new ArrayList<>();
        for (Term term : termsList) {
            termNamesList.add(term.getTermName());
        }
        Spinner spinnerTerm = findViewById(R.id.eTermSpinner);
        ArrayAdapter<String> termAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, termNamesList
        );
        spinnerTerm.setAdapter(termAdapter);
        for (int i = 0; i < termsList.size(); i++)
        {
            if (selectedEditCourse.getCourseTermId() == termsList.get(i).getTermID())
            {
                spinnerTerm.setSelection(i);
            }
        }
        spinnerTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
                selectedTermSpinnerItem = position;
                System.out.println(parent.getItemAtPosition(position).toString());
                System.out.println(mDataSource.getTermId(parent.getItemAtPosition(position).toString()));
                termIDSelection = mDataSource.getTermId((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Create and setup the course status spinner
        Spinner spinnerStatus = findViewById(R.id.eStatusSpinner);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, courseStatusNames
        );
        spinnerStatus.setAdapter(statusAdapter);
        for (int i = 0; i < courseStatusList.size(); i++)
        {
            if (selectedEditCourse.getCourseStatusId() == courseStatusList.get(i).getCourseStatusID())
            {
                spinnerStatus.setSelection(i);
            }
        }
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
                System.out.println(parent.getItemAtPosition(position).toString());
                System.out.println(mDataSource.getCourseStatusId(parent.getItemAtPosition(position).toString()));
                statusIDSelection = mDataSource.getCourseStatusId((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Create and setup the mentor spinner
        Spinner spinMentor = findViewById(R.id.eMentorSpinner);
        ArrayAdapter<String> mentorAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, mentorNames
        );
        spinMentor.setAdapter(mentorAdapter);
        for (int i = 0; i < mentorsList.size(); i++)
        {
            if (selectedEditCourse.getMentorId() == mentorsList.get(i).getMentorId())
            {
                spinMentor.setSelection(i);
            }
        }
        spinMentor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
                String mentorFullName = parent.getItemAtPosition(position).toString().replace(",", " ");
                String[] fullName = mentorFullName.split("\\s+");
                mentorIDSelection = mDataSource.getMentorID(fullName[0], fullName[1]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
