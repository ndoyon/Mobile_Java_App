package com.example.ndoyon_c196.Entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "assessment_table",
        foreignKeys = @ForeignKey(
                entity = course.class,
                parentColumns = "course_id",
                childColumns = "course_id_fk",
                onDelete = ForeignKey.CASCADE
        )
)
public class assessment {
    @PrimaryKey(autoGenerate = true)

    private int assessment_id;
    private String assessment_name;
    private String assessment_type;
    private Date assessment_due_date;
    private String assessment_info;

    private int course_id_fk;

    public assessment(String assessment_name, String assessment_type, Date assessment_due_date, String assessment_info, int course_id_fk) {
        this.assessment_name = assessment_name;
        this.assessment_type = assessment_type;
        this.assessment_due_date = assessment_due_date;
        this.assessment_info = assessment_info;
        this.course_id_fk = course_id_fk;

    }

    public int getCourse_id_fk() {
        return course_id_fk;
    }

    public void setCourse_id_fk(int course_id_fk) {
        this.course_id_fk = course_id_fk;
    }

    public int getAssessment_id() {
        return assessment_id;
    }

    public void setAssessment_id(int assessment_id) {
        this.assessment_id = assessment_id;
    }

    public String getAssessment_name() {
        return assessment_name;
    }

    public void setAssessment_name(String assessment_name) {
        this.assessment_name = assessment_name;
    }

    public String getAssessment_type() {
        return assessment_type;
    }

    public void setAssessment_type(String assessment_type) {
        this.assessment_type = assessment_type;
    }

    public Date getAssessment_due_date() {
        return assessment_due_date;
    }

    public void setAssessment_due_date(Date assessment_due_date) {
        this.assessment_due_date = assessment_due_date;
    }

    public String getAssessment_info() {
        return assessment_info;
    }

    public void setAssessment_info(String assessment_info) {
        this.assessment_info = assessment_info;
    }


}

