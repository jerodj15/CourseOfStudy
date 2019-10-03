package com.example.trialattemptone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;
import android.widget.TextView;

import com.example.trialattemptone.CourseDetailsScreen;
import com.example.trialattemptone.Creators.Alert;
import com.example.trialattemptone.Creators.Assessment;
import com.example.trialattemptone.Creators.AssessmentType;
import com.example.trialattemptone.Creators.Course;
import com.example.trialattemptone.Creators.CourseNote;
import com.example.trialattemptone.Creators.CourseStatus;
import com.example.trialattemptone.Creators.Mentor;
import com.example.trialattemptone.Creators.Term;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DataSource {
    private Context mContext;
    private SQLiteDatabase mDatabase;
    SQLiteOpenHelper mDbHelper;

    private static final String[] courseStats = {"In Progress", "Completed", "Dropped", "Plan To Take"};
    public static final List<String> courseStatusList = Arrays.asList(courseStats);
    private static final String[] assessType = {"Objective", "Performance"};
    public static final List<String> assessmentTypesList = Arrays.asList(assessType);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    // Variables for the conditional methods
    public static Date curDate;
    public static int curTerm;

    public DataSource(Context context)
    {
        this.mContext = context;
        mDbHelper = new DBHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void open()
    {
        mDatabase = mDbHelper.getWritableDatabase();
    }
    public void close()
    {
        mDatabase.close();
    }

    // Term methods
        public Term createTerm(Term term)
    {
        ContentValues values = term.toValues();
        mDatabase.insert(TermsTable.TABLE_TERMS, null, values);
        return term;
    }
    public void seedDataBase(List<String> courseStatuses, List<String> assessTypes)
    {
        long numCourseStatusItems = getStatusCount();
        long numAssessmentTypeItems = getAssessmentTypesCount();
        if (numCourseStatusItems == 0)
        {
            for (int i = 0; i < courseStatuses.size(); i++)
            {
                try
                {
                    CourseStatus courseStatus = new CourseStatus(courseStatuses.get(i));
                    createCourseStatus(courseStatus);
                }
                catch (SQLiteException e)
                {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }
        if (numAssessmentTypeItems == 0)
        {
            for (int i = 0; i < assessTypes.size(); i++)
            {
                try
                {
                    AssessmentType assessmentType = new AssessmentType(assessTypes.get(i));
                    createAssessmentType(assessmentType);
                }
                catch (SQLiteException e)
                {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }
    }
    public void deleteTermByID(int termID)
    {
        mDatabase.delete(TermsTable.TABLE_TERMS, TermsTable.COLUMN_ID + " = " + termID, null);
    }

    public long getTermCount()
    {
        return DatabaseUtils.queryNumEntries(mDatabase, TermsTable.TABLE_TERMS);
    }
    public List<Term> getAllTerms()
    {
        List<Term> terms = new ArrayList<>();
        Cursor cursor = mDatabase.query(TermsTable.TABLE_TERMS, TermsTable.ALL_COLUMNS, null, null, null, null, TermsTable.COLUMN_START);

        while(cursor.moveToNext())
        {
            Term term = new Term();
            term.setTermID(cursor.getInt(cursor.getColumnIndex(TermsTable.COLUMN_ID)));
            term.setTermName(cursor.getString(cursor.getColumnIndex(TermsTable.COLUMN_NAME)));
            term.setStartDate(cursor.getString(cursor.getColumnIndex(TermsTable.COLUMN_START)));
            term.setEndDate(cursor.getString(cursor.getColumnIndex(TermsTable.COLUMN_END)));
            terms.add(term);
        }
        cursor.close();
        return terms;
    }
    public void addTerm(Term term)
    {
        ContentValues values = term.toValues();
        mDatabase.insert(TermsTable.TABLE_TERMS,null, values);
    }

    public int getTermId(String termTitle)
    {
        int termID = 0;
        Cursor cursor = mDatabase.query(TermsTable.TABLE_TERMS, TermsTable.ALL_COLUMNS, TermsTable.COLUMN_NAME + " = '" + termTitle +"'",null,null,null,null);

        while (cursor.moveToNext())
        {
            termID = cursor.getInt(cursor.getColumnIndex(TermsTable.COLUMN_ID));
        }
        cursor.close();
        return termID;
    }
    public Term getTermByTermID(int termID)
    {
        Term term = new Term();
        Cursor cursor = mDatabase.query(TermsTable.TABLE_TERMS, TermsTable.ALL_COLUMNS, TermsTable.COLUMN_ID + " = " + termID, null,null,null,null);
        while (cursor.moveToNext())
        {
            term.setTermID(cursor.getInt(cursor.getColumnIndex(TermsTable.COLUMN_ID)));
            term.setTermName(cursor.getString(cursor.getColumnIndex(TermsTable.COLUMN_NAME)));
            term.setStartDate(cursor.getString(cursor.getColumnIndex(TermsTable.COLUMN_START)));
            term.setEndDate(cursor.getString(cursor.getColumnIndex(TermsTable.COLUMN_END)));
        }
        return term;
    }
    // Course Methods

    public Course createCourse(Course course)
    {
        ContentValues values = course.toValues();
        mDatabase.insert(CoursesTable.TABLE_COURSES, null,values);
        return course;
    }
    public void updateCourse(Course course, int courseID)
    {
        ContentValues values = course.toValues();
        mDatabase.update(CoursesTable.TABLE_COURSES, values, CoursesTable.COLUMN_ID + " = '" + courseID + "'", null);
    }
    // Delete course, assessments associated with the course, and notes associated with the course
    public void deleteCourseByID(int courseID)
    {
        mDatabase.delete(CoursesTable.TABLE_COURSES, CoursesTable.COLUMN_ID + " = " + courseID, null);
        mDatabase.delete(AssessmentsTable.TABLE_ASSESSMENTS, AssessmentsTable.COLUMN_COURSE_ID + " = " + courseID, null);
        mDatabase.delete(NotesTable.TABLE_NOTES, NotesTable.COLUMN_COURSE_ID + " = " + courseID, null);
    }


    public long getCourseCount()
    {
        return DatabaseUtils.queryNumEntries(mDatabase, CoursesTable.TABLE_COURSES);
    }
    public List<Course> getAllCourses()
    {
        List<Course> courses = new ArrayList<>();
        Cursor cursor = mDatabase.query(CoursesTable.TABLE_COURSES, CoursesTable.ALL_COLUMNS, null,null,null,null,CoursesTable.COLUMN_START);

        while (cursor.moveToNext())
        {
            Course course = new Course();
            course.setCourseId(cursor.getInt(cursor.getColumnIndex(CoursesTable.COLUMN_ID)));
            course.setCourseTitle(cursor.getString(cursor.getColumnIndex(CoursesTable.COLUMN_NAME)));
            course.setCourseStart(cursor.getString(cursor.getColumnIndex(CoursesTable.COLUMN_START)));
            course.setCourseEnd(cursor.getString(cursor.getColumnIndex(CoursesTable.COLUMN_END)));
            course.setCourseStatusID(cursor.getInt(cursor.getColumnIndex(CoursesTable.COLUMN_COURSE_STATUS)));
            course.setMentorId(cursor.getInt(cursor.getColumnIndex(CoursesTable.COLUMN_COURSE_MENTOR_ID)));
            courses.add(course);
        }
        cursor.close();
        return courses;
    }
    public Course getCourseByID(int courseID)
    {
        Course course = new Course();
        Cursor cursor = mDatabase.query(CoursesTable.TABLE_COURSES, CoursesTable.ALL_COLUMNS, CoursesTable.COLUMN_ID + "= '" + courseID + "';", null, null, null, null);
        while (cursor.moveToNext())
        {
            course.setCourseId(cursor.getInt(cursor.getColumnIndex(CoursesTable.COLUMN_ID)));
            course.setCourseTitle(cursor.getString(cursor.getColumnIndex(CoursesTable.COLUMN_NAME)));
            course.setCourseStart(cursor.getString(cursor.getColumnIndex(CoursesTable.COLUMN_START)));
            course.setCourseEnd(cursor.getString(cursor.getColumnIndex(CoursesTable.COLUMN_END)));
            course.setCourseStatusID(cursor.getInt(cursor.getColumnIndex(CoursesTable.COLUMN_COURSE_STATUS)));
            course.setMentorId(cursor.getInt(cursor.getColumnIndex(CoursesTable.COLUMN_COURSE_MENTOR_ID)));
        }
        return course;
    }
    public List<Course> getTermsCourses(int termID)
    {
        List<Course> courses = new ArrayList<>();
        Cursor cursor = mDatabase.query(CoursesTable.TABLE_COURSES, CoursesTable.ALL_COLUMNS, CoursesTable.COLUMN_TERM_ID + " = '" + termID + "';",null,null,null,null);
        while (cursor.moveToNext())
        {
            Course course = new Course();
            course.setCourseId(cursor.getInt(cursor.getColumnIndex(CoursesTable.COLUMN_ID)));
            course.setCourseTitle(cursor.getString(cursor.getColumnIndex(CoursesTable.COLUMN_NAME)));
            course.setCourseStart(cursor.getString(cursor.getColumnIndex(CoursesTable.COLUMN_START)));
            course.setCourseEnd(cursor.getString(cursor.getColumnIndex(CoursesTable.COLUMN_END)));
            course.setCourseStatusID(cursor.getInt(cursor.getColumnIndex(CoursesTable.COLUMN_COURSE_STATUS)));
            course.setMentorId(cursor.getInt(cursor.getColumnIndex(CoursesTable.COLUMN_COURSE_MENTOR_ID)));
            courses.add(course);
        }
        cursor.close();
        return courses;
    }

    public void addCourse(Course course)
    {
        ContentValues values = course.toValues();
        mDatabase.insert(CoursesTable.TABLE_COURSES,null,values);
    }

    // Assessment Methods

    public Assessment createAssessment(Assessment assessment)
    {
        ContentValues values = assessment.toValues();
        mDatabase.insert(AssessmentsTable.TABLE_ASSESSMENTS,null,values);
        return assessment;
    }
    public void deleteAssessment(int assessmentID)
    {
        mDatabase.delete(AssessmentsTable.TABLE_ASSESSMENTS, AssessmentsTable.COLUMN_ID + " = " + assessmentID, null);
    }

    public void updateAssessment(Assessment assessment, int assessmentID)
    {

        System.out.println(assessmentID);
        ContentValues values = assessment.toValues();
        mDatabase.update(AssessmentsTable.TABLE_ASSESSMENTS, values, AssessmentsTable.COLUMN_ID + "=" + assessmentID , null);

    }
    public long getAssessmentCount()
    {
        return DatabaseUtils.queryNumEntries(mDatabase, AssessmentsTable.TABLE_ASSESSMENTS);
    }

    public Assessment getAssessmentByAssessmentID(int assessmentID)
    {
        Assessment assessment = new Assessment();
        Cursor cursor = mDatabase.query(AssessmentsTable.TABLE_ASSESSMENTS, AssessmentsTable.ALL_COLUMNS, AssessmentsTable.COLUMN_ID + " = '" + assessmentID + "';", null,null,null,null);
        while (cursor.moveToNext())
        {
            assessment.setAssId(cursor.getInt(cursor.getColumnIndex(AssessmentsTable.COLUMN_ID)));
            assessment.setAssTitle(cursor.getString(cursor.getColumnIndex(AssessmentsTable.COLUMN_NAME)));
            assessment.setAssEnd(cursor.getString(cursor.getColumnIndex(AssessmentsTable.COLUMN_END)));
            assessment.setAssCourseID(cursor.getInt(cursor.getColumnIndex(AssessmentsTable.COLUMN_COURSE_ID)));
            assessment.setAssTypeID(cursor.getInt(cursor.getColumnIndex(AssessmentsTable.COLUMN_TYPE_ID)));
        }
        return assessment;
    }

    public List<Assessment> getAssessmentsByCourseID(int courseID)
    {
        List<Assessment> assessments = new ArrayList<>();
        Cursor cursor = mDatabase.query(AssessmentsTable.TABLE_ASSESSMENTS, AssessmentsTable.ALL_COLUMNS, AssessmentsTable.COLUMN_COURSE_ID + " = '" + courseID + "';", null, null , null, null);
        while (cursor.moveToNext())
        {
            Assessment assessment = new Assessment();
            assessment.setAssId(cursor.getInt(cursor.getColumnIndex(AssessmentsTable.COLUMN_ID)));
            assessment.setAssTitle(cursor.getString(cursor.getColumnIndex(AssessmentsTable.COLUMN_NAME)));
            assessment.setAssEnd(cursor.getString(cursor.getColumnIndex(AssessmentsTable.COLUMN_END)));
            assessment.setAssCourseID(cursor.getInt(cursor.getColumnIndex(AssessmentsTable.COLUMN_COURSE_ID)));
            assessment.setAssTypeID(cursor.getInt(cursor.getColumnIndex(AssessmentsTable.COLUMN_TYPE_ID)));
            assessments.add(assessment);
        }
        cursor.close();
        return assessments;
    }
    public List<Assessment> getAllAssessments()
    {
        List<Assessment> assessments = new ArrayList<>();
        Cursor cursor = mDatabase.query(AssessmentsTable.TABLE_ASSESSMENTS, AssessmentsTable.ALL_COLUMNS,null,null,null,null, AssessmentsTable.COLUMN_END);

        while (cursor.moveToNext())
        {
            Assessment assessment = new Assessment();
            assessment.setAssId(cursor.getInt(cursor.getColumnIndex(AssessmentsTable.COLUMN_ID)));
            assessment.setAssTitle(cursor.getString(cursor.getColumnIndex(AssessmentsTable.COLUMN_NAME)));
            assessment.setAssEnd(cursor.getString(cursor.getColumnIndex(AssessmentsTable.COLUMN_END)));
            assessment.setAssTypeID(cursor.getInt(cursor.getColumnIndex(AssessmentsTable.COLUMN_TYPE_ID)));
            assessments.add(assessment);
        }
        cursor.close();
        return assessments;
    }
    public void addAssessment(Assessment assessment)
    {
        ContentValues values = assessment.toValues();
        mDatabase.insert(AssessmentsTable.TABLE_ASSESSMENTS,null,values);
    }

    // Mentor Methods

    public Mentor createMentor(Mentor mentor)
    {
        ContentValues values = mentor.toValues();
        mDatabase.insert(MentorTable.TABLE_MENTORS,null,values);
        return mentor;
    }
    public long getMentorCount()
    {
        return DatabaseUtils.queryNumEntries(mDatabase,MentorTable.TABLE_MENTORS);
    }
    public Mentor getSelectedCourseMentor(int mentorID)
    {
        Mentor mentor = new Mentor();
        Cursor cursor = mDatabase.query(MentorTable.TABLE_MENTORS, MentorTable.ALL_COLUMNS, MentorTable.COLUMN_ID + "= '" + mentorID + "';", null,null,null,null);
        while (cursor.moveToNext())
        {
            mentor.setMentorId(cursor.getInt(cursor.getColumnIndex(MentorTable.COLUMN_ID)));
            mentor.setFirstName(cursor.getString(cursor.getColumnIndex(MentorTable.COLUMN_FNAME)));
            mentor.setLastName(cursor.getString(cursor.getColumnIndex(MentorTable.COLUMN_LNAME)));
            mentor.setMentorPhone(cursor.getString(cursor.getColumnIndex(MentorTable.COLUMN_PHONE)));
            mentor.setMentorEmail(cursor.getString(cursor.getColumnIndex(MentorTable.COLUMN_EMAIL)));
        }
        cursor.close();
        return mentor;
    }

    public List<Mentor> getAllMentors()
    {
        List<Mentor> mentors = new ArrayList<>();
        Cursor cursor = mDatabase.query(MentorTable.TABLE_MENTORS, MentorTable.ALL_COLUMNS,null,null,null,null, null);
        while (cursor.moveToNext())
        {
            Mentor mentor = new Mentor();
            mentor.setMentorId(cursor.getInt(cursor.getColumnIndex(MentorTable.COLUMN_ID)));
            mentor.setFirstName(cursor.getString(cursor.getColumnIndex(MentorTable.COLUMN_FNAME)));
            mentor.setLastName(cursor.getString(cursor.getColumnIndex(MentorTable.COLUMN_LNAME)));
            mentor.setMentorPhone(cursor.getString(cursor.getColumnIndex(MentorTable.COLUMN_PHONE)));
            mentor.setMentorEmail(cursor.getString(cursor.getColumnIndex(MentorTable.COLUMN_EMAIL)));
            mentors.add(mentor);
        }
        cursor.close();
        return mentors;
    }
    public void addMentor(Mentor mentor)
    {
        ContentValues values = mentor.toValues();
        mDatabase.insert(MentorTable.TABLE_MENTORS,null,values);
    }
    public int getMentorID(String lastName, String firstName)
    {
        int mentorID = 0;
        Cursor cursor = mDatabase.query(MentorTable.TABLE_MENTORS, MentorTable.ALL_COLUMNS, MentorTable.COLUMN_FNAME + " = '" + firstName +"' AND " + MentorTable.COLUMN_LNAME + " = '" + lastName + "';", null , null, null, null);

        while(cursor.moveToNext())
        {
           mentorID=  cursor.getInt(cursor.getColumnIndex(MentorTable.COLUMN_ID));
        }
        cursor.close();
        return mentorID;
    }

    // Methods for Status
    public CourseStatus createCourseStatus(CourseStatus status)
    {
        ContentValues values = status.toValues();
        mDatabase.insert(StatusTable.TABLE_STATUS,null,values);
        return status;
    }
    public long getStatusCount()
    {
        return DatabaseUtils.queryNumEntries(mDatabase, StatusTable.TABLE_STATUS);
    }
    public List<CourseStatus> getAllCourseStatus()
    {
        List<CourseStatus> coursesStatus = new ArrayList<>();
        Cursor cursor = mDatabase.query(StatusTable.TABLE_STATUS, StatusTable.ALL_COLUMNS,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            CourseStatus status = new CourseStatus();
            status.setCourseStatusID(cursor.getInt(cursor.getColumnIndex(StatusTable.COLUMN_ID)));
            status.setCourseStatus(cursor.getString(cursor.getColumnIndex(StatusTable.COLUMN_NAME)));
            coursesStatus.add(status);
        }
        cursor.close();
        return coursesStatus;
    }
    public void addCourseStatus(CourseStatus cStatus)
    {
        ContentValues values = cStatus.toValues();
        mDatabase.insert(StatusTable.TABLE_STATUS, null, values);
    }
    public String getCourseStatusByStatusID(int statusID)
    {
        String statusTitle = null;
        Cursor cursor = mDatabase.query(StatusTable.TABLE_STATUS, StatusTable.ALL_COLUMNS, StatusTable.COLUMN_ID + " = '" + statusID + "'", null,null,null,null);
        while(cursor.moveToNext())
        {
            statusTitle = cursor.getString(cursor.getColumnIndex(StatusTable.COLUMN_NAME));
        }
        return statusTitle;
    }

    public int getCourseStatusId(String statusTitle)
    {
        int statusID = 0;
        Cursor cursor = mDatabase.query(StatusTable.TABLE_STATUS, StatusTable.ALL_COLUMNS, StatusTable.COLUMN_NAME + " = '" + statusTitle +"'",null,null,null,null);

        while (cursor.moveToNext())
        {
            statusID = cursor.getInt(cursor.getColumnIndex(StatusTable.COLUMN_ID));
        }
        cursor.close();
        return statusID;
    }

    // Methods for notes
    public CourseNote createCourseNote(CourseNote courseNote)
    {
        ContentValues values = courseNote.toValues();
        mDatabase.insert(NotesTable.TABLE_NOTES, null, values);
        return courseNote;
    }
    public void addCourseNote(CourseNote courseNote)
    {
        ContentValues values = courseNote.toValues();
        mDatabase.insert(NotesTable.TABLE_NOTES, null,values);
    }
    public void deleteCourseNote(int courseNoteId)
    {
        mDatabase.delete(NotesTable.TABLE_NOTES, NotesTable.COLUMN_ID + " = " + courseNoteId, null);
    }
    public void updateCourseNote(CourseNote courseNote, int noteID)
    {
        ContentValues values = courseNote.toValues();
        mDatabase.update(NotesTable.TABLE_NOTES, values, NotesTable.COLUMN_ID + "=" + noteID , null);

    }
    public CourseNote getNoteByNoteID(int noteID)
    {
        CourseNote courseNote = new CourseNote();
        Cursor cursor = mDatabase.query(NotesTable.TABLE_NOTES, NotesTable.ALL_COLUMNS, NotesTable.COLUMN_ID + " = '" + noteID + "';",null,null,null,null);
        while (cursor.moveToNext())
        {
            courseNote.setNoteId(cursor.getInt(cursor.getColumnIndex(NotesTable.COLUMN_ID)));
            courseNote.setTitle(cursor.getString(cursor.getColumnIndex(NotesTable.COLUMN_TITLE)));
            courseNote.setNote(cursor.getString(cursor.getColumnIndex(NotesTable.COLUMN_NOTE)));
            courseNote.setCourseID(cursor.getInt(cursor.getColumnIndex(NotesTable.COLUMN_COURSE_ID)));
        }
        cursor.close();
        return courseNote;
    }
    public List<CourseNote> getAllCourseNotes()
    {
        List<CourseNote> allCourseNotes = new ArrayList<>();
        Cursor cursor = mDatabase.query(NotesTable.TABLE_NOTES, NotesTable.ALL_COLUMNS, null,null,null,null,null);
        while(cursor.moveToNext())
        {
            CourseNote courseNote = new CourseNote();
            courseNote.setNoteId(cursor.getInt(cursor.getColumnIndex(NotesTable.COLUMN_ID)));
            courseNote.setTitle(cursor.getString(cursor.getColumnIndex(NotesTable.COLUMN_TITLE)));
            courseNote.setNote(cursor.getString(cursor.getColumnIndex(NotesTable.COLUMN_NOTE)));
            courseNote.setCourseID(cursor.getInt(cursor.getColumnIndex(NotesTable.COLUMN_COURSE_ID)));
            allCourseNotes.add(courseNote);
        }
        cursor.close();
        return allCourseNotes;
    }


    public List<CourseNote> getCourseNoteByCourseID(int courseID)
    {
        List<CourseNote> notes = new ArrayList<>();
        Cursor cursor = mDatabase.query(NotesTable.TABLE_NOTES, NotesTable.ALL_COLUMNS, NotesTable.COLUMN_COURSE_ID + " = '" + courseID + "';", null,null,null,null);
        while (cursor.moveToNext())
        {
            CourseNote courseNote = new CourseNote();
            courseNote.setNoteId(cursor.getInt(cursor.getColumnIndex(NotesTable.COLUMN_ID)));
            courseNote.setTitle(cursor.getString(cursor.getColumnIndex(NotesTable.COLUMN_TITLE)));
            courseNote.setNote(cursor.getString(cursor.getColumnIndex(NotesTable.COLUMN_NOTE)));
            courseNote.setCourseID(cursor.getInt(cursor.getColumnIndex(NotesTable.COLUMN_COURSE_ID)));
            notes.add(courseNote);
        }
        cursor.close();
        return notes;
    }
    // Methods for assessmentType
    public AssessmentType createAssessmentType(AssessmentType assessmentType)
    {
        ContentValues values = assessmentType.toValues();
        mDatabase.insert(TypesTable.TABLE_TYPES, null,values);
        return assessmentType;
    }
    public void addAssessmentType(AssessmentType assessmentType)
    {
        ContentValues values = assessmentType.toValues();
        mDatabase.insert(TypesTable.TABLE_TYPES, null,values);
    }

    public String getAssessmentTypeByID(int assType)
    {
        String assessmentType;
        AssessmentType assessmentType1 = new AssessmentType();
        Cursor cursor = mDatabase.query(TypesTable.TABLE_TYPES, TypesTable.ALL_COLUMNS, TypesTable.COLUMN_ID + " = '" + assType + "';", null,null,null,null);
        while (cursor.moveToNext())
        {
            assessmentType1.setAssessmentTypeID(cursor.getInt(cursor.getColumnIndex(TypesTable.COLUMN_ID)));
            assessmentType1.setAssessmentType(cursor.getString(cursor.getColumnIndex(TypesTable.COLUMN_NAME)));
        }
        assessmentType = assessmentType1.getAssessmentType();
        return assessmentType;
    }
    public int getAssessmentTypeID(String assTitle)
    {
        int assessID = 0;
        Cursor cursor = mDatabase.query(TypesTable.TABLE_TYPES, TypesTable.ALL_COLUMNS, TypesTable.COLUMN_NAME + " = '" + assTitle + "';",null,null,null,null);
        while (cursor.moveToNext())
        {
            assessID = cursor.getInt(cursor.getColumnIndex(TypesTable.COLUMN_ID));
        }
        return assessID;
    }

    public List<AssessmentType> getAllAssessmentTypes()
    {
        List<AssessmentType> assessmentTypes = new ArrayList<>();
        Cursor cursor = mDatabase.query(TypesTable.TABLE_TYPES, TypesTable.ALL_COLUMNS, null,null,null,null,null);
        while (cursor.moveToNext())
        {
            AssessmentType assessmentType = new AssessmentType();
            assessmentType.setAssessmentTypeID(cursor.getInt(cursor.getColumnIndex(TypesTable.COLUMN_ID)));
            assessmentType.setAssessmentType(cursor.getString(cursor.getColumnIndex(TypesTable.COLUMN_NAME)));
            assessmentTypes.add(assessmentType);
        }
        cursor.close();
        return assessmentTypes;
    }
    public long getAssessmentTypesCount()
    {
        return DatabaseUtils.queryNumEntries(mDatabase, TypesTable.TABLE_TYPES);
    }

    // Methods for alerts
    public void addCourseAlert(Alert alert)
    {
        ContentValues values = alert.toValues();
        mDatabase.insert(CourseAlertsTable.TABLE_COURSEALERTS, null, values);
    }
    public void updateCourseAlert(Alert alert, int alertID)
    {
        ContentValues values = alert.toValues();
        mDatabase.update(CourseAlertsTable.TABLE_COURSEALERTS, values, CourseAlertsTable.COLUMN_COURSE_ID + "=" + alertID + "", null);
    }
    public Alert getCourseAlertByCourseID(int courseID)
    {
        Alert alert = new Alert();
        Cursor cursor = mDatabase.query(CourseAlertsTable.TABLE_COURSEALERTS, CourseAlertsTable.ALL_COLUMNS, CourseAlertsTable.COLUMN_COURSE_ID + " = '" + courseID + "'", null,null,null,null);
        while (cursor.moveToNext())
        {
            alert.setTitle(cursor.getString(cursor.getColumnIndex(CourseAlertsTable.COLUMN_NAME)));
            alert.setDate(cursor.getString(cursor.getColumnIndex(CourseAlertsTable.COLUMN_DATE)));
            alert.setHour(cursor.getInt(cursor.getColumnIndex(CourseAlertsTable.COLUMN_HOUR)));
            alert.setActive(cursor.getInt(cursor.getColumnIndex(CourseAlertsTable.COLUMN_ACTIVE)));
            alert.setCourseID(cursor.getInt(cursor.getColumnIndex(CourseAlertsTable.COLUMN_COURSE_ID)));
            alert.setTypeId(cursor.getInt(cursor.getColumnIndex(CourseAlertsTable.COLUMN_TYPE_ID)));
        }
        cursor.close();
        return alert;
    }
    public void updateAssessmentAlert(Alert alert, int assId)
    {
        ContentValues values = alert.toValues();
        mDatabase.update(CourseAlertsTable.TABLE_COURSEALERTS, values, CourseAlertsTable.COLUMN_TYPE_ID + " = " + assId,null);
    }
    public Alert getAssessmentAlertByAssID(int assID)
    {
        Alert alert = new Alert();
        Cursor cursor = mDatabase.query(CourseAlertsTable.TABLE_COURSEALERTS, CourseAlertsTable.ALL_COLUMNS, CourseAlertsTable.COLUMN_TYPE_ID + " = '" + assID + "'", null,null,null,null);
        while (cursor.moveToNext())
        {
            alert.setTitle(cursor.getString(cursor.getColumnIndex(CourseAlertsTable.COLUMN_NAME)));
            alert.setDate(cursor.getString(cursor.getColumnIndex(CourseAlertsTable.COLUMN_DATE)));
            alert.setHour(cursor.getInt(cursor.getColumnIndex(CourseAlertsTable.COLUMN_HOUR)));
            alert.setActive(cursor.getInt(cursor.getColumnIndex(CourseAlertsTable.COLUMN_ACTIVE)));
            alert.setCourseID(cursor.getInt(cursor.getColumnIndex(CourseAlertsTable.COLUMN_COURSE_ID)));
            alert.setTypeId(cursor.getInt(cursor.getColumnIndex(CourseAlertsTable.COLUMN_TYPE_ID)));
        }
        cursor.close();
        return alert;
    }

    // Conditional queries
    public int getCurrentTermID(String currentDate)
    {

        List<Term> allTerms = getAllTerms();
        try {
            curDate = sdf.parse(currentDate);
        }
        catch (ParseException ex)
        {
            System.out.println(ex.getLocalizedMessage());
        }
        int currentTerm = 0;
        List<Date> termStart = new ArrayList<>();
        List<Date> termEnd = new ArrayList<>();
        for (int i = 0; i < allTerms.size(); i++)
        {
            try {
                Date s = sdf.parse(allTerms.get(i).getStartDate());
                termStart.add(s);
                Date f = sdf.parse(allTerms.get(i).getEndDate());
                termEnd.add(f);
                if (curDate.after(s) == true && curDate.before(f) == true)
                {
                    currentTerm = allTerms.get(i).getTermID();
                }
            }
            catch (ParseException ex)
            {
                System.out.println(ex.getLocalizedMessage());
            }

        }
        return currentTerm;
    }





}
