package com.csc207.ertriage;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.csc207.triage.ER;
import com.csc207.triage.Patient;
import com.csc207.triage.Vitals;

public class VitalsActivity extends ActionBarActivity {

	private String healthCard;
	private Patient p;
	private EditText temp, sys, dia, heart;

	private ER er = ER.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vitals);
		setupActionBar();

		// Get the health card number that was passed into this Intent
		healthCard = (String)getIntent().getSerializableExtra("patient");

		// Gets the actual Patient object to modify
		p = er.getPatient(healthCard);

		temp = (EditText)findViewById(R.id.temperature);
		sys = (EditText)findViewById(R.id.systolic);
		dia = (EditText)findViewById(R.id.diastolic);
		heart = (EditText)findViewById(R.id.heart_rate);

		// Display previous vitals, if they exist
		if (!p.getVitals().isEmpty()) {
			Vitals prevVitals = p.getVitals().get(0);
			temp.append(String.valueOf(prevVitals.getTemperature()));
			sys.append(String.valueOf(prevVitals.getSystolicBP()));
			dia.append(String.valueOf(prevVitals.getDiastolicBP()));
			heart.append(String.valueOf(prevVitals.getHeartRate()));
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar
		getMenuInflater().inflate(R.menu.vitals, menu);
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
			addVitals();
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** Adds the Vitals to the Patient's records. */
	private void addVitals() {
		double newTemp = Double.parseDouble(temp.getText().toString());
		double newSys = Double.parseDouble(sys.getText().toString());
		double newDia = Double.parseDouble(dia.getText().toString());
		double newHeart = Double.parseDouble(heart.getText().toString());

		p.addVitals(new Vitals(newTemp, newSys, newDia, newHeart));
	}
}