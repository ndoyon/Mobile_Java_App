package com.example.ndoyon_c196.DAO;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.ndoyon_c196.Entity.courseMentor;

@Dao
public interface courseMentorDAO {
    @Query("SELECT * FROM course_mentor_table WHERE course_id_fk = :courseID ORDER BY mentor_id")
    List<courseMentor> getMentorList(int courseID);

    @Query("SELECT * FROM  course_mentor_table WHERE course_id_fk = :courseID and mentor_id = :mentorID")
    courseMentor getMentor(int courseID, int mentorID);

    @Insert
    void insertMentor(courseMentor courseMentor);

    @Insert
    void insertAllCourseMentors(courseMentor... courseMentor);

    @Update
    void updateMentor(courseMentor courseMentor);

    @Delete
    void deleteMentor(courseMentor courseMentor);
}