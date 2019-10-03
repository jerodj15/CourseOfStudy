package com.example.trialattemptone.database;

public class AssessmentsTable {
    // define the table name
    public static final String TABLE_ASSESSMENTS = "assessments";
    // define the table column names
    public static final String COLUMN_ID = "assessmentId";
    public static final String COLUMN_NAME = "assessmentTitle";
    public static final String COLUMN_END = "assessmentEnd";
    public static final String COLUMN_COURSE_ID = "courseID";
    public static final String COLUMN_TYPE_ID = "typeID";
    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_END, COLUMN_COURSE_ID, COLUMN_TYPE_ID};
    // create the sql creation statement
    public static final String SQL_CREATE = "CREATE TABLE " + TABLE_ASSESSMENTS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT," +
            COLUMN_END + " TEXT," +
            COLUMN_COURSE_ID + " INTEGER," +
            COLUMN_TYPE_ID + " INTEGER);";
    public static final String SQL_DELETE = "DROP TABLE " + TABLE_ASSESSMENTS;
}
