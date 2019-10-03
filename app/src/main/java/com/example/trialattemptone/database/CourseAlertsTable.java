package com.example.trialattemptone.database;

public class CourseAlertsTable {
    // define the table name
    public static final String TABLE_COURSEALERTS = "courseAlerts";
    // define the table column names
    public static final String COLUMN_ID = "alertId";
    public static final String COLUMN_NAME = "alertTitle";
    public static final String COLUMN_DATE = "alertDate";
    public static final String COLUMN_HOUR = "alertHour";
    public static final String COLUMN_ACTIVE = "alertActive";
    public static final String COLUMN_COURSE_ID = "alertCourseId";
    public static final String COLUMN_TYPE_ID = "alertTypeID";
    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_DATE, COLUMN_HOUR, COLUMN_ACTIVE, COLUMN_COURSE_ID, COLUMN_TYPE_ID};
    // create the sql creation statement
    public static final String SQL_CREATE = "CREATE TABLE " + TABLE_COURSEALERTS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT," +
            COLUMN_DATE + " TEXT," +
            COLUMN_HOUR + " INTEGER," +
            COLUMN_ACTIVE + " INTEGER," +
            COLUMN_COURSE_ID + " INTEGER," +
            COLUMN_TYPE_ID + " INTEGER);";
    public static final String SQL_DELETE = "DROP TABLE " + TABLE_COURSEALERTS;
}
