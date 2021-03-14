package com.example.ndoyon_c196.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.ndoyon_c196.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

import com.example.ndoyon_c196.Utility.projectDB;

public class HomePageActivity extends AppCompatActivity {
    projectDB db;

    TextView coursesPendingTextView;
    TextView coursesCompletedTextView;
    TextView coursesDroppedTextView;
    TextView termsPendingTextView;
    TextView termsPassedTextView;
    TextView termsFailedTextView;
    ExtendedFloatingActionButton startButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setTitle("Student Schedule");
        db = projectDB.getInstance(getApplicationContext());


        coursesPendingTextView = findViewById(R.id.coursesPendingTextView);
        coursesCompletedTextView = findViewById(R.id.coursesCompletedTextView);
        coursesDroppedTextView = findViewById(R.id.coursesDroppedTextView);
        termsPendingTextView = findViewById(R.id.termsPendingTextView);
        termsPassedTextView = findViewById(R.id.termsPassedTextView);
        termsFailedTextView = findViewById(R.id.termsFailedTextView);


        updateViews();

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TermList.class);
                startActivity(intent);
            }
        });

        //nuke button to show programming a button via activity instead of xml.
        ConstraintLayout myLayout = findViewById(R.id.homePageConstraintLayout);
        ConstraintSet set = new ConstraintSet();
        Button nukeDBbutton = new Button(getApplicationContext());
        nukeDBbutton.setText("Nuke  DB.");
        nukeDBbutton.setId(R.id.nukeDBButton);

        set.constrainHeight(nukeDBbutton.getId(), ConstraintSet.WRAP_CONTENT);
        set.constrainWidth(nukeDBbutton.getId(), ConstraintSet.WRAP_CONTENT);
        set.connect(nukeDBbutton.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 8);
        set.connect(nukeDBbutton.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 8);

        if (myLayout.getParent() != null) {
            ((ViewGroup) myLayout.getParent()).removeView(myLayout);
        }

        myLayout.addView(nukeDBbutton);
        setContentView(myLayout);
        set.applyTo(myLayout);


        nukeDBbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.clearAllTables();
                updateViews();
            }
        });


    }



    //update the view to display courses and terms
    private void updateViews() {

        int coursesPending = 0;
        int coursesCompleted = 0;
        int coursesDropped = 0;
        int termsPending = 0;
        int termsCompleted = 0;
        int termsInProgress= 0;

        try {
            List<com.example.ndoyon_c196.Entity.term> termList = db.termDao().reportAllTerms();
            List<com.example.ndoyon_c196.Entity.course> courseList = db.courseDao().reportAllCourses();


            try {
                for (int i = 0; i < courseList.size(); i++) {

                    if (courseList.get(i).getCourse_status().contains("Plan to Take")) coursesPending++;
                    if (courseList.get(i).getCourse_status().contains("In-Progress"))
                        coursesPending++;
                    if (courseList.get(i).getCourse_status().contains("Completed"))
                        coursesCompleted++;
                    if (courseList.get(i).getCourse_status().contains("Dropped")) coursesDropped++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                for (int i = 0; i < termList.size(); i++) {

                    if (termList.get(i).getTerm_status().contains("Not Started"))
                        termsPending++;
                    if (termList.get(i).getTerm_status().contains("In-progress"))
                        termsInProgress++;
                    if (termList.get(i).getTerm_status().contains("Completed"))
                        termsCompleted++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        coursesPendingTextView.setText(String.valueOf(coursesPending));
        coursesCompletedTextView.setText(String.valueOf(coursesCompleted));
        coursesDroppedTextView.setText(String.valueOf(coursesDropped));
        termsPendingTextView.setText(String.valueOf(termsPending));
        termsFailedTextView.setText(String.valueOf(termsInProgress));
        termsPassedTextView.setText(String.valueOf(termsCompleted));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return true;
    }
}


