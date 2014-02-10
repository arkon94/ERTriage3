package com.csc207.ertriage;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.csc207.triage.ER;
import com.csc207.triage.Patient;
import com.csc207.triage.Prescription;
import com.csc207.triage.Symptoms;
import com.csc207.triage.Vitals;

public class PatientActivity extends ActionBarActivity implements Serializable {

	/** Unique ID for serialization */
	private static final long serialVersionUID = -6398008940073679446L;

	/** The Patient to display the information of */
	private String healthCard;
	private Patient p;

	private ER er = ER.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient);
		setupActionBar();

		// Get the health card number that was passed into this Intent
		healthCard = (String)getIntent().getSerializableExtra("patient");

		// Gets the actual Patient object to modify
		p = er.getPatient(healthCard);

		// Display name
		((TextView)findViewById(R.id.name)).setText(
				p.getLastName() + ", " + p.getFirstName());

		// Display date of birth
		((TextView)findViewById(R.id.dob)).setText(p.getDob());

		// Display health card number
		((TextView)findViewById(R.id.healthCard)).setText(p.getHealthCard());

		// Display arrival time
		((TextView) findViewById(R.id.arrival)).setText(p.getArrivalTime());

		// Display records for vitals, symptoms, doctor visit times,
		// prescriptions and urgency/condition
		updateStats();
	}

	@Override
	public void onResume() {
		super.onResume();
		// Update the stats on activity resume
		updateStats();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.patient, menu);
		return true;
	}

	/**
	 * Hides or disables buttons depending on what type of user is logged in.
	 * @param menu The menu.
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (ER.getCurrentPhysician() == null) {
			// Hide Physician-related actions if Nurse logged in
			menu.findItem(R.id.action_prescription).setVisible(false);
			((Button)findViewById(R.id.add_prescription)).setVisibility(
					View.GONE);
		} else if (ER.getCurrentNurse() == null) {
			// Hide Nurse-related actions if Physician logged in
			menu.findItem(R.id.action_vitals).setVisible(false);
			menu.findItem(R.id.action_symptoms).setVisible(false);
			menu.findItem(R.id.action_doctor).setVisible(false);
			((Button)findViewById(R.id.add_vitals)).setVisibility(
					View.GONE);
			((Button)findViewById(R.id.add_symptoms)).setVisibility(
					View.GONE);
			((Button)findViewById(R.id.add_time)).setVisibility(
					View.GONE);
		}
		return true;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.action_vitals:
			showActivity(VitalsActivity.class);
			return true;
		case R.id.action_symptoms:
			showActivity(SymptomsActivity.class);
			return true;
		case R.id.action_doctor:
			// Get the current time and add it to the Patient's records
			p.addTimeDoctor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
					Calendar.getInstance().getTime()));
			updateStats();
			return true;
		case R.id.action_prescription:
			showActivity(PrescriptionActivity.class);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Shows the Add Vitals activity.
	 * @param view The button view.
	 */
	public void addVitals(View view) {
		showActivity(VitalsActivity.class);
	}

	/**
	 * Shows the Add Symptoms activity.
	 * @param view The button view.
	 */
	public void addSymptoms(View view) {
		showActivity(SymptomsActivity.class);
	}

	/**
	 * Shows the Add Prescription activity.
	 * @param view The button view.
	 */
	public void addPrescription(View view) {
		showActivity(PrescriptionActivity.class);
	}

	/**
	 * Adds the current time and adds it to the Patient's records
	 * @param view The button view.
	 */
	@SuppressLint("SimpleDateFormat")
	public void addTime(View view) {
		p.addTimeDoctor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
				Calendar.getInstance().getTime()));
		updateStats();
	}

	/**
	 * Starts and displays the specified activity, passing in the Patient.
	 * @param activity The activity to display.
	 */
	private void showActivity(Class<?> activity) {
		Intent intent = new Intent(this, activity);
		intent.putExtra("patient", p.getHealthCard());
		startActivityForResult(intent, 0);
	}

	/**
	 * Updates the lists of vitals, symptoms, and times seen by the doctor for
	 * the currently viewed Patient. The urgency and isImproving messages are
	 * also updated to reflect changes.
	 */
	private void updateStats() {
		// Display urgency
		Byte urgency = p.getUrgency();
		TextView urgencyText = (TextView)findViewById(R.id.urgency);
		String strUrgency = "%sUrgent (" + urgency + ")";
		if (urgency < 2) {
			urgencyText.setText(String.format(strUrgency, "Non "));
			urgencyText.setTextColor(getResources().getColor(
					R.color.darker_text));
		} else if (urgency < 3) {
			urgencyText.setText(String.format(strUrgency, "Less "));
			urgencyText.setTextColor(getResources().getColor(
					R.color.less_urgent));
		} else {
			urgencyText.setText(String.format(strUrgency, ""));
			urgencyText.setTextColor(getResources().getColor(R.color.urgent));
		}

		// Display isImproving
		TextView improvingText = (TextView)findViewById(R.id.improving);
		improvingText.setText(
				p.getIsImproving() ? "Improving" : "Not improving");

		// Display Vitals records
		List<Vitals> vitals = p.getVitals();
		if (!vitals.isEmpty()) {
			String vitalsText = "";
			for (Vitals v : vitals)
				vitalsText += String.format("\n%s\nTemperature: %s\u00B0C\n",
						v.getTime(), v.getTemperature()) +
						String.format("Blood pressure: %s mmHg / %s mmHg\n",
								v.getSystolicBP(), v.getDiastolicBP()) +
								String.format("Heart rate: %s bpm\n",
										v.getHeartRate());

			((TextView)findViewById(R.id.vitals)).setText(vitalsText);
		}

		// Display Symptoms records
		List<Symptoms> symptoms = p.getSymptoms();
		if (!symptoms.isEmpty()) {
			String symptomsText = "";
			for (Symptoms s : symptoms)
				symptomsText += String.format("\n%s\n%s\n", s.getTime(),
						s.getDescriptions());

			((TextView)findViewById(R.id.symptoms)).setText(symptomsText);
		}

		// Display Doctor visit records
		List<String> doctor = p.getTimeDoctor();
		if (!doctor.isEmpty()) {
			String doctorText = "";
			for (String s : doctor)
				doctorText += String.format("\n%s\n", s);

			((TextView)findViewById(R.id.doctor)).setText(doctorText);
		}

		// Display Prescription records
		List<Prescription> prescriptions = p.getPrescriptions();
		if (!prescriptions.isEmpty()) {
			String prescriptionsText = "";
			for (Prescription p : prescriptions)
				prescriptionsText += String.format(
						"\n%s\nName: %s\nInstructions: %s\n",
						p.getTime(), p.getMedicationName(),
						p.getInstructions());

			((TextView)findViewById(R.id.prescriptions)).setText(prescriptionsText);
		}
	}
}