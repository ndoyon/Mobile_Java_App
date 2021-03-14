package com.example.ndoyon_c196.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ndoyon_c196.Adapters.TermAdapter;
import com.example.ndoyon_c196.Entity.term;
import com.example.ndoyon_c196.R;
import com.example.ndoyon_c196.ViewModel.termView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.ndoyon_c196.Activity.EditTerm.extraTerm_EndDate;
import static com.example.ndoyon_c196.Activity.EditTerm.extraTerm_StartDate;
import static com.example.ndoyon_c196.Activity.EditTerm.extraTerm_Status;
import static com.example.ndoyon_c196.Activity.EditTerm.extraTerm_Title;

public class TermList extends AppCompatActivity {
    private termView termViewModel;

    private static final String TAG = "TermList";
    public static final int ADD_TERM_REQUEST = 101;
    public static final int EDIT_TERM_REQUEST = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Term List");
        setContentView(R.layout.activity_term_list);

        RecyclerView recyclerView = findViewById(R.id.termList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        TermAdapter adapter = new TermAdapter();
        recyclerView.setAdapter(adapter);


        termViewModel = new ViewModelProvider(this).get(termView.class);
        termViewModel.getAllTerms().observe(this, new Observer<List<term>>() {
            @Override
            public void onChanged(@Nullable final List<term> terms) {
                //update RecyclerView
                adapter.setTerms(terms);
            }
        });

       //FAB setup
        FloatingActionButton fab = findViewById(R.id.fabAddTerm);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(TermList.this, EditTerm.class);
            startActivityForResult(intent, ADD_TERM_REQUEST);
        });

        //swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                term deletedTerm = adapter.getTermAt(viewHolder.getAdapterPosition());
                int courseCount = termViewModel.getCourseCount(deletedTerm.getTerm_id());
                if (courseCount == 0) {
                    termViewModel.delete(adapter.getTermAt(viewHolder.getAdapterPosition()));
                    Toast.makeText(TermList.this, "Term Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.restoreItem(deletedTerm, viewHolder.getAdapterPosition());
                    Toast.makeText(TermList.this, "Delete all courses in this term first.", Toast.LENGTH_LONG).show();
                }
            }
        }).attachToRecyclerView(recyclerView);

        //click term listener
        adapter.setOnItemClickListener(new TermAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(term term) {
                Log.d(TAG, "onItemClick: term name = " + term.getTerm_name());
                Intent intent = new Intent(TermList.this, TermDetailsActivity.class);
                intent.putExtra("TERM_ID", String.valueOf(term.getTerm_id()));
                intent.putExtra("TERM_NAME", term.getTerm_name());
                intent.putExtra("TERM_STATUS", term.getTerm_status());
                intent.putExtra("TERM_START_DATE",term.getTerm_start().toString());
                intent.putExtra("TERM_END_DATE",term.getTerm_end().toString());
                startActivity(intent);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String title = data.getStringExtra(extraTerm_Title);
        String status = data.getStringExtra(extraTerm_Status);

        Date startDate = null;
        try { startDate = StringToDate(data.getStringExtra(extraTerm_StartDate)); }
        catch (ParseException e) { e.printStackTrace(); }

        Date endDate = null;
        try { endDate = StringToDate(data.getStringExtra(extraTerm_EndDate)); }
        catch (ParseException e) { e.printStackTrace(); }

        term term = new term(title, status, startDate, endDate);

        if (requestCode == ADD_TERM_REQUEST && resultCode == RESULT_OK) {
            termViewModel.insert(term);
            Toast.makeText(this, "Term Added", Toast.LENGTH_LONG).show();
        } else if (requestCode == EDIT_TERM_REQUEST && resultCode == RESULT_OK) {
            termViewModel.update(term);

        }
    }

   //converts string date
    private Date StringToDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date returnDate = formatter.parse(date);
        return returnDate;
    }
}