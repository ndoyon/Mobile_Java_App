package com.example.ndoyon_c196.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.ndoyon_c196.DAO.assessmentDAO;
import com.example.ndoyon_c196.Entity.assessment;
import com.example.ndoyon_c196.Utility.projectDB;

import java.util.List;

public class AssessmentRepository {
    private assessmentDAO assessmentDao;
    private LiveData<List<assessment>> courseAssessments;

    public AssessmentRepository(Application application) {
        projectDB database = projectDB.getInstance(application);
        assessmentDao = database.assessmentDao();
    }

    public void insert(assessment assessment) { projectDB.databaseWriteExecutor.execute(() -> assessmentDao.insertAssessment(assessment)); }
    public void update(assessment assessment) { new UpdateAssessmentAsyncTask(assessmentDao).execute(assessment); }
    public void delete(assessment assessment) { new DeleteAssessmentAsyncTask(assessmentDao).execute(assessment); }
    public void deleteAllAssessments() { new DeleteAllAssessmentAsyncTask(assessmentDao).execute(); }

    private static class UpdateAssessmentAsyncTask extends AsyncTask<assessment, Void, Void> {
        private assessmentDAO assessmentDao;

        private UpdateAssessmentAsyncTask(assessmentDAO assessmentDao) { this.assessmentDao = assessmentDao; }
        @Override
        protected Void doInBackground(assessment... assessments) {
            assessmentDao.updateAssessment(assessments[0]);
            return null;
        }
    }
    private static class DeleteAssessmentAsyncTask extends AsyncTask<assessment, Void, Void> {
        private assessmentDAO assessmentDao;

        private DeleteAssessmentAsyncTask(assessmentDAO assessmentDao) { this.assessmentDao = assessmentDao; }
        @Override
        protected Void doInBackground(assessment... assessments) {
            assessmentDao.deleteAssessment(assessments[0]);
            return null;
        }
    }
    private static class DeleteAllAssessmentAsyncTask extends AsyncTask<Void, Void, Void> {
        private assessmentDAO assessmentDao;

        private DeleteAllAssessmentAsyncTask(assessmentDAO assessmentDao) { this.assessmentDao = assessmentDao; }
        @Override
        protected Void doInBackground(Void... voids) {
            assessmentDao.deleteAllAssessments();
            return null;
        }
    }

    public LiveData<List<assessment>> getCourseAssessments(int course_id) {
        courseAssessments = assessmentDao.getCourseAssessments(course_id);
        return courseAssessments;
    }
}
