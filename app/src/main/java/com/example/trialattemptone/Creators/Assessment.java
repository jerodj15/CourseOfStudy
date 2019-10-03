package com.example.trialattemptone.Creators;

import android.content.ContentValues;

import com.example.trialattemptone.database.AssessmentsTable;

public class Assessment {
    private int assId;
    private String assTitle;
    private String assEnd;
    private int assCourseID;
    private int assTypeID;


    public Assessment()
    {

    }

    public Assessment(String title, String endDate, int aCourseId, int assTypeId)
    {
        this.assTitle = title;
        this.assEnd = endDate;
        this.assCourseID = aCourseId;
        this.assTypeID = assTypeId;
    }

    public ContentValues toValues()
    {
        ContentValues values = new ContentValues(4);

        values.put(AssessmentsTable.COLUMN_NAME, assTitle);
        values.put(AssessmentsTable.COLUMN_END, assEnd);
        values.put(AssessmentsTable.COLUMN_COURSE_ID, assCourseID);
        values.put(AssessmentsTable.COLUMN_TYPE_ID, assTypeID);

        return values;
    }
    public void setAssId(int aID)
    {
        this.assId = aID;
    }
    public int getAssId()
    {
        return this.assId;
    }
    public void setAssTitle(String aTitle)
    {
        this.assTitle = aTitle;
    }
    public String getAssTitle()
    {
        return this.assTitle;
    }
    public void setAssEnd(String aEnd)
    {
        this.assEnd = aEnd;
    }
    public String getAssEnd()
    {
        return this.assEnd;
    }
    public void setAssCourseID(int acID)
    {
        this.assCourseID = acID;
    }
    public int getAssCourseID()
    {
        return this.assCourseID;
    }
    public void setAssTypeID(int assTypeId)
    {
        this.assTypeID = assTypeId;
    }
    public int getAssTypeID()
    {
        return this.assTypeID;
    }

}
