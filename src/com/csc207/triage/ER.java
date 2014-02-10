package com.csc207.triage;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ER implements Serializable {

	/** Unique ID for serialization. */
	private static final long serialVersionUID = -6151895975255109519L;

	/** The nurses registered in this ER. */
	private static Map<String, Nurse> nurses;
	private static Map<String, Physician> physicians;

	/** The current user in this ER. */
	private static Nurse currentNurse;
	private static Physician currentPhysician;

	/** Returns true if the nurse is signed in to this ER. */
	public static boolean signedIn;

	/** The patients in this ER. */
	private static Map<String, Patient> patients;

	/** Singleton design pattern. */
	private static final ER INSTANCE = new ER();

	/**
	 * Constructs an instance of ER, instantiating the HashMaps of Nurse,
	 * Physicians, and Patients.
	 */
	private ER() {
		nurses = new HashMap<String, Nurse>();
		physicians = new HashMap<String, Physician>();
		patients = new HashMap<String, Patient>();
	}

	/**
	 * Returns the single instance of ER.
	 * @return the single instance of ER.
	 */
	public static ER getInstance() {
		return INSTANCE;
	}

	/**
	 * Adds a patient to this ER, given their health card number.
	 * @param patient The patient to be added to this ER.
	 */
	public void addPatient(Patient patient) {
		patients.put(patient.getID(), patient);
	}

	/**
	 * Returns a Patient from this ER, given their given and surnames.
	 * @param firstName The patient's given name(s).
	 * @param lastName The patient's surname.
	 * @return A Patient from this ER with the given name.
	 */
	public Patient getPatient(String firstName, String lastName) {
		for (Patient p : patients.values())
			if (p.getFirstName().equals(firstName) &&
					p.getLastName().equals(lastName))
				return p;
		return null;
	}

	/**
	 * Returns a Patient in this ER, given their health card number.
	 * @param healthCard The Patient's health card number.
	 * @return A Patient in this ER, given their health card number.
	 */
	public Patient getPatient(String healthCard) {
		return patients.get(healthCard);
	}

	/**
	 * Returns a list of Patients in this ER, sorted by their urgency.
	 * @param arrival If true, get patients by arrival time, else get by
	 * urgency level.
	 * @param unseen If true, only return the Patients that have not been seen
	 * by a doctor yet.
	 * @return A list of Patients in this ER, sorted by their urgency.
	 */
	public List<Patient> getPatients(boolean arrival, boolean unseen) {
		List<Patient> list = new ArrayList<Patient>();
		boolean first = true;

		for (Patient p : patients.values()){
			if (!unseen || unseen && p.getTimeDoctor().size() == 0) {
				if (first) {
					list.add(p);
					first = false;
				} else {
					int j = 0;
					if (arrival)
						while (j < list.size() &&
								p.getArrivalTime().compareTo(
										list.get(j).getArrivalTime()) > 0)
							j++;
					else
						while (j < list.size() && p.getUrgency() <
								list.get(j).getUrgency())
							j++;

					if (j < patients.size())
						list.add(j, p);
					else
						list.add(p);
				}
			}
		}
		return list;
	}

	/**
	 * Saves the Patients to the file.
	 * @throws IOException Thrown if there is an error with writing to the file.
	 */
	public void savePatients(FileIO<Patient> patientFile, String filePath)
			throws IOException {
		// Remove patients loaded when this object was created
		patientFile.clearPeopleList();

		// Add all Patients currently in ER
		for (Patient p : patients.values())
			patientFile.addPerson(p);

		// Save the file
		patientFile.save(filePath, false);
	}

	/**
	 * Adds a Nurse to this ER.
	 * @param nurse The Nurse to be added to this ER.
	 */
	public void addNurse(Nurse nurse) {
		nurses.put(nurse.getID(), nurse);
	}

	/**
	 * Returns the Map of Nurses in this ER.
	 * @return The Map of Nurses in this ER.
	 */
	public Map<String, Nurse> getNurses() {
		return nurses;
	}

	/**
	 * Returns the current Nurse in this ER.
	 * @return The current Nurse in this ER.
	 */
	public static Nurse getCurrentNurse() {
		return currentNurse;
	}

	/**
	 * Sets the current Nurse in this ER.
	 * @param currentNurse The current Nurse in this ER.
	 */
	public void setCurrentNurse(Nurse currentNurse) {
		ER.currentNurse = currentNurse;
	}

	/**
	 * Adds a Physician to this ER.
	 * @param physician The Physician to be added to this ER.
	 */
	public void addPhysician(Physician physician) {
		physicians.put(physician.getID(), physician);
	}

	/**
	 * Returns the map of Nurses in this ER.
	 * @return The Map of Nurses in this ER.
	 */
	public Map<String, Physician> getPhysicians() {
		return physicians;
	}

	/**
	 * Returns the current Physician in this ER.
	 * @return The current Physician in this ER.
	 */
	public static Physician getCurrentPhysician() {
		return currentPhysician;
	}

	/**
	 * Sets the current Physician in this ER.
	 * @param physician The current Physician in this ER.
	 */
	public void setCurrentPhysician(Physician physician) {
		ER.currentPhysician = physician;
	}

	/**
	 * Returns true iff a Nurse is signed in to this ER.
	 * @return True iff a Nurse is signed in to this ER.
	 */
	public boolean isSignedIn() {
		return signedIn;
	}

	/**
	 * Sets true iff a Nurse is signed in to this ER.
	 * @param signedIn The boolean representation of whether a Nurse is signed
	 * in or not.
	 */
	public void setSignedIn(boolean signedIn) {
		ER.signedIn = signedIn;
	}
}