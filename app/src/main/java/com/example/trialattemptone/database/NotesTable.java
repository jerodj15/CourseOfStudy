package com.example.trialattemptone.database;

public class NotesTable {
    // define the table name
    public static final String TABLE_NOTES = "notes";
    // define the table column names
    public static final String COLUMN_ID = "noteId";
    public static final String COLUMN_TITLE = "noteTitle";
    public static final String COLUMN_NOTE = "noteNote";
    public static final String COLUMN_COURSE_ID = "courseId";
    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_TITLE, COLUMN_NOTE, COLUMN_COURSE_ID};
    // create the sql creation statement
    public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NOTES + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_TITLE + " TEXT," +
            COLUMN_NOTE + " TEXT," +
            COLUMN_COURSE_ID + " INTEGER);";
    public static final String SQL_DELETE = "DROP TABLE " + TABLE_NOTES;
}
