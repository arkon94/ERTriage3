package com.csc207.ertriage;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.csc207.triage.ER;
import com.csc207.triage.Nurse;
import com.csc207.triage.Patient;
import com.csc207.triage.Physician;

public class SearchActivity extends ActionBarActivity {

	private Nurse currentNurse = ER.getCurrentNurse();
	private Physician currentPhysician = ER.getCurrentPhysician();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupActionBar();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Retrieves the Patient with the given health card number. If the Patient
	 * does not exist, display an error message.
	 * @param view The button view.
	 */
	public void search(View view) {
		String healthCard =
				((EditText)findViewById(R.id.healthCard)).getText().toString();
		search(healthCard);
	}

	/**
	 * Displays PatientActivity with the Patient with the given health card
	 * number's information, if they exist in the database. If not, display
	 * an error message in a toast.
	 * @param healthCard The health card number of the Patient to look up.
	 */
	private void search(String healthCard) {
		Patient p = null;
		if (currentNurse != null)
			p = currentNurse.getPatient(healthCard);
		else if (currentPhysician != null)
			p = currentPhysician.getPatient(healthCard);


		// Check if the Patient exists or not
		if (p != null) {
			// Display the patient info if the record exists
			Intent patientIntent = new Intent(SearchActivity.this,
					PatientActivity.class);
			patientIntent.putExtra("patient", p.getHealthCard());
			startActivity(patientIntent);
		} else {
			Toast.makeText(getApplicationContext(), R.string.patient_error,
					Toast.LENGTH_SHORT).show();
		}
	}
}