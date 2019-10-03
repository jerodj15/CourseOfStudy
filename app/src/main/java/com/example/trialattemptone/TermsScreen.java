package com.example.trialattemptone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trialattemptone.Creators.Course;
import com.example.trialattemptone.Creators.Term;
import com.example.trialattemptone.database.DataSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TermsScreen extends AppCompatActivity {

    Button selectDateButton;
    public static EditText startDate;
    public static EditText endDate;
    static GridView termGridView;
    public static int selectedTermID;
    public static int longSelectedTermID;
    List<String> termNamesList = new ArrayList<>();
    List<Integer> termIDList = new ArrayList<>();
    CalendarView calView;
    DataSource mDataSource;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    EditText startDateTV;
    EditText endDateTV;
    List<Term> termListing = new ArrayList<>();
    List<Course> courseList;
    public static String termTitle = "";
    public static String startingDate = "";
    public static String endingDate = "";
    public static int startOrEnd = 0; // Start = 0 End = 1
    public static int selectedForEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_screen);
        startDate = findViewById(R.id.startET);
        endDate = findViewById(R.id.endET);
        mDataSource = new DataSource(this);
        mDataSource.open();
        courseList = mDataSource.getAllCourses();
        List<Term> termListFromDB = mDataSource.getAllTerms();
        for (Term terms : termListFromDB)
        {
            termNamesList.add(terms.getTermName() + "\nStart Date : " + terms.getStartDate() + "\nEnding Date : " + terms.getEndDate());
            termIDList.add(terms.getTermID());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, termNamesList
        );
        termGridView = findViewById(R.id.termsGV);
        termGridView.setAdapter(adapter);
        getSupportActionBar().setTitle("Terms");
        termGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedTerm = adapter.getItem(position);
                selectedTermID = termIDList.get(position);
                System.out.println(selectedTermID);
                System.out.println();
                System.out.println(selectedTerm);
                Intent intent = new Intent(view.getContext(), CoursesScreen.class);
                startActivity(intent);
            }
        });
        registerForContextMenu(termGridView);
        /*
        termGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longSelectedTermID = termIDList.get(position);
                System.out.println("Long term selected ID = " + longSelectedTermID);
                return true;
            }
        });
        */
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.deleteitem_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        longSelectedTermID = termIDList.get(info.position);
        ArrayList<Integer> cycleTermsID = new ArrayList<>();
        switch (item.getItemId()) {
            case R.id.remove_item:
                deleteTerm(info.id);
                this.recreate();
             //   Intent intent = new Intent(this, TermsScreen.class);
             //   startActivity(intent);
                return true;


        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.term_item:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addTermPressed(View view)
    {
        setContentView(R.layout.addterm_layout);
        getSupportActionBar().setTitle("Add New Term");

    }

    // Create methods for termCreator screen
    public void startDatePickPressed(View view)
    {
        EditText titleEditT = findViewById(R.id.termTitleET);
        if (titleEditT.getText().toString().isEmpty() == false)
        {
            termTitle = titleEditT.getText().toString();
        }
        startOrEnd =0;
        setContentView(R.layout.activity_date_picker);
        getSupportActionBar().setTitle("Pick Starting Date");
        calView = findViewById(R.id.calendarView);
        long date = calView.getDate();
        // Setup the starting date
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.setTime(new Date(calView.getDate()));
        startingDate = sdf.format(new Date(calendar.getTime().getTime()));
        selectDateButton = findViewById(R.id.selectButton);
        calView = findViewById(R.id.calendarView);
        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String selMonth = String.valueOf(month + 1);
                String selYear = String.valueOf(year);
                String selDay = String.valueOf(dayOfMonth);
                String newMonth = selMonth;
                String newDay = selDay;
                String newYear = selYear;
                if (month < 10)
                {
                    newMonth = "0" + selMonth;

                }
                if (dayOfMonth < 10)
                {
                    newDay = "0" + selDay;
                }

                startingDate = newMonth + "/" + newDay + "/" + newYear;
                System.out.println(startingDate);



            }
        });

    }
    public void endDatePickPressed(View view)
    {
        EditText titleEditT = findViewById(R.id.termTitleET);
        if (titleEditT.getText().toString().isEmpty() == false)
        {
            termTitle = titleEditT.getText().toString();
        }
        startOrEnd = 1;
        setContentView(R.layout.activity_date_picker);
        getSupportActionBar().setTitle("Pick a Ending Date");
        selectDateButton = findViewById(R.id.selectButton);
        calView = findViewById(R.id.calendarView);
        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String selMonth = String.valueOf(month + 1);
                String selYear = String.valueOf(year);
                String selDay = String.valueOf(dayOfMonth);
                String newMonth = selMonth;
                String newDay = selDay;
                String newYear = selYear;
                if (month < 10)
                {
                    newMonth = "0" + selMonth;

                }
                if (dayOfMonth < 10)
                {
                    newDay = "0" + selDay;
                }

                endingDate = newMonth + "/" + newDay + "/" + newYear;
                System.out.println(endingDate);

            }
        });
    }
    public void cancelButtonPressed(View view)
    {
        finish();
        Intent intent = new Intent(this, TermsScreen.class);
        startActivity(intent);

    }
    public void saveButtonPressed(View view)
    {

            finish();
            EditText titleEditT = findViewById(R.id.termTitleET);
            String termTitle = titleEditT.getText().toString();
            Term term = new Term(termTitle, startingDate, endingDate);
            mDataSource.addTerm(term);
            Toast.makeText(this, "Term Added", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, TermsScreen.class);
            startActivity(intent);


    }
    // Create methods for date selection layout
    public void selectButtonPressed(View view)
    {
        if (startOrEnd == 0)
        {
            setContentView(R.layout.addterm_layout);
            EditText startEdit = findViewById(R.id.startET);
            startEdit.setText(startingDate);
            EditText tTitle = findViewById(R.id.termTitleET);
            tTitle.setText(termTitle);
            getSupportActionBar().setTitle("Add New Term");
        }
        else if (startOrEnd == 1)
        {
            setContentView(R.layout.addterm_layout);
            EditText startEdit = findViewById(R.id.endET);
            startEdit.setText(endingDate);
            EditText tTitle = findViewById(R.id.termTitleET);
            tTitle.setText(termTitle);
            getSupportActionBar().setTitle("Add New Term");
        }
        if (startingDate.isEmpty() == false && endingDate.isEmpty() == false)
        {
            setContentView(R.layout.addterm_layout);
            EditText startEditText = findViewById(R.id.startET);
            EditText endEditText = findViewById(R.id.endET);
            startEditText.setText(startingDate);
            endEditText.setText(endingDate);
            EditText tTitle = findViewById(R.id.termTitleET);
            tTitle.setText(termTitle);
            getSupportActionBar().setTitle("Add New Term");
        }


    }
    public void cancelDatePick(View view)
    {
        setContentView(R.layout.addterm_layout);
        getSupportActionBar().setTitle("Add New Term");
    }

    public void deleteTerm(long selectedItem)
    {

        int selectedForDelete = termIDList.get((int) selectedItem);
        List<Course> allOfTheCourses = mDataSource.getTermsCourses(selectedForDelete);
        System.out.println("Selected for delete " + selectedForDelete);

        if (allOfTheCourses.isEmpty() == true)
        {
            mDataSource.deleteTermByID(selectedForDelete);
        }
        else
        {
            Toast.makeText(this, "Cannot delete term because it has courses", Toast.LENGTH_LONG).show();
        }
    }


}
