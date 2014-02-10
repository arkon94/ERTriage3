package com.csc207.ertriage;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.csc207.triage.ER;
import com.csc207.triage.FileIO;
import com.csc207.triage.Nurse;
import com.csc207.triage.Physician;
import com.csc207.triage.Professional;

public class LogInActivity extends Activity {

	/** The file in which to write nurse records. */
	private static final String FILENAME = "/passwords.txt";
	private static String FILEPATH;

	/** Password and dialog for registration */
	protected static final String ADMINPASS = "foobar";
	private Dialog register;

	/** The file manager for the Nurse and Physician users. */
	private FileIO<Professional> userFile;

	private static ER er = ER.getInstance();

	private String username, password;
	private TextView loginMessage, dMsg;
	private EditText userText, passText, dUser, dPass, dPass2, dAdmin;
	private RadioButton rbNurse, rbPhysician;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Hide ActionBar and use dark theme for login screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTheme(R.style.DarkTheme);
		setContentView(R.layout.activity_login);

		// Get the default filepath (root/data/data/com.csc207.ertriage/files/)
		FILEPATH = new File(this.getApplicationContext().getFilesDir(),
				FILENAME).getPath();

		loginMessage = (TextView)findViewById(R.id.incorrect);
		userText = (EditText)findViewById(R.id.username);
		passText = (EditText)findViewById(R.id.password);

		// Instantiate the file manager for nurses and fill in the database
		// from the file, if the file exists. Otherwise, create a new file to
		// save to.
		try {
			userFile = new FileIO<Professional>(
					this.getApplicationContext().getFilesDir(),
					FILENAME, new Professional());
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), R.string.file_error,
					Toast.LENGTH_SHORT).show();
		}

		// Handles the soft keyboard Sign in button
		passText.setOnEditorActionListener(
				new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						boolean handled = false;
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							signIn(passText);
							handled = true;
						}
						return handled;
					}
				});
	}

	/** Prevents users from going back to the main activity if not logged in */
	@Override
	public void onBackPressed() {}

	/**
	 * Checks the provided credentials and signs in the user if they are
	 * correct. If the credentials are incorrect, an error message is displayed.
	 * @param view The button View.
	 */
	public void signIn(View view) {
		username = userText.getText().toString();
		password = passText.getText().toString();

		// Check that the username is valid
		if (er.getNurses().containsKey(username) ||
				er.getPhysicians().containsKey(username)) {
			// Sign in the Nurse or Physician if the password is correct
			if (er.getNurses().containsKey(username) &&
					er.getNurses().get(username).getPassword().equals(
							password)) {
				er.setSignedIn(true);

				// Set the current user text in the main activity
				er.setCurrentNurse(er.getNurses().get(username));
				er.setCurrentPhysician(null);
				MainActivity.username = ER.getCurrentNurse().getUsername();

				this.finish();
			} else if (er.getPhysicians().containsKey(username) &&
					er.getPhysicians().get(username).getPassword().equals(
							password)) {
				er.setSignedIn(true);

				// Set the current user text in the main activity
				er.setCurrentPhysician(er.getPhysicians().get(username));
				er.setCurrentNurse(null);
				MainActivity.username = ER.getCurrentPhysician().getUsername();

				this.finish();
			} else { loginMessage.setText(R.string.wrong_pass); }
		} else { loginMessage.setText(R.string.user_not_found); }
	}

	/**
	 * Displays the registration dialog and handles the button click.
	 * @param view The button View.
	 */
	public void showRegisterDialog(View view) {
		// Show the dialog
		register = new Dialog(this);
		register.setContentView(R.layout.register_dialog);
		register.setTitle(R.string.register_prompt);

		rbNurse = (RadioButton)register.findViewById(R.id.radio_nurse);
		rbPhysician = (RadioButton)register.findViewById(R.id.radio_physician);

		dUser = (EditText)register.findViewById(R.id.username);
		dPass = (EditText)register.findViewById(R.id.password);
		dPass2 = (EditText)register.findViewById(R.id.password2);
		dAdmin = (EditText)register.findViewById(R.id.admin);
		dMsg = (TextView)register.findViewById(R.id.incorrect);

		// Handle the Register button click in the dialog
		Button dialogButton = (Button)register.findViewById(
				R.id.register_button);
		dialogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Get the textbox values
				final String strUser, strPass, strPass2, strAdmin;
				strUser = dUser.getText().toString();
				strPass = dPass.getText().toString();
				strPass2 = dPass2.getText().toString();
				strAdmin = dAdmin.getText().toString();
				// Call register method to deal with the values
				registerCheck(strUser, strPass, strPass2, strAdmin, dMsg);
			}
		});

		register.show();
	}

	/**
	 * Checks that the form is filled in correctly. If correct, register the
	 * user. Otherwise, display an appropriate error message.
	 * @param user The username to register.
	 * @param pass The password for the user to register.
	 * @param pass2 A confirmation of the password. This should match pass.
	 * @param admin The adminstrative password that must be entered to register
	 * new users.
	 * @param registerMsg The TextView used to display error messages.
	 */
	private void registerCheck(String user, String pass, String pass2,
			String admin, TextView registerMsg) {
		if (user.length() == 0 || pass.length() == 0 ||
				pass2.length() == 0 || admin.length() == 0) {
			// Check all fields are filled in
			registerMsg.setText(R.string.empty_fields);
		} else if (er.getNurses().get(user) != null ||
				er.getPhysicians().get(user) != null) {
			// Check that the username isn't already used
			registerMsg.setText(R.string.user_exists);
		} else if (!admin.equals(ADMINPASS)) {
			// Check that the admin password is correct
			registerMsg.setText(R.string.wrong_admin);
		} else if (!pass.equals(pass2)) {
			// Check that the passwords match
			registerMsg.setText(R.string.pass_mismatch);
		} else if (!rbNurse.isChecked() && !rbPhysician.isChecked()) {
			// Check that a user type is checked
			registerMsg.setText(R.string.choose_type);
		} else if (user.indexOf(",") > 0 || pass.indexOf(",") > 0) {
			registerMsg.setText(R.string.commas);
		} else {
			// If all checks passed, create the user and add to ER/file
			register(user, pass);
		}
	}

	/**
	 * Registers a user with the provided username and password.
	 * @param user The username for the new user.
	 * @param pass The password for the new user.
	 */
	private void register(String user, String pass) {
		try {
			if (rbNurse.isChecked()) {
				// Create and add a new Nurse
				Nurse newUser = new Nurse(user, pass);
				er.addNurse(newUser);

				userFile.addPerson(newUser);

				Toast.makeText(getApplicationContext(),
						String.format("Registered Nurse %s", user),
						Toast.LENGTH_SHORT).show();
			} else if (rbPhysician.isChecked()) {
				// Create and add a new Physician
				Physician newUser = new Physician(user, pass);
				er.addPhysician(newUser);

				userFile.addPerson(newUser);

				Toast.makeText(getApplicationContext(),
						String.format("Registered Physician %s", user),
						Toast.LENGTH_SHORT).show();
			}
			userFile.save(FILEPATH, true);
			register.dismiss();
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(),
					R.string.save_error,
					Toast.LENGTH_SHORT).show();
		}
	}
}