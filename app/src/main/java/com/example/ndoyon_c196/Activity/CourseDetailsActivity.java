package com.example.ndoyon_c196.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ndoyon_c196.Adapters.AssessmentAdapter;
import com.example.ndoyon_c196.Entity.assessment;
import com.example.ndoyon_c196.Entity.course;
import com.example.ndoyon_c196.R;
import com.example.ndoyon_c196.Utility.Converters;
import com.example.ndoyon_c196.ViewModel.assessmentView;
import com.example.ndoyon_c196.ViewModel.courseView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CourseDetailsActivity extends AppCompatActivity {
    public static final String TAG = "CourseDetailsActivity";
    private static final int EDIT_COURSE_REQUEST = 202;
    private static final int ADD_ASSESSMENT_REQUEST = 301;
    private static final int EDIT_ASSESSMENT_REQUEST = 302;
    private static final int ADD_NOTES_REQUEST = 401;
    private static final int ADD_MENTOR_REQUEST = 501;

    public static final String extraCourseDetailsTerms_TermFKID = "extraCourseDetailsTerms_TermFKID";
    public static final String extraCourseDetails_CourseID = "extraCourseDetails_CourseID";
    public static final String extraCourseDetails_Name = "extraCourseDetails_Name";
    public static final String extraCourseDetails_Status = "extraCourseDetails_Status";
    public static final String extraCourseDetails_Start = "extraCourseDetails_Start";
    public static final String extraCourseDetails_End = "extraCourseDetails_End";
    public static final String extraCourseDetails_Notes = "extraCourseDetails_Notes";
    public static final String extraCourseDetails_MentorName = "extraCourseDetails_MentorName";
    public static final String extraCourseDetails_MentorPhone = "extraCourseDetails_MentorPhone";
    public static final String extraCourseDetails_MentorEmail = "extraCourseDetails_MentorEmail";

    private TextView CourseName;
    private TextView CourseStatus;
    private TextView CourseStartDate;
    private TextView CourseEndDate;
    private FloatingActionButton fabEditCourse;
    private FloatingActionButton fabAddAssessment;
    private FloatingActionButton fabCourseNotes;
    private FloatingActionButton fabMentor;

    private int courseTermFKID;
    private int courseID;
    private String courseName;
    private String courseStatus;
    private String courseStartDate;
    private String courseEndDate;
    private String courseNotes;
    private String mentorName;
    private String mentorPhone;
    private String mentorEmail;

    private courseView courseViewModel;
    private assessmentView assessmentViewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Course Details");
        setContentView(R.layout.activity_course_details);

        //create the view to talk to DB
        courseViewModel = new ViewModelProvider(this).get(courseView.class);
        assessmentViewModel = new ViewModelProvider(this).get(assessmentView.class);

        //set up the display items
        CourseName = findViewById(R.id.courseDetails_Name);
        CourseStatus = findViewById(R.id.courseDetailsStatus);
        CourseStartDate = findViewById(R.id.courseDetails_Start);
        CourseEndDate = findViewById(R.id.courseDetails_End);
        fabEditCourse = findViewById(R.id.fabEditCourse);
        fabAddAssessment = findViewById(R.id.fabAddCourse);
        fabCourseNotes = findViewById(R.id.fabNotes);
        fabMentor = findViewById(R.id.fabMentor);

        //grab data from previous activity
        Bundle extras = getIntent().getExtras();
        courseTermFKID = extras.getInt(extraCourseDetailsTerms_TermFKID);
        int parentId = extras.getInt("TEST_INT_PASSING", -1);
        Log.d(TAG, "onCreate: parentId Passed = " + parentId);
        courseID = extras.getInt(extraCourseDetails_CourseID, -1);
        Log.d(TAG, "onCreate: courseID = " + courseID);
        courseName = extras.getString(extraCourseDetails_Name);
        courseStatus = extras.getString(extraCourseDetails_Status);
        courseStartDate = "";
        courseEndDate = "";
        SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat fromFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy"); //Sat May 01 00:00:00 CDT 2021
        try {
            courseStartDate = toFormat.format(fromFormat.parse(extras.getString(extraCourseDetails_Start)));
            courseEndDate = toFormat.format(fromFormat.parse(extras.getString(extraCourseDetails_End)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        courseNotes = extras.getString(extraCourseDetails_Notes);
        mentorName = extras.getString(extraCourseDetails_MentorName);
        mentorPhone = extras.getString(extraCourseDetails_MentorPhone);
        mentorEmail = extras.getString(extraCourseDetails_MentorEmail);

        //Update the text
        CourseName.setText(courseName);
        CourseStatus.setText(courseStatus);
        CourseStartDate.setText(courseStartDate);
        CourseEndDate.setText(courseEndDate);


        RecyclerView recyclerView = findViewById(R.id.assessmentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        AssessmentAdapter adapter = new AssessmentAdapter();
        recyclerView.setAdapter(adapter);


        assessmentViewModel.getCourseAssessments(courseID).observe(this, new Observer<List<assessment>>() {
            @Override
            public void onChanged(List<assessment> assessments) {
                adapter.setAssessments(assessments);
            }
        });

        //SWIPE TO DELETE
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                assessmentViewModel.delete(adapter.getAssessmentAt(viewHolder.getAdapterPosition()));
                Toast.makeText(CourseDetailsActivity.this, "Assessment Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        //on click set up
        adapter.setOnItemClickListener(new AssessmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(assessment assessment) {
                int id = assessment.getAssessment_id();
                String name = assessment.getAssessment_name();
                String type = assessment.getAssessment_type();
                String info = assessment.getAssessment_info();
                String duedate = null;
                try { duedate = Converters.DateToString(assessment.getAssessment_due_date()); }
                catch (ParseException e) { e.printStackTrace(); }

                Intent intent = new Intent(CourseDetailsActivity.this, EditAssessment.class);
                intent.putExtra(EditAssessment.extraAssessmentEdit_Course_FKID, courseID);
                intent.putExtra(EditAssessment.extraAssessmentEdit_ID, id);
                intent.putExtra(EditAssessment.extraAssessmentEdit_Name, name);
                intent.putExtra(EditAssessment.extraAssessmentEdit_DueDate, duedate);
                intent.putExtra(EditAssessment.extraAssessmentEdit_Type, type);
                intent.putExtra(EditAssessment.extraAssessmentEdit_Info, info);
                startActivityForResult(intent, EDIT_ASSESSMENT_REQUEST);
            }
        });

       //FAB set up
        fabAddAssessment.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: fabAddAssessment Clicked");
            Intent intent = new Intent(CourseDetailsActivity.this, EditAssessment.class);
            startActivityForResult(intent, ADD_ASSESSMENT_REQUEST);
        });
        fabEditCourse.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: fabEditCourse Clicked");
            Intent intentEditCourse = new Intent(CourseDetailsActivity.this, EditCourse.class);
            intentEditCourse.putExtra(EditCourse.extraCourseEdit_TermFKID, courseTermFKID);
            intentEditCourse.putExtra(EditCourse.extraCourseEdit_CourseID, courseID);
            intentEditCourse.putExtra(EditCourse.extraCourseEdit_Name, CourseName.getText());
            intentEditCourse.putExtra(EditCourse.extraCourseEdit_Status, CourseStatus.getText());
            intentEditCourse.putExtra(EditCourse.extraCourseEdit_Start, CourseStartDate.getText());
            intentEditCourse.putExtra(EditCourse.extraCourseEdit_End, CourseEndDate.getText());
            startActivityForResult(intentEditCourse, EDIT_COURSE_REQUEST);
        });
        fabCourseNotes.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: fabCourseNotes Clicked");
            Intent intentCourseNotes = new Intent(CourseDetailsActivity.this, CourseNotes.class);
            intentCourseNotes.putExtra(CourseNotes.extraCourseNotes_CourseID, courseID);
            intentCourseNotes.putExtra(CourseNotes.extraCourseNotes_CourseNotes, courseNotes);
            intentCourseNotes.putExtra(CourseNotes.extraCourseNotes_Name, CourseName.getText());
            intentCourseNotes.putExtra(CourseNotes.extraCourseNotes_Status, CourseStatus.getText());
            intentCourseNotes.putExtra(CourseNotes.extraCourseNotes_Start, CourseStartDate.getText());
            intentCourseNotes.putExtra(CourseNotes.extraCourseNotes_End, CourseEndDate.getText());
            startActivityForResult(intentCourseNotes, ADD_NOTES_REQUEST);
        });
        fabMentor.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: fabMentor Clicked");
            Intent intentCourseMentor = new Intent(CourseDetailsActivity.this, MentorList.class);
            intentCourseMentor.putExtra(MentorList.extraCourseMentor_CourseID, courseID);
            intentCourseMentor.putExtra(MentorList.extraCourseMentor_Name, CourseName.getText());
            intentCourseMentor.putExtra(MentorList.extraCourseMentor_Status, CourseStatus.getText());
            intentCourseMentor.putExtra(MentorList.extraCourseMentor_Start, CourseStartDate.getText());
            intentCourseMentor.putExtra(MentorList.extraCourseMentor_End, CourseEndDate.getText());
            intentCourseMentor.putExtra(MentorList.extraCourseMentor_MentorName, mentorName);
            intentCourseMentor.putExtra(MentorList.extraCourseMentor_MentorPhone, mentorPhone);
            intentCourseMentor.putExtra(MentorList.extraCourseMentor_MentorEmail, mentorEmail);
            startActivityForResult(intentCourseMentor, ADD_MENTOR_REQUEST);
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode = " + requestCode);
        if (requestCode == EDIT_COURSE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(EditCourse.extraCourseEdit_CourseID, -1);
            if (id == -1) {
                Toast.makeText(this, "Course could not be saved.", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(EditCourse.extraCourseEdit_Name);
            String status = data.getStringExtra(EditCourse.extraCourseEdit_Status);
            Log.d(TAG, "onActivityResult: extraCourseEdit_Name = " + data.getStringExtra(EditCourse.extraCourseEdit_Name));
            Log.d(TAG, "onActivityResult: extraCourseEdit_Start = " + data.getStringExtra(EditCourse.extraCourseEdit_Start));
            Log.d(TAG, "onActivityResult: extraCourseEdit_End = " + data.getStringExtra(EditCourse.extraCourseEdit_End));
            //Get the EXTRA_START_DATE String and convert it into a Date
            Date startDate = null;
            try {
                startDate = Converters.StringToDate(data.getStringExtra(EditCourse.extraCourseEdit_Start));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Get the endDate and convert
            Date endDate = null;
            try {
                endDate = Converters.StringToDate(data.getStringExtra(EditCourse.extraCourseEdit_End));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            course course = new course(title, status, startDate, endDate, courseTermFKID);
            Log.d(TAG, "onActivityResult: course = " + course.toString());
            course.setCourse_id(id);
            courseViewModel.update(course);

            CourseName.setText(data.getStringExtra(EditCourse.extraCourseEdit_Name));
            CourseStartDate.setText(data.getStringExtra(EditCourse.extraCourseEdit_Start));
            CourseEndDate.setText(data.getStringExtra(EditCourse.extraCourseEdit_End));
            CourseStatus.setText(data.getStringExtra(EditCourse.extraCourseEdit_Status));

            Toast.makeText(this, "Course Updated", Toast.LENGTH_LONG).show();
        } else if (requestCode == ADD_ASSESSMENT_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(EditAssessment.extraAssessmentEdit_Name);
            String info = data.getStringExtra(EditAssessment.extraAssessmentEdit_Info);
            String type = data.getStringExtra(EditAssessment.extraAssessmentEdit_Type);
            Date due = null;
            try {
                Log.d(TAG, "onActivityResult: EXTRA_ASSESSMENT_ADDEDIT_ASSESSMENT_DUEDATE = " + data.getStringExtra(EditAssessment.extraAssessmentEdit_DueDate));
                due = Converters.StringToDate(data.getStringExtra(EditAssessment.extraAssessmentEdit_DueDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assessment assessment = new assessment(name, type, due, info, courseID);
            assessmentViewModel.insert(assessment);
        } else if (requestCode == EDIT_ASSESSMENT_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(EditAssessment.extraAssessmentEdit_ID, -1);
            String name = data.getStringExtra(EditAssessment.extraAssessmentEdit_Name);
            String info = data.getStringExtra(EditAssessment.extraAssessmentEdit_Info);
            String type = data.getStringExtra(EditAssessment.extraAssessmentEdit_Type);
            Date due = null;
            try {
                Log.d(TAG, "onActivityResult: EXTRA_ASSESSMENT_ADDEDIT_ASSESSMENT_DUEDATE = " + data.getStringExtra(EditAssessment.extraAssessmentEdit_DueDate));
                due = Converters.StringToDate(data.getStringExtra(EditAssessment.extraAssessmentEdit_DueDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assessment assessment = new assessment(name, type, due, info, courseID);
            assessment.setAssessment_id(id);
            assessmentViewModel.update(assessment);
        } else if (requestCode == ADD_NOTES_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(CourseNotes.extraCourseNotes_CourseID, -1);
            String notes = data.getStringExtra(CourseNotes.extraCourseNotes_CourseNotes);

            String title = data.getStringExtra(CourseNotes.extraCourseNotes_Name);
            String status = data.getStringExtra(CourseNotes.extraCourseNotes_Status);
            Date startDate = null;
            try {
                startDate = Converters.StringToDate(data.getStringExtra(CourseNotes.extraCourseNotes_Start));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //convert to date
            Date endDate = null;
            try {
                endDate = Converters.StringToDate(data.getStringExtra(CourseNotes.extraCourseNotes_End));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            course course = new course(title, status, startDate, endDate, courseTermFKID);
            Log.d(TAG, "onActivityResult: course = " + course.toString());
            course.setCourse_id(id);
            course.setCourse_notes(notes);
            course.setMentor_name(mentorName);
            course.setMentor_phone(mentorPhone);
            course.setMentor_email(mentorEmail);
            courseViewModel.update(course);

            courseNotes = notes;

        } else if (requestCode == ADD_MENTOR_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(MentorList.extraCourseMentor_CourseID, -1);
            String mentorName = data.getStringExtra(MentorList.extraCourseMentor_MentorName);
            String mentorPhone = data.getStringExtra(MentorList.extraCourseMentor_MentorPhone);
            String mentorEmail = data.getStringExtra(MentorList.extraCourseMentor_MentorEmail);


            String title = data.getStringExtra(MentorList.extraCourseMentor_Name);
            String status = data.getStringExtra(MentorList.extraCourseMentor_Status);
            Date startDate = null;
            try {
                startDate = Converters.StringToDate(data.getStringExtra(MentorList.extraCourseMentor_Start));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //convert to date
            Date endDate = null;
            try {
                endDate = Converters.StringToDate(data.getStringExtra(MentorList.extraCourseMentor_End));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            course course = new course(title, status, startDate, endDate, courseTermFKID);
            Log.d(TAG, "onActivityResult: course = " + course.toString());
            course.setCourse_id(id);
            course.setCourse_notes(courseNotes);
            course.setMentor_name(mentorName);
            course.setMentor_phone(mentorPhone);
            course.setMentor_email(mentorEmail);
            courseViewModel.update(course);

            this.mentorName = mentorName;
            this.mentorPhone = mentorPhone;
            this.mentorEmail = mentorEmail;
        }
    }
}

