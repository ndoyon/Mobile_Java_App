package com.example.ndoyon_c196.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import com.example.ndoyon_c196.R;


public class MentorList extends AppCompatActivity {

        public static final String TAG = "MentorList";

        public static final String extraCourseMentor_MentorName = "extraCourseMentor_MentorName";
        public static final String extraCourseMentor_MentorPhone = "extraCourseMentor_MentorPhone";
        public static final String extraCourseMentor_MentorEmail = "extraCourseMentor_MentorEmail";
        public static final String extraCourseMentor_CourseID = "extraCourseMentor_CourseID";
        public static final String extraCourseMentor_Name = "extraCourseMentor_Name";
        public static final String extraCourseMentor_Status = "extraCourseMentor_Status";
        public static final String extraCourseMentor_Start = "extraCourseMentor_Start";
        public static final String extraCourseMentor_End = "extraCourseMentor_End";

        private EditText editMentorName;
        private EditText editMentorPhone;
        private EditText editMentorEmail;


        private int courseID;
        private String courseName;
        private String courseStatus;
        private String courseStartDate;
        private String courseEndDate;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setTitle("Edit Mentor");
            setContentView(R.layout.activity_mentor_list);

            editMentorName = findViewById(R.id.mentorName);
            editMentorPhone = findViewById(R.id.mentorPhone);
            editMentorEmail = findViewById(R.id.mentorEmail);

            //prefill data if any
            Intent intent = getIntent();
            editMentorName.setText(intent.getStringExtra(extraCourseMentor_MentorName));
            editMentorPhone.setText(intent.getStringExtra(extraCourseMentor_MentorPhone));
            editMentorEmail.setText(intent.getStringExtra(extraCourseMentor_MentorEmail));

            //save data from previous screen
            courseID = intent.getIntExtra(extraCourseMentor_CourseID, -1);
            courseName = intent.getStringExtra(extraCourseMentor_Name);
            courseStatus = intent.getStringExtra(extraCourseMentor_Status);
            courseStartDate = intent.getStringExtra(extraCourseMentor_Start);
            courseEndDate = intent.getStringExtra(extraCourseMentor_End);

            final Button saveButton = findViewById(R.id.btnSaveMentor);
            saveButton.setOnClickListener(view -> saveMentor());

        }

        private void saveMentor() {
            Log.d(TAG, "saveMentor: clicked.");
            Intent data = new Intent();
            data.putExtra(extraCourseMentor_MentorName, editMentorName.getText().toString());
            data.putExtra(extraCourseMentor_MentorPhone, editMentorPhone.getText().toString());
            data.putExtra(extraCourseMentor_MentorEmail, editMentorEmail.getText().toString());
            data.putExtra(extraCourseMentor_CourseID, courseID);
            data.putExtra(extraCourseMentor_Name, courseName);
            data.putExtra(extraCourseMentor_Status, courseStatus);
            data.putExtra(extraCourseMentor_Start, courseStartDate);
            data.putExtra(extraCourseMentor_End, courseEndDate);
            setResult(RESULT_OK, data);
            finish();


        }
    }
