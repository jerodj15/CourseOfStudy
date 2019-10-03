package com.example.trialattemptone.database;

public class TermsTable {
    // define the table name
    public static final String TABLE_TERMS = "terms";
    // define the table column names
    public static final String COLUMN_ID = "termId";
    public static final String COLUMN_NAME = "termTitle";
    public static final String COLUMN_START = "termStart";
    public static final String COLUMN_END = "termEnd";

    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_START, COLUMN_END};
    // create the sql creation statement
    public static final String SQL_CREATE = "CREATE TABLE " + TABLE_TERMS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT," +
            COLUMN_START + " TEXT," +
            COLUMN_END + " TEXT" + ");";
    public static final String SQL_DELETE = "DROP TABLE " + TABLE_TERMS;
}
