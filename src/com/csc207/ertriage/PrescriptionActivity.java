package com.csc207.ertriage;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.csc207.triage.ER;
import com.csc207.triage.Patient;
import com.csc207.triage.Prescription;

public class PrescriptionActivity extends ActionBarActivity {

	private String healthCard;
	private Patient p;

	private ER er = ER.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prescription);
		setupActionBar();

		// Get the health card number that was passed into this Intent
		healthCard = (String)getIntent().getSerializableExtra("patient");

		// Gets the actual Patient object to modify
		p = er.getPatient(healthCard);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.prescription, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
		case R.id.action_discard:
			this.finish();
			return true;
		case R.id.action_accept:
			String medication = ((EditText)findViewById(
					R.id.medication)).getText().toString();
			String instructions = ((EditText)findViewById(
					R.id.instructions)).getText().toString();

			// Add the Prescription to the Patient object iff it's not blank
			if (medication.length() != 0 && instructions.length() != 0)
				if (medication.indexOf('=') < 1 &&
						instructions.indexOf('=') < 1) {
					p.addPrescriptions(
							new Prescription(medication, instructions));
					this.finish();
					return true;
				} else {
					Toast.makeText(getApplicationContext(),
							R.string.equals_error, Toast.LENGTH_SHORT).show();
					return false;
				}
		}
		return super.onOptionsItemSelected(item);
	}
}