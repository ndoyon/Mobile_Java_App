package com.example.ndoyon_c196.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.ndoyon_c196.Entity.course;
import com.example.ndoyon_c196.Entity.term;

@Dao
public interface courseDAO {
    @Query("SELECT * FROM course_table WHERE term_id_fk = :termID ORDER BY course_id")
    List<course> getCourseList(int termID);

    @Query("SELECT * FROM course_table WHERE term_id_fk = :termID and course_id = :courseID")
    course getCourse(int termID, int courseID);

    @Query("SELECT * FROM course_table ORDER BY course_start ASC")
    LiveData<List<course>> getAllCourses();

    @Query("SELECT * FROM course_table WHERE term_id_fk = :term_id ORDER BY course_start ASC")
    LiveData<List<course>> getTermCourses(int term_id);


    @Query("SELECT * FROM course_table ORDER BY course_start ASC")
    List<course> reportAllCourses();

    @Query("DELETE FROM course_table")
    void deleteAllCourses();


    @Insert
    void insertCourse(course course);

    @Insert
    void insertAllCourses(course... course);

    @Update
    void updateCourse(course course);

    @Delete
    void deleteCourse(course course);

}
