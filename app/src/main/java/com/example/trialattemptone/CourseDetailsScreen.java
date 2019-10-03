package com.example.trialattemptone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trialattemptone.Creators.Assessment;
import com.example.trialattemptone.Creators.Course;
import com.example.trialattemptone.Creators.CourseNote;
import com.example.trialattemptone.Creators.Mentor;
import com.example.trialattemptone.database.DataSource;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailsScreen extends AppCompatActivity {
    DataSource mDataSource;
    public static Course selectedCourse;
    public static Mentor selectedMentor;
    public static CourseNote selectedNote;
    public static int selCourseID;
    public static int selectedNoteID;
    public static int selectedAssId;
    public static int longSelectedID;
    public boolean isAssLongSelected = false;
    public boolean isNoteLongSelected = false;
    TextView mCourseNameTV;
    EditText firstName;
    EditText lastName;
    EditText phoneNumber;
    EditText emailAddress;
    ListView assessmentsListView;
    ListView notesListView;
    public static List<String> assessmentNames = new ArrayList<>();
    public static List<Integer> assessmentsID = new ArrayList<>();
    public static List<String> notesTitles = new ArrayList<>();
    public static List<Integer> notesId = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        assessmentNames.clear();
        assessmentsID.clear();
        notesTitles.clear();
        notesId.clear();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details_screen);
        mDataSource = new DataSource(this);
        mDataSource.open();
        // Get the previously selected item information
        selCourseID = CoursesScreen.selectedCourseID;
        final int selMentorID = CoursesScreen.selectedMentorID;
        selectedCourse = mDataSource.getCourseByID(selCourseID);
        selectedMentor = mDataSource.getSelectedCourseMentor(selMentorID);
        // Setup the title for the course
        mCourseNameTV = findViewById(R.id.courseNameTV);
        mCourseNameTV.setText("Status: " + mDataSource.getCourseStatusByStatusID(selectedCourse.getCourseStatusId()));
        // Setup mentor information
        firstName = findViewById(R.id.mFnameET);
        lastName = findViewById(R.id.mLnameET);
        phoneNumber = findViewById(R.id.mPhoneET);
        emailAddress = findViewById(R.id.mEmailET);
        firstName.setText(selectedMentor.getFirstName());
        lastName.setText(selectedMentor.getLastName());
        phoneNumber.setText(selectedMentor.getMentorPhone());
        emailAddress.setText(selectedMentor.getMentorEmail());

        // Setup assessments information
        assessmentsListView = findViewById(R.id.assessmentsLV);
        List<Assessment> assessmentList = mDataSource.getAssessmentsByCourseID(selCourseID);
        for (Assessment assessment : assessmentList)
        {
            assessmentNames.add("Title: " + assessment.getAssTitle() + "\nType: " + mDataSource.getAssessmentTypeByID(assessment.getAssTypeID()) + "\nDue Date: " + assessment.getAssEnd());
            assessmentsID.add(assessment.getAssId());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
          this, android.R.layout.simple_list_item_1, assessmentNames
        );
        assessmentsListView.setAdapter(adapter);
        assessmentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedAssId = assessmentsID.get(position);
                finish();
                Intent intent = new Intent(view.getContext(), AssessmentDetailsScreen.class);
                startActivity(intent);
            }
        });
        assessmentsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                isAssLongSelected = true;
                isNoteLongSelected = false;
                longSelectedID = assessmentsID.get(position);
                registerForContextMenu(assessmentsListView);
                return false;
            }
        });

        // Setup notes information
        notesListView = findViewById(R.id.notesLV);
        final List<CourseNote> notesList = mDataSource.getCourseNoteByCourseID(selCourseID);
        for (CourseNote note : notesList)
        {
            notesTitles.add(note.getTitle());
            notesId.add(note.getNoteId());

        }
        ArrayAdapter<String> noteAdapter = new ArrayAdapter<>(
          this, android.R.layout.simple_list_item_1, notesTitles
        );
        notesListView.setAdapter(noteAdapter);
        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedNoteID = notesId.get(position);
                finish();
                Intent intent = new Intent(view.getContext(), NoteDetailsScreen.class);
                startActivity(intent);
            }
        });
        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                isNoteLongSelected = true;
                isAssLongSelected = false;
                longSelectedID = notesId.get(position);
                registerForContextMenu(notesListView);
                return false;
            }
        });
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        getSupportActionBar().setTitle("Details for " + selectedCourse.getCourseTitle());
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

        switch (item.getItemId()) {
            case R.id.remove_item:
                if (isNoteLongSelected == true)
                {
                    deleteSelectedNote(longSelectedID);
                }
                if (isAssLongSelected == true)
                {
                    deleteSelectedAssessment(longSelectedID);
                }

                this.recreate();
                //   Intent intent = new Intent(this, TermsScreen.class);
                //   startActivity(intent);
                return true;
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.coursedetails_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.add_assignment:
                this.finish();
                Intent intent = new Intent(this, AddAssignmentScreen.class);
                startActivity(intent);
                return true;
            case R.id.add_note:
                this.finish();
                Intent intent1 = new Intent(this,AddNoteScreen.class);
                startActivity(intent1);
                return true;
            case R.id.edit_course:
                this.finish();
                Intent intent2 = new Intent(this, EditCourseScreen.class);
                startActivity(intent2);
                return true;
            case  R.id.courseAlert:
                this.finish();
                Intent intent3 = new Intent(this, CourseAlertScreen.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void deleteSelectedNote(int selectedItem)
    {
        mDataSource.deleteCourseNote(selectedItem);
        Toast.makeText(this, "Selected Note Has Been Deleted", Toast.LENGTH_LONG).show();
    }
    public void deleteSelectedAssessment(int selectedItem)
    {
        mDataSource.deleteAssessment(selectedItem);
        Toast.makeText(this, "Selected Assessment Has Been Deleted", Toast.LENGTH_LONG).show();

    }




}
