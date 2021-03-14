package com.example.ndoyon_c196.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.ndoyon_c196.DAO.courseDAO;
import com.example.ndoyon_c196.Entity.course;
import com.example.ndoyon_c196.Utility.projectDB;

import java.util.List;

public class CourseRepository {
    private courseDAO courseDao;
    private LiveData<List<course>> allCourses;
    private LiveData<List<course>> termCourses;

    public CourseRepository(Application application) {
        projectDB database = projectDB.getInstance(application);
        courseDao = database.courseDao();
        allCourses = (LiveData<List<course>>) courseDao.getAllCourses();
    }

    public void insert(course course) { projectDB.databaseWriteExecutor.execute(() -> courseDao.insertCourse(course)); }
    public void update(course course) { new UpdateCourseAsyncTask(courseDao).execute(course); }
    public void delete(course course) { new DeleteCourseAsyncTask(courseDao).execute(course); }
    public void deleteAllCourses() { new DeleteAllCoursesAsyncTask(courseDao).execute(); }

    private static class InsertCourseAsyncTask extends AsyncTask<course, Void, Void> {
        private courseDAO courseDao;

        private InsertCourseAsyncTask(courseDAO courseDao) {
            this.courseDao = courseDao;
        }
        @Override
        protected Void doInBackground(course... courses) {
            courseDao.insertCourse(courses[0]);
            return null;
        }
    }

    private static class UpdateCourseAsyncTask extends AsyncTask<course, Void, Void> {
        private courseDAO courseDao;

        private UpdateCourseAsyncTask(courseDAO courseDao) {
            this.courseDao = courseDao;
        }
        @Override
        protected Void doInBackground(course... courses) {
            courseDao.updateCourse(courses[0]);
            return null;
        }
    }

    private static class DeleteCourseAsyncTask extends AsyncTask<course, Void, Void> {
        private courseDAO courseDao;

        private DeleteCourseAsyncTask(courseDAO courseDao) {
            this.courseDao = courseDao;
        }
        @Override
        protected Void doInBackground(course... courses) {
            courseDao.deleteCourse(courses[0]);
            return null;
        }
    }

    private static class DeleteAllCoursesAsyncTask extends AsyncTask<Void, Void, Void> {
        private courseDAO courseDao;

        private DeleteAllCoursesAsyncTask(courseDAO courseDao) {
            this.courseDao = courseDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            courseDao.deleteAllCourses();
            return null;
        }
    }

    public LiveData<List<course>> getAllCourses() {
        return allCourses;
    }
    public LiveData<List<course>> getTermCourses(int term_id) {
        termCourses = courseDao.getTermCourses(term_id);
        return termCourses;
    }
}
