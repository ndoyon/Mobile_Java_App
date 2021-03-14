package com.example.ndoyon_c196.Entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "course_table",
            foreignKeys = @ForeignKey(
                    entity = term.class,
                    parentColumns = "term_id",
                    childColumns = "term_id_fk",
                    onDelete = ForeignKey.CASCADE
                    ))

public class course {
    @PrimaryKey(autoGenerate = true)
    private int course_id;
    @ColumnInfo(name = "term_id_fk")
    private int term_id_fk;
    @ColumnInfo(name = "course_name")
    private String course_name;
    @ColumnInfo(name = "course_start")
    private Date course_start;
    @ColumnInfo(name = "course_end")
    private Date course_end;
    @ColumnInfo(name = "course_status")
    private String course_status;
    @ColumnInfo(name = "course_notes")
    private String course_notes;

    private String mentor_name;
    private String mentor_phone;
    private String mentor_email;

    public course(String course_name, String course_status, Date course_start, Date course_end, int term_id_fk) {
        this.course_name = course_name;
        this.course_status = course_status;
        this.course_start = course_start;
        this.course_end = course_end;
        this.term_id_fk = term_id_fk;
    }

    public String getCourse_notes() {
        return course_notes;
    }

    public void setCourse_notes(String course_notes) {
        this.course_notes = course_notes;
    }

    public String getMentor_name() {
        return mentor_name;
    }

    public void setMentor_name(String mentor_name) {
        this.mentor_name = mentor_name;
    }

    public String getMentor_phone() {
        return mentor_phone;
    }

    public void setMentor_phone(String mentor_phone) {
        this.mentor_phone = mentor_phone;
    }

    public String getMentor_email() {
        return mentor_email;
    }

    public void setMentor_email(String mentor_email) {
        this.mentor_email = mentor_email;
    }

    public int getCourse_id() {

        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getTerm_id_fk() {
        return term_id_fk;
    }

    public void setTerm_id_fk(int term_id_fk) {
        this.term_id_fk = term_id_fk;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public Date getCourse_start() {
        return course_start;
    }

    public void setCourse_start(Date course_start) {
        this.course_start = course_start;
    }

    public Date getCourse_end() {
        return course_end;
    }

    public void setCourse_end(Date course_end) {
        this.course_end = course_end;
    }

    public String getCourse_status() {
        return course_status;
    }

    public void setCourse_status(String course_status) {
        this.course_status = course_status;
    }
}