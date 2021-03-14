package com.example.ndoyon_c196.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.ndoyon_c196.R;

public class CourseNotes extends AppCompatActivity {
    public static final String TAG = "CourseNotes";


    public static final String extraCourseNotes_CourseID = "extraCourseNotes_CourseID";
    public static final String extraCourseNotes_CourseNotes = "extraCourseNotes_CourseNotes";
    public static final String extraCourseNotes_Name = "extraCourseNotes_Name";
    public static final String extraCourseNotes_Status = "extraCourseNotes_Status";
    public static final String extraCourseNotes_Start = "extraCourseNotes_Start";
    public static final String extraCourseNotes_End = "extraCourseNotes_End";




    private EditText editNotes;
    private EditText textEmailAddress;


    private int courseID;
    private String courseName;
    private String courseStatus;
    private String courseStartDate;
    private String courseEndDate;
    private String courseNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Course Notes");
        setContentView(R.layout.activity_course_notes);

        editNotes = findViewById(R.id.editNotesText);
        textEmailAddress = findViewById(R.id.emailAddressText);


        Intent intent = getIntent();
        if (intent.hasExtra(extraCourseNotes_CourseID)) {
            editNotes.setText(intent.getStringExtra(extraCourseNotes_CourseNotes));
        }


        courseID = intent.getIntExtra(extraCourseNotes_CourseID, -1);
        courseName = intent.getStringExtra(extraCourseNotes_Name);
        courseStatus = intent.getStringExtra(extraCourseNotes_Status);
        courseStartDate = intent.getStringExtra(extraCourseNotes_Start);
        courseEndDate = intent.getStringExtra(extraCourseNotes_End);
        courseNotes = intent.getStringExtra(extraCourseNotes_CourseNotes);


        final Button emailButton = findViewById(R.id.sendEmailBtn);
        emailButton.setOnClickListener(view -> sendEmail());
        final Button saveButton = findViewById(R.id.saveNotesBtn);
        saveButton.setOnClickListener(view -> saveNotes());


    }


    private void saveNotes() {
        Log.d(TAG, "saveNotes: clicked.");
        Intent data = new Intent();
        data.putExtra(extraCourseNotes_CourseNotes, editNotes.getText().toString());
        data.putExtra(extraCourseNotes_CourseID, courseID);
        data.putExtra(extraCourseNotes_Name, courseName);
        data.putExtra(extraCourseNotes_Status, courseStatus);
        data.putExtra(extraCourseNotes_Start, courseStartDate);
        data.putExtra(extraCourseNotes_End, courseEndDate);
        setResult(RESULT_OK, data);
        finish();
    }


    private void sendEmail() {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , textEmailAddress.getText().toString());
        i.putExtra(Intent.EXTRA_SUBJECT, "Here are my course notes for " + courseName);
        i.putExtra(Intent.EXTRA_TEXT   , courseNotes);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CourseNotes.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}