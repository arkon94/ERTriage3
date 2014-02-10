package com.csc207.triage;

import java.io.Serializable;

public class Physician extends Professional implements Serializable {

	/** Unique ID for serialization. */
	private static final long serialVersionUID = 8886954997766909945L;

	/** The ER that we're interacting with */
	private ER er = ER.getInstance();

	/** Constructs a blank Physician, for use with FileIO. */
	public Physician() {
		super();
	}

	/**
	 * Constructs a Physician with username and password.
	 * @param username This Physician's username.
	 * @param password This Physician's password.
	 */
	public Physician(String username, String password) {
		super(username, password);
	}

	/**
	 * Records a Patient's Prescriptions.
	 * @param patient A Patient with Prescriptions being recorded.
	 * @param name The name of the Prescriptions object.
	 * @param instructions The Prescriptions object's instructions.
	 */
	public void recordPrescription(Patient patient, String name,
			String instructions) {
		patient.addPrescriptions(new Prescription(name, instructions));
	}

	/**
	 * Parses the contents of fields and instantiates this Physician. It
	 * is then added to the ER database and returned.
	 * @param fields An array of contents used to instantiate this Physician.
	 * @return The instantiated Physician.
	 */
	@Override
	public Physician scan(String[] fields) {
		Physician physician = new Physician(fields[0], fields[1]);
		er.addPhysician(physician);
		return physician;
	}

	/**
	 * Returns the String representation of this Physician, in CSV format.
	 * @return The String representation of this Physician, in CSV format.
	 */
	@Override
	public String toString() {
		return "p, " + super.toString();
	}
}
