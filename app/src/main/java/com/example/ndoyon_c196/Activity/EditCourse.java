package com.example.ndoyon_c196.Activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.ndoyon_c196.R;
import com.example.ndoyon_c196.Utility.Alerts;
import com.example.ndoyon_c196.Utility.Converters;


import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class EditCourse extends AppCompatActivity {

    public static final String TAG = "EditCourse";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String ALERT_UNIQUE_ID = "ALERT_UNIQUE_ID";

    public static final String extraCourseEdit_TermFKID = "extraCourseEdit_TermFKID";
    public static final String extraCourseEdit_CourseID = "extraCourseEdit_CourseID";
    public static final String extraCourseEdit_Name = "extraCourseEdit_Name";
    public static final String extraCourseEdit_Start = "extraCourseEdit_Start";
    public static final String extraCourseEdit_End = "extraCourseEdit_End";
    public static final String extraCourseEdit_Status = "extraCourseEdit_Status";
    public static final String extraCourseEdit_AlertStart = "extraCourseEdit_AlertStart";
    public static final String extraCourseEdit_AlertEnd = "extraCourseEdit_AlertEnd";


    //Course Name
    private EditText editTitle;
    //Course Status
    private Spinner spinnerStatus;
    //Course Dates
    private EditText editStartDate;
    private EditText editEndDate;
    private CheckBox checkAlertStart;
    private CheckBox checkAlertEnd;

    private int courseTermFKID;

    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Course");
        setContentView(R.layout.activity_edit_course);
        initStartDatePicker();
        initEndDatePicker();


        editTitle = findViewById(R.id.courseName);

        spinnerStatus = findViewById(R.id.spinCourseStatus);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.course_statuses_array,
                R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerAdapter);

        editStartDate = findViewById(R.id.courseStartDate);
        editEndDate = findViewById(R.id.courseEndDate);
        checkAlertStart = findViewById(R.id.checkboxCourseStartAlert);
        checkAlertEnd = findViewById(R.id.checkboxCourseEndAlert);


        final Button button = findViewById(R.id.button_add_course);
        button.setOnClickListener(view -> saveTerm());


        Intent intent = getIntent();
        if (intent.hasExtra(extraCourseEdit_CourseID)) {
            courseTermFKID = intent.getIntExtra(extraCourseEdit_TermFKID, -1);
            editTitle.setText(intent.getStringExtra(extraCourseEdit_Name));
            editStartDate.setText(intent.getStringExtra(extraCourseEdit_Start));
            editEndDate.setText(intent.getStringExtra(extraCourseEdit_End));
            spinnerStatus.setSelection(spinnerAdapter.getPosition(extraCourseEdit_Status));
            button.setText("Update Course");
        }
    }


    private void initStartDatePicker() {
        DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                editStartDate.setText(date);
            }
        };

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
                editEndDate.setText(date);
            }
        };
        //Setup the look and default selected date of the date picker
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.BUTTON_POSITIVE;
        //create the date picker dialog
        endDatePickerDialog = new DatePickerDialog(this, style, endDateSetListener, year, month, day);
    }

    public void openStartDatePicker(View view) {
        startDatePickerDialog.show();
    }

    public void openEndDatePicker(View view) {
        endDatePickerDialog.show();
    }

    private void saveTerm() {
        Log.d(TAG, "saveTerm: clicked.");
        Log.d(TAG, "onCreate: editTextStartDate = " + editStartDate.getText());
        Intent data = new Intent();
        int id = getIntent().getIntExtra(extraCourseEdit_CourseID, -1);
        if (id != -1) {
            data.putExtra(extraCourseEdit_CourseID, id);
        }
        String title = editTitle.getText().toString();
        String startDate = editStartDate.getText().toString();
        String endDate = editEndDate.getText().toString();
        String status = spinnerStatus.getSelectedItem().toString();
        boolean alertStart = checkAlertStart.isChecked();
        boolean alertEnd = checkAlertEnd.isChecked();

        if (title.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty() || status.trim().isEmpty()) {
            if (title.trim().isEmpty()) {
                editTitle.setError("Course Name is required");
            }
            if (startDate.trim().isEmpty()) {
                editStartDate.setError("Course Start Date is required.");
            }
            if (endDate.trim().isEmpty()) {
                editEndDate.setError("Course End Date is required.");
            }

            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        Date courseStartAlertDate = null;
        Date courseEndAlertDate = null;
        try {
            courseStartAlertDate = Converters.StringToDate(startDate);
            courseEndAlertDate = Converters.StringToDate(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (alertStart == true) {
            Calendar alertDate = Calendar.getInstance();
            alertDate.setTime(courseStartAlertDate);
            setAlarm(alertDate, title, "Course is scheduled to start today.");
        }
        if (alertEnd == true) {
            Calendar alertDate = Calendar.getInstance();
            alertDate.setTime(courseEndAlertDate);
            setAlarm(alertDate, title, "Course is scheduled to end today.");
        }

        data.putExtra(extraCourseEdit_TermFKID, courseTermFKID);
        data.putExtra(extraCourseEdit_Name, title);
        data.putExtra(extraCourseEdit_Start, startDate);
        data.putExtra(extraCourseEdit_End, endDate);
        data.putExtra(extraCourseEdit_Status, status);
        data.putExtra(extraCourseEdit_AlertStart, alertStart);
        data.putExtra(extraCourseEdit_AlertEnd, alertEnd);
        setResult(RESULT_OK, data);
        finish();
    }

    private void setAlarm(Calendar c, String title, String message) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int uniqueID = sharedPreferences.getInt(ALERT_UNIQUE_ID, 1);
        Log.d(TAG, "setAlarm: uniqueID = " + uniqueID);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Alerts.class);
        intent.putExtra("NOTIFICATION_TITLE", title);
        intent.putExtra("NOTIFICATION_MESSAGE", message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, uniqueID, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        uniqueID++;
        editor.putInt(ALERT_UNIQUE_ID, uniqueID);
        editor.commit();
    }
}