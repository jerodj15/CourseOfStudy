package com.example.trialattemptone.Creators;

import android.content.ContentValues;

import com.example.trialattemptone.database.TermsTable;

public class Term {
    private int termID;
    private String termName;
    private String startDate;
    private String endDate;

    public Term()
    {

    }

    public Term(String termNameString, String starDateString, String endDateString)
    {
        this.termName = termNameString;
        this.startDate = starDateString;
        this.endDate = endDateString;
    }

    public void setTermID(int tId)
    {
        this.termID = tId;
    }
    public int getTermID()
    {
        return this.termID;
    }
    public String getTermName()
    {
        return this.termName;
    }
    public void setTermName(String tName)
    {
        this.termName = tName;
    }
    public String getStartDate()
    {
        return this.startDate;
    }
    public void setStartDate(String sDate)
    {
        this.startDate = sDate;
    }
    public String getEndDate()
    {
        return this.endDate;
    }
    public void setEndDate(String eDate)
    {
        this.endDate = eDate;
    }
    public ContentValues toValues()
    {
        ContentValues values = new ContentValues(4);

        values.put(TermsTable.COLUMN_NAME, termName);
        values.put(TermsTable.COLUMN_START, startDate);
        values.put(TermsTable.COLUMN_END, endDate);

        return values;
    }
}
