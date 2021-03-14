package com.example.ndoyon_c196.DAO;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import com.example.ndoyon_c196.Entity.assessment;



@Dao
    public interface assessmentDAO {
        @Query("SELECT * FROM assessment_table WHERE course_id_fk = :courseID ORDER BY assessment_id")
        List<assessment> getAssessmentList(int courseID);

        @Query("Select * from assessment_table WHERE course_id_fk = :courseID and assessment_id = :assessmentID")
        assessment getAssessment(int courseID, int assessmentID);

        @Query("SELECT * FROM assessment_table ORDER BY assessment_due_date ASC")
        LiveData<List<assessment>> getAllAssessments();

        @Query("SELECT * FROM assessment_table WHERE course_id_fk = :course_id ORDER BY assessment_due_date ASC")
        LiveData<List<assessment>> getCourseAssessments(int course_id);

        @Query("DELETE FROM assessment_table")
        void deleteAllAssessments();

        @Insert
        void insertAssessment(assessment assessment);

        @Insert
        void insertAllAssessments(assessment... assessment);

        @Update
        void updateAssessment(assessment assessment);

        @Delete
        void deleteAssessment(assessment assessment);


    }

