package com.example.trialattemptone.Creators;

import android.content.ContentValues;

import com.example.trialattemptone.database.TypesTable;

public class AssessmentType {
    private int assessmentTypeID;
    private String assessmentType;

    public AssessmentType()
    {

    }
    public AssessmentType(String assTitle)
    {
        this.assessmentType = assTitle;
    }

    public void setAssessmentTypeID(int atID)
    {
        this.assessmentTypeID = atID;
    }
    public int getAssessmentTypeID()
    {
        return this.assessmentTypeID;
    }
    public void setAssessmentType(String aType)
    {
        this.assessmentType = aType;
    }
    public String getAssessmentType()
    {
        return this.assessmentType;
    }
    public ContentValues toValues()
    {
        ContentValues values = new ContentValues(1);
        values.put(TypesTable.COLUMN_NAME, assessmentType);
        return values;
    }
}
