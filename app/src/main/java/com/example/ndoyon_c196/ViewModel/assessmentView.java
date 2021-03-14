package com.example.ndoyon_c196.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ndoyon_c196.Entity.assessment;
import com.example.ndoyon_c196.Repositories.AssessmentRepository;

import java.util.List;

public class assessmentView extends AndroidViewModel {
    public static final String TAG = "AssessmentViewModel";
    private AssessmentRepository repository;
    private LiveData<List<assessment>> courseAssessments;

    public assessmentView(@NonNull Application application) {
        super(application);
        repository = new AssessmentRepository(application);
    }

    public void insert(assessment assessment) { repository.insert(assessment); }
    public void update(assessment assessment) { repository.update(assessment); }
    public void delete(assessment assessment) { repository.delete(assessment); }

    public LiveData<List<assessment>> getCourseAssessments(int course_id) {
        courseAssessments = repository.getCourseAssessments(course_id);
        return courseAssessments;
    }
}
