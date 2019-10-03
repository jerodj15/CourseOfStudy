package com.example.trialattemptone.database;

public class TypesTable {
    // define the table name
    public static final String TABLE_TYPES = "types";
    // define the table column names
    public static final String COLUMN_ID = "typeId";
    public static final String COLUMN_NAME = "typeTitle";
    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_NAME};
    // create the sql creation statement
    public static final String SQL_CREATE = "CREATE TABLE " + TABLE_TYPES + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT" + ");";
    public static final String SQL_DELETE = "DROP TABLE " + TABLE_TYPES;
}
