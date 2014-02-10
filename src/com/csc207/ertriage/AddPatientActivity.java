package com.csc207.ertriage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.csc207.triage.ER;
import com.csc207.triage.Patient;

public class AddPatientActivity extends ActionBarActivity {

	private Button dob;
	private String surname, given, healthCard, birthday, currentTime;

	private ER er = ER.getInstance();

	// Based on code from http://goo.gl/GxYXO4
	// Update the label on the date of birth button when the date picker is used
	private final Calendar calendar = Calendar.getInstance();
	DatePickerDialog.OnDateSetListener date =
			new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month,
				int day) {
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DAY_OF_MONTH, day);
			updateDobLabel();
		}
	};

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_patient);
		setupActionBar();

		// Birthday button
		birthday = "";
		dob = (Button)findViewById(R.id.dob);
		// Set the date picker dialog to show the current date when opened
		dob.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(AddPatientActivity.this, date,
						calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		// Set the current time and display it
		currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
				Calendar.getInstance().getTime());
		((TextView)findViewById(R.id.currentTime)).setText(currentTime);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_patient, menu);
		return true;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_accept:
			healthCard = ((EditText)
					findViewById(R.id.healthCard)).getText().toString();

			// Check if the health card isn't already in the database
			if (er.getPatient(healthCard) == null) {
				surname = ((EditText)
						findViewById(R.id.lastName)).getText().toString();
				given = ((EditText)
						findViewById(R.id.firstName)).getText().toString();

				// Check that all the fields were filled in
				if (surname.length() != 0 && given.length() != 0 &&
						healthCard.length() != 0 && birthday.length() != 0) {

					// Add the Patient to the ER
					er.addPatient(new Patient(given, surname, birthday,
							healthCard, currentTime));

					// Notify the user that it was successful
					Toast.makeText(getApplicationContext(),
							R.string.success, Toast.LENGTH_SHORT).show();

					this.finish();
					return true;
				} else {
					Toast.makeText(getApplicationContext(),
							R.string.empty_fields, Toast.LENGTH_SHORT).show();
					return false;
				}
			} else {
				Toast.makeText(getApplicationContext(),
						R.string.patient_exists, Toast.LENGTH_SHORT).show();
				return false;
			}
		case R.id.action_discard:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** Updates the date of birth button text. */
	private void updateDobLabel() {
		birthday = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(
				calendar.getTime());
		dob.setText("Date of birth: " + birthday);
	}
}