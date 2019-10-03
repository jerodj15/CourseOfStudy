package com.example.trialattemptone.Creators;

import android.content.ContentValues;

import com.example.trialattemptone.database.NotesTable;

public class CourseNote {
    private int noteId;
    private String title;
    private String note;
    private int courseID;

    public CourseNote()
    {

    }


    public CourseNote(String courseTitle, String courseNote, int courseId)
    {
        this.title = courseTitle;
        this.note = courseNote;
        this.courseID = courseId;
    }

    public void setNoteId(int nID)
    {
        this.noteId = nID;
    }
    public int getNoteId()
    {
        return this.noteId;
    }

    public void setTitle(String nTitle)
    {
        this.title = nTitle;
    }
    public String getTitle()
    {
        return this.title;
    }
    public void setNote(String nNote)
    {
        this.note = nNote;
    }
    public String getNote()
    {
        return this.note;
    }
    public void setCourseID(int cID)
    {
        this.courseID = cID;
    }
    public int getCourseID()
    {
        return this.courseID;
    }
    public ContentValues toValues()
    {
        ContentValues values = new ContentValues(5);
        values.put(NotesTable.COLUMN_TITLE, title);
        values.put(NotesTable.COLUMN_NOTE, note);
        values.put(NotesTable.COLUMN_COURSE_ID, courseID);
        return values;
    }

}
