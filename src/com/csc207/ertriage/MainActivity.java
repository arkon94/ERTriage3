package com.csc207.ertriage;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.csc207.triage.ER;
import com.csc207.triage.FileIO;
import com.csc207.triage.Patient;

public class MainActivity extends ActionBarActivity implements Serializable,
ActionBar.OnNavigationListener {

	/** Unique ID for serialization */
	private static final long serialVersionUID = 7562734131322541014L;

	/** The file in which to write patient records. */
	public static final String FILENAME = "/patientlist.csv";
	private static String FILEPATH;

	/** Sort options */
	private static boolean sortArrival = false;
	private static boolean sortUnseen = true;

	/** The file manager for the Patients */
	private FileIO<Patient> patientsFile;

	private static ListView patientsListView;
	static TextView type, user;
	static String username;
	private List<Patient> patientsList;

	private ER er = ER.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Display log in page if not signed in
		if (!er.isSignedIn())
			showActivity(LogInActivity.class);

		// Get the default filepath (root/data/data/com.csc207.ertriage/files/)
		FILEPATH = new File(this.getApplicationContext().getFilesDir(),
				FILENAME).getPath();

		// Instantiate a file manager for the list of patients.
		// Read the file if it exists, otherwise create a new one.
		try {
			patientsFile = new FileIO<Patient>(
					this.getApplicationContext().getFilesDir(),
					FILENAME, new Patient());
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(),
					R.string.file_error, Toast.LENGTH_SHORT).show();
		}

		setupSort();
		setupPatientsList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (ER.getCurrentNurse() == null) {
			menu.findItem(R.id.action_new).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			showActivity(SearchActivity.class);
			return true;
		case R.id.action_new:
			showActivity(AddPatientActivity.class);
			return true;
		case R.id.action_save:
			if (savePatients())
				Toast.makeText(getApplicationContext(), R.string.patients_saved,
						Toast.LENGTH_SHORT).show();
			return true;
		default:
			return false;
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onResume() {
		super.onResume();
		supportInvalidateOptionsMenu();
		updateUserText();
		// Re-populate the patients list when the activity resumes
		populateList(er.getPatients(sortArrival, sortUnseen));
		// Save file
		savePatients();
	}

	@Override
	public boolean onNavigationItemSelected(int position, long itemID) {
		if (itemID == 0) {
			sortArrival = false;
			sortUnseen = true;
		} else if (itemID == 1) {
			sortArrival = true;
			sortUnseen = true;
		} else if (itemID == 2) {
			sortArrival = false;
			sortUnseen = false;
		} else if (itemID == 3) {
			sortArrival = true;
			sortUnseen = false;
		}
		populateList(er.getPatients(sortArrival, sortUnseen));
		return false;
	}

	private void setupSort() {
		// Show sort options in an ActionBar spinner
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Populate dropdown with values from arrays
		String[] sort = getResources().getStringArray(R.array.sort);
		String[] sortMap = getResources().getStringArray(R.array.sort_map);

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sort.length; i+=2) {
			Map<String, String> item = new HashMap<String, String>();
			for (int j = i; j < i + 2; j++)
				item.put(sortMap[j], sort[j]);
			data.add(item);
		}

		// Show the sort options in the spinner
		SimpleAdapter sortAdapter = new SimpleAdapter(this, data,
				R.layout.actionbar_spinner_item,
				new String[] {"1", "2"},
				new int[] {R.id.text1, R.id.text2});

		sortAdapter.setDropDownViewResource(
				R.layout.actionbar_spinner_dropdown_item);

		actionBar.setListNavigationCallbacks(sortAdapter, this);
	}

	private void setupPatientsList() {
		patientsListView = (ListView)findViewById(R.id.patients);

		// Display a message if there's no patients in the database
		patientsListView.setEmptyView(findViewById(R.id.empty_list));

		// Make ListView items open an activy with the patient info
		patientsListView.setClickable(true);
		patientsListView.setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent patientIntent = new Intent(MainActivity.this,
								PatientActivity.class);
						Patient p = patientsList.get(
								(int) parent.getItemIdAtPosition(
										position));
						patientIntent.putExtra("patient", p.getHealthCard());
						startActivity(patientIntent);
					}
				});

		// Populate the ListView with all patients (sorted by arrival time)
		populateList(er.getPatients(sortArrival, sortUnseen));
	}

	public void updateUserText() {
		type = (TextView)findViewById(R.id.loggedInAs);
		type.setText(ER.getCurrentNurse() == null ? "Physician" : "Nurse");

		if (ER.getCurrentNurse() != null)
			username = ER.getCurrentNurse().getUsername();
		else if (ER.getCurrentPhysician() != null)
			username = ER.getCurrentPhysician().getUsername();

		user = (TextView)findViewById(R.id.user);
		user.setText(username);
	}

	public void signOut(View view) {
		showActivity(LogInActivity.class);
		er.setSignedIn(false);
	}

	private boolean savePatients() {
		try {
			er.savePatients(patientsFile, FILEPATH);
			return true;
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), R.string.save_error,
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	/**
	 * Starts and displays the specified activity.
	 * @param activity The activity to display.
	 */
	public void showActivity(Class<?> activity) {
		Intent intent = new Intent(this, activity);
		startActivity(intent);
	}

	/**
	 * Populates the ListView with the patients list.
	 * @param patients The list of patients to display.
	 */
	public void populateList(List<Patient> patients) {
		patientsList = patients;

		// Based on code from http://goo.gl/Y9ItQo

		// A list of data for the adapter
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		// Iterate through each patient in the list and add it to the list
		for (Patient p : patients) {
			// Maps used for patient data to show in the list
			Map<String, String> patient = new HashMap<String, String>();

			// Name (Last name, First name(
			patient.put("1", String.format("%s, %s",
					p.getLastName(), p.getFirstName()));

			// Arrival time, urgency, and a note if the patient has been seen
			// by a doctor already
			patient.put("2", String.format("Arrived at: %s / Urgency: %s%s",
					p.getArrivalTime(), String.valueOf(p.getUrgency()),
					p.getTimeDoctor().isEmpty() ? "" : " / Seen by doctor"));

			data.add(patient);
		}

		// Show the data on the ListView!
		SimpleAdapter adapter = new SimpleAdapter(this, data,
				android.R.layout.simple_list_item_2,
				new String[] {"1", "2"},
				new int[] {android.R.id.text1, android.R.id.text2});

		patientsListView.setAdapter(adapter);
	}
}