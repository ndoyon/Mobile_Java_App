package com.example.ndoyon_c196.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ndoyon_c196.Entity.term;
import com.example.ndoyon_c196.Repositories.TermRepository;

import java.util.List;

public class termView extends AndroidViewModel {
    private TermRepository repository;
    private LiveData<List<term>> allTerms;

    public termView(Application application) {
        super(application);
        repository = new TermRepository(application);
        allTerms = repository.getAllTerms();
    }

    public void insert(term term) { repository.insert(term); }
    public void update(term term) { repository.update(term); }
    public void delete(term term) { repository.delete(term); }
    public void deleteAllNotes() { repository.deleteAllNotes(); }
    public LiveData<List<term>> getAllTerms() { return allTerms; }
    public int getCourseCount(int term_id) { return repository.getCourseCount(term_id); }

}
