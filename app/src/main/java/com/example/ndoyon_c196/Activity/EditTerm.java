package com.example.ndoyon_c196.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.ndoyon_c196.R;

import java.util.Calendar;


public class EditTerm extends AppCompatActivity {

    public static final String extraTerm_ID = "extraTerm_ID";
    public static final String extraTerm_Title = "extraTerm_Title";
    public static final String extraTerm_StartDate = "extraTerm_StartDate";
    public static final String extraTerm_EndDate = "extraTerm_EndDate";
    public static final String extraTerm_Status = "extraTerm_Status";


    private EditText editTextTitle;
    private Spinner spinnerStatus;
    private EditText editTextStartDate;
    private EditText editTextEndDate;

    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Term");
        setContentView(R.layout.activity_edit_term);
        initStartDatePicker();
        initEndDatePicker();

        //setting up the create term/edit term
        editTextTitle = findViewById(R.id.termName);
        spinnerStatus = findViewById(R.id.spinner_term_status);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.term_statuses_array,
                R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerAdapter);
        editTextStartDate = findViewById(R.id.startDate);
        editTextEndDate = findViewById(R.id.endDate);

        //save the term
        final Button button = findViewById(R.id.btnTermSave);
        button.setOnClickListener(view -> saveTerm());


        Intent intent = getIntent();
        if (intent.hasExtra(extraTerm_ID)) {
            editTextTitle.setText(intent.getStringExtra(extraTerm_Title));
            spinnerStatus.setSelection(spinnerAdapter.getPosition(intent.getStringExtra(extraTerm_Status)));
            editTextStartDate.setText(intent.getStringExtra(extraTerm_StartDate));
            editTextEndDate.setText(intent.getStringExtra(extraTerm_EndDate));
            button.setText("Update Term");
        }
    }

    private void initStartDatePicker() {
        DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                editTextStartDate.setText(date);
            }
        };
        //date picker
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.BUTTON_POSITIVE;
        //create the date picker dialog
        startDatePickerDialog = new DatePickerDialog(this, style, startDateSetListener, year, month, day);
    }
    private void initEndDatePicker() {
        DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                editTextEndDate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.BUTTON_POSITIVE;
        //create the date picker dialog
        endDatePickerDialog = new DatePickerDialog(this, style, endDateSetListener, year, month, day);
    }

    private void saveTerm() {
        int termID = getIntent().getIntExtra(extraTerm_ID, -1);
        String title = editTextTitle.getText().toString();
        String startDate = editTextStartDate.getText().toString();
        String endDate = editTextEndDate.getText().toString();
        String status = spinnerStatus.getSelectedItem().toString();

        if (title.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty() || status.trim().isEmpty()) {
            if (title.trim().isEmpty()) { editTextTitle.setError("Term Name is required"); }
            if (startDate.trim().isEmpty()) { editTextStartDate.setError("Term Start Date is required."); }
            if (endDate.trim().isEmpty()) { editTextEndDate.setError("Term End Date is required."); }

            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        if (termID != -1) { data.putExtra(extraTerm_ID, termID); }
        data.putExtra(extraTerm_Title, title);
        data.putExtra(extraTerm_Status, status);
        data.putExtra(extraTerm_StartDate, startDate);
        data.putExtra(extraTerm_EndDate, endDate);
        setResult(RESULT_OK, data);
        finish();
    }

    public void openStartDatePicker(View view) {
        startDatePickerDialog.show();
    }
    public void openEndDatePicker(View view) {
        endDatePickerDialog.show();
    }
}