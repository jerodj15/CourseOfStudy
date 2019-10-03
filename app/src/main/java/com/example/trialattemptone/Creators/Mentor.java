package com.example.trialattemptone.Creators;

import android.content.ContentValues;

import com.example.trialattemptone.database.MentorTable;

public class Mentor {
    private int mentorId;
    private String firstName;
    private String lastName;
    private String mentorPhone;
    private String mentorEmail;

    public Mentor()
    {

    }

    public Mentor(String fName, String lName, String phoneNumber, String emailAddress)
    {
        this.firstName = fName;
        this.lastName = lName;
        this.mentorPhone = phoneNumber;
        this.mentorEmail = emailAddress;
    }
    public void setMentorId(int mId)
    {
        this.mentorId = mId;
    }
    public int getMentorId()
    {
        return this.mentorId;
    }
    public void setFirstName(String mFirst)
    {
        this.firstName = mFirst;
    }
    public String getFirstName()
    {
        return this.firstName;
    }
    public void setLastName(String mLast)
    {
        this.lastName = mLast;
    }
    public String getLastName()
    {
        return this.lastName;
    }
    public void setMentorPhone(String mPhone)
    {
        this.mentorPhone = mPhone;
    }
    public String getMentorPhone()
    {
        return this.mentorPhone;
    }
    public void setMentorEmail(String mEmail)
    {
        this.mentorEmail = mEmail;
    }
    public String getMentorEmail()
    {
        return this.mentorEmail;
    }
    public ContentValues toValues()
    {
        ContentValues values = new ContentValues(4);

        values.put(MentorTable.COLUMN_FNAME, firstName);
        values.put(MentorTable.COLUMN_LNAME, lastName);
        values.put(MentorTable.COLUMN_PHONE, mentorPhone);
        values.put(MentorTable.COLUMN_EMAIL, mentorEmail);

        return values;
    }
}
