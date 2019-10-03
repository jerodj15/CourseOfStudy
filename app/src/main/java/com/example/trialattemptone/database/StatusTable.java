package com.example.trialattemptone.database;

public class StatusTable {
    // define the table name
    public static final String TABLE_STATUS = "status";
    // define the table column names
    public static final String COLUMN_ID = "statusId";
    public static final String COLUMN_NAME = "statusTitle";
    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_NAME};
    // create the sql creation statement
    public static final String SQL_CREATE = "CREATE TABLE " + TABLE_STATUS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT" + ");";
    public static final String SQL_DELETE = "DROP TABLE " + TABLE_STATUS;
}
