package com.example.ndoyon_c196.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.ndoyon_c196.Adapters.CourseAdapter;
import com.example.ndoyon_c196.Entity.course;
import com.example.ndoyon_c196.Entity.term;
import com.example.ndoyon_c196.R;
import com.example.ndoyon_c196.Utility.Converters;
import com.example.ndoyon_c196.ViewModel.courseView;
import com.example.ndoyon_c196.ViewModel.termView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TermDetailsActivity extends AppCompatActivity {
    private static final String TAG = "TermDetailsActivity";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String ALERT_UNIQUE_ID = "ALERT_UNIQUE_ID";

    private static final int ADD_COURSE_REQUEST = 201;
    private static final int DETAILS_COURSE_REQUEST = 203;
    private static final int EDIT_TERM_REQUEST = 102;

    private termView termViewModel;
    private courseView courseViewModel;

    private TextView textTermName;
    private TextView textTermStartDate;
    private TextView textTermEndDate;
    private TextView textTermStatus;
    private FloatingActionButton fabAddCourse;
    private FloatingActionButton fabEditTerm;


    private int term_id;
    private String termName;
    private String termStatus;
    private String termStartDate;
    private String termEndDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Term Details");
        setContentView(R.layout.activity_term_details);


        termViewModel = new ViewModelProvider(this).get(termView.class);
        courseViewModel = new ViewModelProvider(this).get(courseView.class);


        Bundle extras = getIntent().getExtras();
        term_id = Integer.valueOf(extras.getString("TERM_ID"));
        termName = extras.getString("TERM_NAME");
        termStatus = extras.getString("TERM_STATUS");
        termStartDate = "";
        termEndDate = "";
        SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat fromFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy"); //Sat May 01 00:00:00 CDT 2021
        try {
            termStartDate = toFormat.format(fromFormat.parse(extras.getString("TERM_START_DATE")));
            termEndDate = toFormat.format(fromFormat.parse(extras.getString("TERM_END_DATE")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        textTermName = findViewById(R.id.termName);
        textTermStatus = findViewById(R.id.termDetailsStatus);
        textTermStartDate = findViewById(R.id.termDetailsStart);
        textTermEndDate = findViewById(R.id.termDetailsEnd);
        fabAddCourse = findViewById(R.id.fabAddCourse);
        fabEditTerm = findViewById(R.id.fabEditCourse);


        //Term details added to top of screen
        textTermName.setText(termName);
        textTermStatus.setText(termStatus);
        textTermStartDate.setText(termStartDate);
        textTermEndDate.setText(termEndDate);


        RecyclerView recyclerView = findViewById(R.id.courseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        CourseAdapter adapter = new CourseAdapter();
        recyclerView.setAdapter(adapter);

        //adapter to recycler
        courseViewModel.getTermCourses(term_id).observe(this, new Observer<List<course>>() {
            @Override
            public void onChanged(@Nullable final List<course> courses) {
                //update RecyclerView
                adapter.setCourses(courses);
            }
        });

        //swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                courseViewModel.delete(adapter.getCourseAt(viewHolder.getAdapterPosition()));
                Toast.makeText(TermDetailsActivity.this, "Course Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        //onclick details
        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(course course) {
                //Intent intent = new Intent(TermDetailsActivity.this, CourseAddEditActivity.class);
                int parentId = 999;
                Intent intent = new Intent(TermDetailsActivity.this, CourseDetailsActivity.class);
                Log.d(TAG, "onItemClick: Course ID: " + course.getCourse_id());
                intent.putExtra("TEST_INT_PASSING", parentId);
                intent.putExtra(CourseDetailsActivity.extraCourseDetailsTerms_TermFKID, course.getTerm_id_fk());
                intent.putExtra(CourseDetailsActivity.extraCourseDetails_CourseID, course.getCourse_id());
                Log.d(TAG, "onItemClick: Passing Course ID to CourseDetailsActivity: " + course.getCourse_id());
                intent.putExtra(CourseDetailsActivity.extraCourseDetails_Name, course.getCourse_name());
                intent.putExtra(CourseDetailsActivity.extraCourseDetails_Status, course.getCourse_status());
                intent.putExtra(CourseDetailsActivity.extraCourseDetails_Start, course.getCourse_start().toString());
                intent.putExtra(CourseDetailsActivity.extraCourseDetails_End, course.getCourse_end().toString());
                intent.putExtra(CourseDetailsActivity.extraCourseDetails_Notes, course.getCourse_notes());
                intent.putExtra(CourseDetailsActivity.extraCourseDetails_MentorName, course.getMentor_name());
                intent.putExtra(CourseDetailsActivity.extraCourseDetails_MentorPhone, course.getMentor_phone());
                intent.putExtra(CourseDetailsActivity.extraCourseDetails_MentorEmail, course.getMentor_email());
                Log.d(TAG, "onItemClick: CourseNotes = " + course.getCourse_notes());
                startActivityForResult(intent, DETAILS_COURSE_REQUEST);

            }
        });

       //FAB set up
        fabAddCourse.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: fabAddCourse Clicked");
            fabAddCourse.setTooltipText("Add a Course");
            Intent intent = new Intent(TermDetailsActivity.this, EditCourse.class);
            startActivityForResult(intent, ADD_COURSE_REQUEST);
        });
        fabEditTerm.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: fabEditCourse Clicked");
            fabEditTerm.setTooltipText("Edit A Term");
            Intent intent = new Intent(TermDetailsActivity.this, EditTerm.class);
            intent.putExtra(EditTerm.extraTerm_ID, term_id);
            intent.putExtra(EditTerm.extraTerm_Title, textTermName.getText());
            intent.putExtra(EditTerm.extraTerm_Status, textTermStatus.getText());
            intent.putExtra(EditTerm.extraTerm_StartDate, textTermStartDate.getText());
            intent.putExtra(EditTerm.extraTerm_EndDate, textTermEndDate.getText());
            startActivityForResult(intent, EDIT_TERM_REQUEST);
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode = " + requestCode);
        if (requestCode == ADD_COURSE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(EditCourse.extraCourseEdit_Name);
            String status = data.getStringExtra(EditCourse.extraCourseEdit_Status);

            Date startDate = null;
            try {
                startDate = Converters.StringToDate(data.getStringExtra(EditCourse.extraCourseEdit_Start));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date endDate = null;
            try {
                endDate = Converters.StringToDate(data.getStringExtra(EditCourse.extraCourseEdit_End));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            boolean alertStart = data.getBooleanExtra(EditCourse.extraCourseEdit_AlertStart, false);
            boolean alertEnd = data.getBooleanExtra(EditCourse.extraCourseEdit_AlertEnd, false);

            course course = new course(title, status, startDate, endDate, term_id);
            courseViewModel.insert(course);
            Toast.makeText(this, "Course Added", Toast.LENGTH_LONG).show();


        } else if (requestCode == EDIT_TERM_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(EditTerm.extraTerm_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Term could not be updated.", Toast.LENGTH_LONG).show();
            }

            String termTitle = data.getStringExtra(EditTerm.extraTerm_Title);
            String termStatus = data.getStringExtra(EditTerm.extraTerm_Status);
            Log.d(TAG, "onActivityResult: termStatus = " + termStatus);
            Date termStartDate = null;
            try {
                termStartDate = Converters.StringToDate(data.getStringExtra(EditTerm.extraTerm_StartDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date termEndDate = null;
            try {
                termEndDate = Converters.StringToDate(data.getStringExtra(EditTerm.extraTerm_EndDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            term term = new term(termTitle, termStatus, termStartDate, termEndDate);
            term.setTerm_id(id);
            termViewModel.update(term);
            //Updates term details
            textTermName.setText(termTitle);
            textTermStatus.setText(termStatus);
            textTermStartDate.setText(data.getStringExtra(EditTerm.extraTerm_StartDate));
            textTermEndDate.setText(data.getStringExtra(EditTerm.extraTerm_EndDate));
            Toast.makeText(this, "Term Updated", Toast.LENGTH_LONG).show();

        }
    }
}
