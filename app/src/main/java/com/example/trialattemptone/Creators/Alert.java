package com.example.trialattemptone.Creators;

import android.content.ContentValues;

import com.example.trialattemptone.database.CourseAlertsTable;

public class Alert {

    private int aID;
    private String aTitle;
    private String aDate;
    private int aHour;
    private int aActive;
    private int aCourseId;
    private int typeId;
    public Alert()
    {

    }

    public Alert(String alertTitle, String alertDate, int alertHour, int isActive, int courseID, int typeID)
    {
        this.aTitle = alertTitle;
        this.aDate = alertDate;
        this.aHour = alertHour;
        this.aActive = isActive;
        this.aCourseId = courseID;
        this.typeId = typeID;
    }
    public void setAlertID(int alertID)
    {
        this.aID = alertID;
    }
    public int getAlertID()
    {
        return this.aID;
    }

    public void setTitle(String title)
    {
        this.aTitle = title;
    }
    public String getTitle()
    {
        return this.aTitle;
    }
    public void setDate(String date)
    {
        this.aDate = date;
    }
    public String getDate()
    {
        return this.aDate;
    }
    public void setHour(int hour)
    {
        this.aHour = hour;
    }
    public int getHour()
    {
        return this.aHour;
    }
    public void setActive(int active)
    {
        this.aActive = active;
    }
    public int getActive()
    {
        return this.aActive;
    }
    public void setCourseID(int courseID)
    {
        this.aCourseId = courseID;
    }
    public int getCourseID()
    {
        return this.aCourseId;
    }
    public void setTypeId(int tID)
    {
        this.typeId = tID;
    }
    public int getTypeId()
    {
        return this.typeId;
    }


    public ContentValues toValues()
    {
        ContentValues values = new ContentValues(6);

        values.put(CourseAlertsTable.COLUMN_NAME, aTitle);
        values.put(CourseAlertsTable.COLUMN_DATE, aDate);
        values.put(CourseAlertsTable.COLUMN_HOUR, aHour);
        values.put(CourseAlertsTable.COLUMN_ACTIVE, aActive);
        values.put(CourseAlertsTable.COLUMN_COURSE_ID, aCourseId);
        values.put(CourseAlertsTable.COLUMN_TYPE_ID, typeId);
        return values;
    }



}
