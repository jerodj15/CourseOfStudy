package com.example.trialattemptone.Creators;

import android.content.ContentValues;

import com.example.trialattemptone.database.CoursesTable;

public class Course {

    private int courseId;
    private String courseTitle;
    private String courseStart;
    private String courseEnd;
    private int courseTermId;
    private int courseStatusID;
    private int mentorId;
    public Course()
    {

    }

    public Course(String titleString, String startDateString, String endDateString, int termId, int statusId, int mentorID)
    {
        this.courseTitle = titleString;
        this.courseStart = startDateString;
        this.courseEnd = endDateString;
        this.courseTermId = termId;
        this.courseStatusID = statusId;
        this.mentorId = mentorID;
    }

    public void setCourseId (int cId)
    {
        this.courseId = cId;
    }
    public int getCourseId()
    {
        return this.courseId;
    }

    public void setCourseTitle(String cTitle)
    {
        this.courseTitle = cTitle;
    }
    public String getCourseTitle()
    {
        return this.courseTitle;
    }
    public void setCourseStart(String cStart)
    {
        this.courseStart = cStart;
    }
    public String getCourseStart()
    {
        return this.courseStart;
    }
    public void setCourseEnd(String cEnd)
    {
        this.courseEnd = cEnd;
    }
    public String getCourseEnd()
    {
        return this.courseEnd;
    }
    public void setCourseTermId(int cTiD)
    {
        this.courseTermId = cTiD;
    }
    public int getCourseTermId()
    {
        return this.courseTermId;
    }

    public void setCourseStatusID(int csID)
    {
        this.courseStatusID = csID;
    }
    public int getCourseStatusId()
    {
        return this.courseStatusID;
    }
    public void setMentorId(int mId)
    {
        this.mentorId = mId;
    }
    public int getMentorId()
    {
        return this.mentorId;
    }


    public ContentValues toValues()
    {
        ContentValues values = new ContentValues(6);

        values.put(CoursesTable.COLUMN_NAME, courseTitle);
        values.put(CoursesTable.COLUMN_START, courseStart);
        values.put(CoursesTable.COLUMN_END, courseEnd);
        values.put(CoursesTable.COLUMN_TERM_ID, courseTermId);
        values.put(CoursesTable.COLUMN_COURSE_STATUS, courseStatusID);
        values.put(CoursesTable.COLUMN_COURSE_MENTOR_ID, mentorId);
        return values;
    }

}
