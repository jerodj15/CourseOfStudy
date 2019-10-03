package com.example.trialattemptone.Creators;

import android.content.ContentValues;

import com.example.trialattemptone.database.StatusTable;

public class CourseStatus {

    private String courseStatus;
    private int courseStatusID;

    public CourseStatus()
    {

    }
    public CourseStatus(String statusTitle)
    {
        this.courseStatus = statusTitle;
    }
    public int getCourseStatusID()
    {
        return this.courseStatusID;
    }
    public void setCourseStatusID(int csID)
    {
        this.courseStatusID = csID;
    }
    public void setCourseStatus(String cStatus)
    {
        this.courseStatus = cStatus;
    }
    public String getCourseStatus()
    {
        return this.courseStatus;
    }
    public ContentValues toValues()
    {
        ContentValues values = new ContentValues(1);

        values.put(StatusTable.COLUMN_NAME, courseStatus);
        return values;
    }
}
