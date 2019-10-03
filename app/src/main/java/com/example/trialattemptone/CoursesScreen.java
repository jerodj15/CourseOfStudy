package com.example.trialattemptone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import java.util.Objects;

public class CoursesScreen extends AppCompatActivity {

    Button selectDateButton;
    public static EditText startDate;
    public static EditText endDate;
    public static int termIDSelection;
    public static int statusIDSelection;
    public static int mentorIDSelection;
    public static int longCourseSelectedID;
    public static int selectedTermSpinnerItem;
    static ListView courseListView;
    List<String> courseNamesList = new ArrayList<>();
    List<Integer> courseMentorIDlist = new ArrayList<>();
    List<Integer> courseIdList = new ArrayList<>();
    CalendarView calView;
    DataSource mDataSource;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    public static int termID = 0;
    public static String courseTitle = "";
    public static String startingDate = "";
    public static String endingDate = "";
    public static int startOrEnd = 0; // Start = 0 End = 1
    public static int selectedCourseID;
    public static int selectedMentorID;
    Term currentTerm;
    Date termStart, termEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_screen);
        startDate = findViewById(R.id.startDateET);
        endDate = findViewById(R.id.endDateET);
        mDataSource = new DataSource(this);
        mDataSource.open();
        termID = TermsScreen.selectedTermID;
        if (termID == 0) {
            List<Course> courseListFromDB = mDataSource.getAllCourses();
            for (Course couses : courseListFromDB) {
                courseNamesList.add(couses.getCourseTitle() + " " + couses.getCourseStart());
                courseIdList.add(couses.getCourseId());
                courseMentorIDlist.add(couses.getMentorId());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1, courseNamesList
            );
            courseListView = findViewById(R.id.coursesLV);
            courseListView.setAdapter(adapter);
            Objects.requireNonNull(getSupportActionBar()).setTitle("Courses");
        }
        else
        {
            List<Course> courseListFromDB = mDataSource.getTermsCourses(termID);
            for (Course couses : courseListFromDB) {
                courseNamesList.add(couses.getCourseTitle() + "\nStart: " + couses.getCourseStart() + "\nEnd: " + couses.getCourseEnd() + "\nStatus: " + mDataSource.getCourseStatusByStatusID(couses.getCourseStatusId()));
                courseIdList.add(couses.getCourseId());
                courseMentorIDlist.add(couses.getMentorId());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1, courseNamesList
            );
            courseListView = findViewById(R.id.coursesLV);
            courseListView.setAdapter(adapter);
            getSupportActionBar().setTitle("Courses");
        }
        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCourseID = courseIdList.get(position);
                selectedMentorID = courseMentorIDlist.get(position);
                System.out.println(selectedCourseID);
                System.out.println(selectedMentorID);
                finish();
                Intent intent = new Intent(view.getContext(), CourseDetailsScreen.class);
                startActivity(intent);
            }
        });
        registerForContextMenu(courseListView);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.deleteitem_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        longCourseSelectedID = courseIdList.get(info.position);
        ArrayList<Integer> cycleTermsID = new ArrayList<>();
        switch (item.getItemId()) {
            case R.id.remove_item:
                deleteCourse(info.id);
                this.recreate();
                //   Intent intent = new Intent(this, TermsScreen.class);
                //   startActivity(intent);
                return true;
        }
        return false;
    }

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

    public void addCoursePressed(View view)
    {
        setContentView(R.layout.addcourse_layout);
       setupSpinners();
        getSupportActionBar().setTitle("Add New Course");
        final EditText courseTitleEditText = findViewById(R.id.courseTitleET);
        courseTitleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(courseTitleEditText.getWindowToken(), 0);
                }
            }
        });

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
        EditText titleEditT = findViewById(R.id.courseTitleET);
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
        EditText titleEditT = findViewById(R.id.courseTitleET);
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
        Intent intent = new Intent(this, CoursesScreen.class);
        startActivity(intent);
        getSupportActionBar().setTitle("Courses");

    }

    public void saveButtonPressed(View view)
    {
        EditText titleEditT = findViewById(R.id.courseTitleET);
        String courseTitle = titleEditT.getText().toString();
        Course course = new Course(courseTitle, startingDate, endingDate, termIDSelection, statusIDSelection, mentorIDSelection);
        mDataSource.addCourse(course);
        Toast.makeText(this, "Course Added", Toast.LENGTH_SHORT).show();
        this.finish();
        Intent intent = new Intent(this, CoursesScreen.class);
        startActivity(intent);

    }

    // Create methods for date selection layout
    public void selectButtonPressed(View view)
    {
        if (startOrEnd == 0)
        {
            setContentView(R.layout.addcourse_layout);
            EditText startEdit = findViewById(R.id.startDateET);
            startEdit.setText(startingDate);
            EditText tTitle = findViewById(R.id.courseTitleET);
            tTitle.setText(courseTitle);
           setupSpinners();
            Spinner spinnerTerm = findViewById(R.id.termSpinner);
            spinnerTerm.setSelection(selectedTermSpinnerItem);

        }
        else if (startOrEnd == 1)
        {
            setContentView(R.layout.addcourse_layout);
            EditText startEdit = findViewById(R.id.endDateET);
            startEdit.setText(endingDate);
            EditText tTitle = findViewById(R.id.courseTitleET);
            tTitle.setText(courseTitle);
            setupSpinners();
            Spinner spinnerTerm = findViewById(R.id.termSpinner);
            spinnerTerm.setSelection(selectedTermSpinnerItem);
        }
        if (startingDate.isEmpty() == false && endingDate.isEmpty() == false)
        {
            setContentView(R.layout.addcourse_layout);
            EditText startEditText = findViewById(R.id.startDateET);
            EditText endEditText = findViewById(R.id.endDateET);
            startEditText.setText(startingDate);
            endEditText.setText(endingDate);
            EditText tTitle = findViewById(R.id.courseTitleET);
            tTitle.setText(courseTitle);
           setupSpinners();
            Spinner spinnerTerm = findViewById(R.id.termSpinner);
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
        setContentView(R.layout.addcourse_layout);
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
    public void setupSpinners()
    {

        List<CourseStatus> courseStatusList = mDataSource.getAllCourseStatus();
        List<Mentor> mentorsList = mDataSource.getAllMentors();
        List<String> courseStatusNames = new ArrayList<>();
        List<String> mentorNames = new ArrayList<>();

        for (CourseStatus courseStatus : courseStatusList)
        {
            courseStatusNames.add(courseStatus.getCourseStatus());
        }
        for (Mentor mentor : mentorsList)
        {
            mentorNames.add(mentor.getLastName() + ", " + mentor.getFirstName());
        }
        // Create and setup the spinner for terms
        List<Term> termsList = mDataSource.getAllTerms();
        List<String> termNamesList = new ArrayList<>();
        for (Term term : termsList)
        {
            termNamesList.add(term.getTermName());
        }
        Spinner spinnerTerm = findViewById(R.id.termSpinner);
        ArrayAdapter<String> termAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, termNamesList
        );
        spinnerTerm.setAdapter(termAdapter);
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
        Spinner spinnerStatus = findViewById(R.id.statusSpinner);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, courseStatusNames
        );
        spinnerStatus.setAdapter(statusAdapter);
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
        Spinner spinMentor = findViewById(R.id.spinnerMentor);
        ArrayAdapter<String> mentorAdapter = new ArrayAdapter<>(
          this, android.R.layout.simple_spinner_item, mentorNames
        );
        spinMentor.setAdapter(mentorAdapter);
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


    public void deleteCourse(long selectedItem)
    {

        int selectedForDelete = courseIdList.get((int) selectedItem);
        System.out.println("Selected for delete " + selectedForDelete);

            mDataSource.deleteCourseByID(selectedForDelete);
            Toast.makeText(this, "Course, Course Assessments, and Course Notes have been deleted", Toast.LENGTH_LONG).show();

    }
}
