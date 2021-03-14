package com.example.ndoyon_c196.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.ndoyon_c196.DAO.termDAO;
import com.example.ndoyon_c196.Entity.term;
import com.example.ndoyon_c196.Utility.projectDB;

import java.util.List;

public class TermRepository {
    private termDAO termDao;
    private LiveData<List<term>> allTerms;

    public TermRepository(Application application) {
        projectDB database = projectDB.getInstance(application);
        termDao = database.termDao();
        allTerms = (LiveData<List<term>>) termDao.getAllTerms();
    }

    public void insert(term term) { projectDB.databaseWriteExecutor.execute(() -> termDao.insertTerm(term)); }
    public void update(term term) { new UpdateTermAsyncTask(termDao).execute(term); }
    public void delete(term term) { new DeleteTermAsyncTask(termDao).execute(term); }
    public void deleteAllNotes() { new DeleteAllNotesAsyncTask(termDao).execute(); }

    private static class InsertTermAsyncTask extends AsyncTask<term, Void, Void> {
        private termDAO termDao;

        private InsertTermAsyncTask(termDAO termDao) {
            this.termDao = termDao;
        }
        @Override
        protected Void doInBackground(term... terms) {
            termDao.insertTerm(terms[0]);
            return null;
        }
    }

    private static class UpdateTermAsyncTask extends AsyncTask<term, Void, Void> {
        private termDAO termDao;

        private UpdateTermAsyncTask(termDAO termDao) {
            this.termDao = termDao;
        }
        @Override
        protected Void doInBackground(term... terms) {
            termDao.updateTerm(terms[0]);
            return null;
        }
    }

    private static class DeleteTermAsyncTask extends AsyncTask<term, Void, Void> {
        private termDAO termDao;

        private DeleteTermAsyncTask(termDAO termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(term... terms) {
            termDao.deleteTerm(terms[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private termDAO termDao;

        private DeleteAllNotesAsyncTask(termDAO termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            termDao.deleteAllTerms();
            return null;
        }
    }

    public LiveData<List<term>> getAllTerms() {
        return allTerms;
    }

    public int getCourseCount(int term_id) { return termDao.getCourseCount(term_id); }

}
