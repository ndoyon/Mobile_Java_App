package com.example.ndoyon_c196.Utility;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.ndoyon_c196.DAO.assessmentDAO;
import com.example.ndoyon_c196.DAO.courseDAO;
import com.example.ndoyon_c196.DAO.courseMentorDAO;
import com.example.ndoyon_c196.DAO.termDAO;
import com.example.ndoyon_c196.Entity.assessment;
import com.example.ndoyon_c196.Entity.course;
import com.example.ndoyon_c196.Entity.courseMentor;
import com.example.ndoyon_c196.Entity.term;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@androidx.room.Database(entities = {term.class, course.class, courseMentor.class, assessment.class}, exportSchema = false, version = 7 )
@TypeConverters({Converters.class})
public abstract class projectDB extends RoomDatabase {

    private static final String DB_Name = "project_DB.db";
    private static projectDB instance;

    public static synchronized projectDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), projectDB.class, DB_Name).fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return instance;
    }

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public abstract termDAO termDao();

    public abstract courseDAO courseDao();

    public abstract courseMentorDAO courseMentorDao();

    public abstract assessmentDAO assessmentDao();
}