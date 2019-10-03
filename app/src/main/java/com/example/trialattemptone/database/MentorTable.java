package com.example.trialattemptone.database;

import android.content.ContentValues;

public class MentorTable {
    // define the table name
    public static final String TABLE_MENTORS = "mentors";
    // define the table column names
    public static final String COLUMN_ID = "mentorId";
    public static final String COLUMN_FNAME = "mentorFirst";
    public static final String COLUMN_LNAME = "mentorLast";
    public static final String COLUMN_PHONE = "mentorPhone";
    public static final String COLUMN_EMAIL = "mentorEmail";
    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_FNAME, COLUMN_LNAME, COLUMN_PHONE, COLUMN_EMAIL};
    // create the sql creation statement
    public static final String SQL_CREATE = "CREATE TABLE " + TABLE_MENTORS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_FNAME + " TEXT," +
            COLUMN_LNAME + " TEXT," +
            COLUMN_PHONE + " TEXT," +
            COLUMN_EMAIL + " TEXT" + ");";
    public static final String SQL_DELETE = "DROP TABLE " + TABLE_MENTORS;
}
