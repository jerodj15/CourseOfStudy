package com.example.trialattemptone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.trialattemptone.Creators.AssessmentType;
import com.example.trialattemptone.database.DataSource;

public class AddTypeScreen extends AppCompatActivity {

    public DataSource mDataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type_screen);
        mDataSource = new DataSource(this);
        mDataSource.open();
    }

    public void cancelTypeButtonPressed(View view)
    {
        this.finish();
    }
    public void saveTypeButtonPressed(View view)
    {
        EditText typeTitleText;
        typeTitleText = findViewById(R.id.typeTitleET);
        String typeTitleString = typeTitleText.getText().toString();
        AssessmentType assessmentType = new AssessmentType(typeTitleString);
        mDataSource.addAssessmentType(assessmentType);
        this.finish();

    }

}
