package com.example.ndoyon_c196.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.ndoyon_c196.Entity.term;

@Dao
public interface termDAO {
    @Query("SELECT * FROM term_table ORDER BY term_id")
        List<term> getTermList();

    @Query("SELECT * FROM term_table WHERE term_id = :termID ORDER BY term_id")
        term getTerm(int termID);

    @Query("SELECT * FROM term_table ORDER BY term_start ASC")
        LiveData<List<term>> getAllTerms();


    @Query("SELECT * FROM term_table ORDER BY term_start ASC")
        List<term> reportAllTerms();


    @Query("SELECT COUNT(*) FROM course_table WHERE term_id_fk = :term_id")
    int getCourseCount(int term_id);


    @Query("DELETE FROM term_table")
    void deleteAllTerms();

    @Insert
    void insertTerm(term term);

    @Insert
    void insertAllTerms(term... term);

    @Update
    void updateTerm(term term);

    @Delete
    void deleteTerm(term term);
}