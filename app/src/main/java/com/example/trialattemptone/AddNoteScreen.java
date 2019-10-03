package com.example.trialattemptone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.trialattemptone.Creators.CourseNote;
import com.example.trialattemptone.database.DataSource;

public class AddNoteScreen extends AppCompatActivity {

    DataSource mDataSource;
    public static int selectedCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDataSource = new DataSource(this);
        mDataSource.open();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note_screen);
        selectedCourse = CourseDetailsScreen.selCourseID;
        getSupportActionBar().setTitle("Add New Note");
    }
    // Method for the cancel button
    public void cancelNoteButtonPressed(View view)
    {
        this.finish();
        Intent intent = new Intent(this,CourseDetailsScreen.class);
        startActivity(intent);
    }
    // Method for the save button
    public void saveNoteButtonPressed(View view)
    {
        EditText noteTitle = findViewById(R.id.noteTitleET);
        String title = noteTitle.getText().toString();
        EditText noteBox = findViewById(R.id.noteTextMLT);
        String note = noteBox.getText().toString();
        CourseNote courseNote = new CourseNote(title, note, selectedCourse);
        mDataSource.addCourseNote(courseNote);
        this.finish();
        Intent intent = new Intent(this, CourseDetailsScreen.class);
        startActivity(intent);
    }


}
