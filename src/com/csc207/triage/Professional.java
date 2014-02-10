package com.csc207.triage;

import java.io.Serializable;
import java.util.List;

/** A medical professional. */
public class Professional implements Person<Professional>, Serializable {

	/** Unique ID for serialization. */
	private static final long serialVersionUID = -3554115802093144180L;

	/** This Professional's username and password for the app. */
	private final String username, password;

	/** The ER that we're interacting with. */
	private ER er = ER.getInstance();

	/** Constructs a blank Professional, for use with FileIO. */
	public Professional() {
		this.username = null;
		this.password = null;
	}

	/**
	 * Constructs a Professional with username and password.
	 * @param username This Professional's username.
	 * @param password This Professional's password.
	 */
	public Professional(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * Gets this Professional's ID (username), for use with the Person
	 * interface.
	 * @return This Professional's ID (username).
	 */
	@Override
	public String getID() {
		return username;
	}

	/**
	 * Returns this Professional's username.
	 * @return This Professional's username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns this Professional's password.
	 * @return This Professional's password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns a Patient, given their given and surnames.
	 * @param firstName The Patient's given name(s).
	 * @param lastName The Patient's surname.
	 * @return A Patient with the given given and surnames, if they are in
	 * the records. Otherwise, return null.
	 */
	public Patient getPatient(String firstName, String lastName) {
		return er.getPatient(firstName, lastName);
	}

	/**
	 * Returns a Patient, given their health card number.
	 * @param healthCard The Patient's health card number.
	 * @return A Patient with the given health card number, if they are in the
	 * records. Otherwise, return null.
	 */
	public Patient getPatient(String healthCard) {
		return er.getPatient(healthCard);
	}

	/**
	 * Returns a list of Patients sorted by their urgency levels.
	 * @param unseen If true, only return the Patients that have not been seen
	 * by a doctor yet.
	 * @return A list of Patients sorted by their urgency levels.
	 */
	public List<Patient> getPatientsByUrgency(boolean arrival, boolean unseen) {
		return er.getPatients(arrival, unseen);
	}

	/**
	 * Returns a list of Patients sorted by their arrival times.
	 * @param unseen If true, only return the Patients that have not been seen
	 * by a doctor yet.
	 * @return A list of Patients sorted by their arrival times.
	 */
	public List<Patient> getPatientsByArrival(boolean arrival, boolean unseen) {
		return er.getPatients(arrival, unseen);
	}

	/**
	 * Parses the contents of fields and instantiates either a Nurse or a
	 * Physician depending on the contents. The created user object is then
	 * added to the ER database and returned.
	 * @param fields An array of contents used to instantiate a Nurse or
	 * Physician.
	 * @return The instantiated Nurse or Physician.
	 */
	@Override
	public Professional scan(String[] fields) {
		String type = fields[0];
		String[] otherFields = new String[] { fields[1], fields[2] };

		if (type.equals("n")) {
			// If the first item is "n", create a Nurse and return it
			Nurse nurse = new Nurse();
			return nurse.scan(otherFields);
		} else if (type.equals("p")) {
			// If the first item is "p", create a Physician and return it
			Physician physician = new Physician();
			return physician.scan(otherFields);
		} else {
			return null;
		}
	}

	/**
	 * Returns this Professional's string representation, that includes their
	 * username and password, in CSV format.
	 * @return This Professional's string representation, in CSV format.
	 */
	@Override
	public String toString() {
		return String.format("%s, %s", this.getUsername(), this.getPassword());
	}
}