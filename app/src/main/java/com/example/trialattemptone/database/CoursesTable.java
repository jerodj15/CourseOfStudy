package com.example.trialattemptone.database;

public class CoursesTable {
    // define the table name
    public static final String TABLE_COURSES = "courses";
    // define the table column names
    public static final String COLUMN_ID = "courseId";
    public static final String COLUMN_NAME = "courseTitle";
    public static final String COLUMN_START = "courseStart";
    public static final String COLUMN_END = "courseEnd";
    public static final String COLUMN_TERM_ID = "termId";
    public static final String COLUMN_COURSE_STATUS = "statusId";
    public static final String COLUMN_COURSE_MENTOR_ID = "mentorId";
    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_START, COLUMN_END, COLUMN_TERM_ID, COLUMN_COURSE_STATUS, COLUMN_COURSE_MENTOR_ID};
    // create the sql creation statement
    public static final String SQL_CREATE = "CREATE TABLE " + TABLE_COURSES + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT," +
            COLUMN_START + " TEXT," +
            COLUMN_END + " TEXT," +
            COLUMN_TERM_ID + " INTEGER," +
            COLUMN_COURSE_STATUS + " INTEGER," +
            COLUMN_COURSE_MENTOR_ID + " INTEGER);";
    public static final String SQL_DELETE = "DROP TABLE " + TABLE_COURSES;
}
