package com.example.ndoyon_c196.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ndoyon_c196.Entity.course;
import com.example.ndoyon_c196.Repositories.CourseRepository;

import java.util.List;

public class courseView extends AndroidViewModel {
    public static final String TAG = "CourseViewModel";
    private CourseRepository repository;
    private LiveData<List<course>> allCourses;
    private LiveData<List<course>> termCourses;

    public courseView(Application application) {
        super(application);
        repository = new CourseRepository(application);
        allCourses = repository.getAllCourses();
    }

    public void insert(course course) { repository.insert(course); }
    public void update(course course) { repository.update(course); }
    public void delete(course course) { repository.delete(course); }
    public void deleteAllCourses() { repository.deleteAllCourses(); }

    public LiveData<List<course>> getAllCourses() { return allCourses; }
    public LiveData<List<course>> getTermCourses(int term_id) {
        termCourses = repository.getTermCourses(term_id);
        return termCourses;
    }
}