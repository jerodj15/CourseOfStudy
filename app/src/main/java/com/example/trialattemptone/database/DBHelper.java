package com.example.trialattemptone.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_FILE_NAME = "cos.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context)
    {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TermsTable.SQL_CREATE);
        db.execSQL(CoursesTable.SQL_CREATE);
        db.execSQL(AssessmentsTable.SQL_CREATE);
        db.execSQL(MentorTable.SQL_CREATE);
        db.execSQL(NotesTable.SQL_CREATE);
        db.execSQL(StatusTable.SQL_CREATE);
        db.execSQL(TypesTable.SQL_CREATE);
        db.execSQL(CourseAlertsTable.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(TermsTable.SQL_DELETE);
        db.execSQL(CoursesTable.SQL_DELETE);
        db.execSQL(AssessmentsTable.SQL_DELETE);
        db.execSQL(MentorTable.SQL_DELETE);
        db.execSQL(NotesTable.SQL_DELETE);
        db.execSQL(StatusTable.SQL_DELETE);
        db.execSQL(TypesTable.SQL_DELETE);
        db.execSQL(CourseAlertsTable.SQL_CREATE);
        onCreate(db);
    }


}
