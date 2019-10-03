package com.example.trialattemptone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trialattemptone.Creators.CourseNote;
import com.example.trialattemptone.database.DataSource;

import java.io.Console;

public class NoteDetailsScreen extends AppCompatActivity {

    int selectedNoteID;
    int selectedCourseID;
    CourseNote selectedNote;
    DataSource mDataSource;
    Button mOkayButton;
    Button mSaveButton;
    EditText mEditTitle;
    EditText mNoteNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details_screen);
        mDataSource = new DataSource(this);
        mDataSource.open();
        selectedNoteID = CourseDetailsScreen.selectedNoteID;
        selectedNote = mDataSource.getNoteByNoteID(selectedNoteID);
        selectedCourseID = CourseDetailsScreen.selCourseID;
        mOkayButton = findViewById(R.id.detailOkayButton);
        mSaveButton = findViewById(R.id.detailSaveButton);
        mEditTitle = findViewById(R.id.detailNoteTitleET);
        mNoteNote = findViewById(R.id.detailsNoteNoteET);
        // Populate the screen with the note information
        mEditTitle.setText(selectedNote.getTitle());
        mNoteNote.setText(selectedNote.getNote());
        getSupportActionBar().setTitle("Details for " + selectedNote.getTitle());
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.notedetails_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.edit_note:
                mSaveButton.setVisibility(View.VISIBLE);
                mEditTitle.setEnabled(true);
                mNoteNote.setEnabled(true);
                mOkayButton.setText("Cancel");
                return true;
            case R.id.share_note:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = mNoteNote.getText().toString();
                String shareSub = mEditTitle.getText().toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void okayButtonPressed(View view)
    {
        finish();
        Intent intent = new Intent(this, CourseDetailsScreen.class);
        startActivity(intent);
    }
    public void saveButtonPressed(View view)
    {
        EditText editNoteTitle = findViewById(R.id.detailNoteTitleET);
        EditText editNoteNote = findViewById(R.id.detailsNoteNoteET);
        String eNoteTitle = editNoteTitle.getText().toString();
        String eNoteNote = editNoteNote.getText().toString();
        CourseNote courseNoteEdited = new CourseNote(eNoteTitle, eNoteNote, selectedCourseID);
        mDataSource.updateCourseNote(courseNoteEdited, selectedNoteID);
        finish();
        Intent intent = new Intent(this, CourseDetailsScreen.class);
        startActivity(intent);

    }

}
