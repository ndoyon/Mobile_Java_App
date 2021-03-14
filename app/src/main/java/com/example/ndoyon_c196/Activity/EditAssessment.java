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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ndoyon_c196.R;
import com.example.ndoyon_c196.Utility.Alerts;
import com.example.ndoyon_c196.Utility.Converters;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class EditAssessment extends AppCompatActivity {

        public static final String TAG = "AssessmentAddEditActivity";
        public static final String SHARED_PREFS = "sharedPrefs";
        public static final String ALERT_UNIQUE_ID = "ALERT_UNIQUE_ID";

        public static final String extraAssessmentEdit_Course_FKID = "extraAssessmentEdit_Course_FKID";
        public static final String extraAssessmentEdit_ID = "extraAssessmentEdit_ID";
        public static final String extraAssessmentEdit_Name = "extraAssessmentEdit_Name";
        public static final String extraAssessmentEdit_Type = "extraAssessmentEdit_Type";
        public static final String extraAssessmentEdit_Info = "extraAssessmentEdit_Info";
        public static final String extraAssessmentEdit_DueDate = "extraAssessmentEdit_DueDate";

        private EditText editTextName;
        private EditText editTextDueDate;
        private EditText editTextInfo;
        private RadioGroup radioGroupType;
        private CheckBox checkAlertDue;
        private int courseFKID;

        private DatePickerDialog dueDatePickerDialog;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setTitle("Edit Assessments");
            setContentView(R.layout.activity_edit_assessment);
            initDueDatePicker();

            editTextName = findViewById(R.id.assessmentName);
            editTextDueDate = findViewById(R.id.assessmentDueDate); //onClick is in the layout
            editTextInfo = findViewById(R.id.assessmentEditInfo);
            radioGroupType = findViewById(R.id.radGroupAssessment);
            checkAlertDue = findViewById(R.id.checkboxAssessmentDueAlert);

            final Button saveButton = findViewById(R.id.btnSave);
            saveButton.setOnClickListener(view -> saveAssessment());

            //prefill the data if this is an edit to the assessment
            Intent intent = getIntent();
            if (intent.hasExtra(extraAssessmentEdit_ID)) {
                courseFKID = intent.getIntExtra(extraAssessmentEdit_Course_FKID, -1);
                editTextName.setText(intent.getStringExtra(extraAssessmentEdit_Name));
                editTextDueDate.setText(intent.getStringExtra(extraAssessmentEdit_DueDate));
                editTextInfo.setText(intent.getStringExtra(extraAssessmentEdit_Info));
                saveButton.setText("Update Assessment");
            }
        }

        private void saveAssessment() {
            Log.d(TAG, "saveAssessment: clicked.");
            Intent data = new Intent();
            int id = getIntent().getIntExtra(extraAssessmentEdit_ID, -1);
            Log.d(TAG, "saveAssessment: course id = " + id);
            if (id != -1) {
                data.putExtra(extraAssessmentEdit_ID, id);
            }
            String name = editTextName.getText().toString();
            String due = editTextDueDate.getText().toString();
            Log.d(TAG, "saveAssessment: due date = " + due);
            String info = editTextInfo.getText().toString();

            int selectedId = radioGroupType.getCheckedRadioButtonId();
            RadioButton selectedType = findViewById(selectedId);
            String type = (String) selectedType.getText();

            if (name.trim().isEmpty() || due.trim().isEmpty() || type.trim().isEmpty()) {
                if (name.trim().isEmpty()) {
                    editTextName.setError("Assessment Name Required.");
                }
                if (due.trim().isEmpty()) {
                    editTextDueDate.setError("Due Date Required.");
                }
                Toast.makeText(this, "Missing Information", Toast.LENGTH_SHORT).show();
                return;
            }

            Date assessmentDueAlertDate = null;
            try {
                assessmentDueAlertDate = Converters.StringToDate(due);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (checkAlertDue.isChecked() == true) {
                Calendar alertDate = Calendar.getInstance();
                alertDate.setTime(assessmentDueAlertDate);
                setAlarm(alertDate, name, "Assessment is due today.");
            }

            data.putExtra(extraAssessmentEdit_Course_FKID, courseFKID);
            data.putExtra(extraAssessmentEdit_Name, name);
            data.putExtra(extraAssessmentEdit_Info, info);
            data.putExtra(extraAssessmentEdit_Type, type);
            data.putExtra(extraAssessmentEdit_DueDate, due);
            setResult(RESULT_OK, data);
            finish();
        }

        private void initDueDatePicker() {
            DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month = month + 1;
                    String date = month + "/" + dayOfMonth + "/" + year;
                    editTextDueDate.setText(date);
                }
            };
            //Datepicker setup
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int style = AlertDialog.BUTTON_POSITIVE;
            //create the date picker dialog
            dueDatePickerDialog = new DatePickerDialog(this, style, startDateSetListener, year, month, day);
        }

        public void openDueDatePicker(View view) {
            dueDatePickerDialog.show();
        }

        //set Alarm/notification
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