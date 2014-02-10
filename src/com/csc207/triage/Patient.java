package com.csc207.triage;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;

public class Patient implements Person<Patient>, Serializable {

	/** Unique ID for serialization. */
	private static final long serialVersionUID = -1724218736392866968L;

	/** This Patient's first name, last name, and date of birth. */
	private final String firstName, lastName, dob;

	/** This Patient's health card number. */
	private final String healthCard;

	/** This Patient's arrival time. */
	private String arrivalTime;

	/** This Patient's prescriptions. */
	private List<Prescription> prescriptions;

	/** This Patient's symptoms records. */
	private List<Symptoms> symptoms;

	/** This Patient's vitals records. */
	private List<Vitals> vitals;

	/** This Patient's urgency. */
	private Byte urgency;

	/** The times that this Patient has been seen by a doctor. */
	private List<String> timeDoctor;

	/** True if this Patient is improving. */
	private Boolean isImproving;

	/** Constructs a blank Patient, for use with FileIO. */
	public Patient() {
		this.firstName = null;
		this.lastName = null;
		this.dob = null;
		this.healthCard = null;
		this.arrivalTime = null;

		this.symptoms = new ArrayList<Symptoms>();
		this.vitals = new ArrayList<Vitals>();
		this.prescriptions = new ArrayList<Prescription>();
		this.urgency = 0;
		this.timeDoctor = new ArrayList<String>();
		this.isImproving = false;
	}

	/**
	 * Constructs a Patient with their name, date of birth, health card number,
	 * and arrival time. Initializes empty lists for symptoms, vitals, times
	 * visited by a doctor, urgency level, and isImproving.
	 * @param firstName This Patient's given name(s).
	 * @param lastName This Patient's surname.
	 * @param dob This Patient's date of birth.
	 * @param healthCard This Patient's health card number.
	 * @param arrivalTime This Patient's arrival time at the ER.
	 */
	public Patient(String firstName, String lastName, String dob,
			String healthCard, String arrivalTime) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.healthCard = healthCard;
		this.arrivalTime = arrivalTime;

		this.symptoms = new ArrayList<Symptoms>();
		this.vitals = new ArrayList<Vitals>();
		this.prescriptions = new ArrayList<Prescription>();
		this.urgency = 0;
		this.timeDoctor = new ArrayList<String>();
		this.isImproving = false;
	}

	/**
	 * Returns this Patient's ID (health card number).
	 * @return This Patient's ID (health card number).
	 */
	@Override
	public String getID() {
		return healthCard;
	}

	/**
	 * Returns this Patient's given name(s).
	 * @return This Patient's given name(s).
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns this Patient's surname.
	 * @return This Patient's surname.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns this Patient's date of birth.
	 * @return This Patient's date of birth.
	 */
	public String getDob() {
		return dob;
	}

	/**
	 * Returns this Patient's health card number.
	 * @return This Patient's health card number.
	 */
	public String getHealthCard() {
		return healthCard;
	}

	/**
	 * Returns this Patient's arrival time.
	 * @return This Patient's arrival time.
	 */
	public String getArrivalTime() {
		return arrivalTime;
	}

	/**
	 * Sets this Patient's arrival time.
	 * @param arrivalTime This Patient's arrival time.
	 */
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	/**
	 * Returns this Patient's symptoms.
	 * @return This Patient's symptoms.
	 */
	public List<Symptoms> getSymptoms() {
		return symptoms;
	}

	/**
	 * Adds symptoms to this Patient.
	 * @param symptoms The symptoms to add to this Patient's records.
	 */
	public void addSymptoms(Symptoms symptoms) {
		this.symptoms.add(0, symptoms);
		updateUrgencyImproving();
	}

	/**
	 * Returns this Patient's vitals.
	 * @return This Patient's vitals.
	 */
	public List<Vitals> getVitals() {
		return vitals;
	}

	/**
	 * Adds vitals to this Patient.
	 * @param vitals The vitals to add to this Patient's records.
	 */
	public void addVitals(Vitals vitals) {
		this.vitals.add(0, vitals);
		updateUrgencyImproving();
	}

	/**
	 * Returns this Patient's prescriptions.
	 * @return This Patient's prescriptions.
	 */
	public List<Prescription> getPrescriptions() {
		return prescriptions;
	}

	/**
	 * Adds a prescription to this Patient.
	 * @param prescription The prescription to add to this Patient's records.
	 */
	public void addPrescriptions(Prescription prescription) {
		this.prescriptions.add(0, prescription);
	}

	/** Updates this Patient's urgency level according to hospital policy. */
	public void updateUrgencyImproving() {
		if (!this.getVitals().isEmpty()) {
			byte urgency = 0;
			Vitals v = this.getVitals().get(0);

			// age < 2 years
			if (calculateAge(this.getDob()) < 2)
				urgency += 1;

			// Temp >= 39
			if (v.getTemperature() >= 39.0)
				urgency += 1;

			// BP: sys >= 140 or dia >= 90
			if (v.getSystolicBP() >= 140.0 || v.getDiastolicBP() >= 90.0)
				urgency += 1;

			// heart rate: >= 100 or <= 50
			if (v.getHeartRate() >= 100.0 || v.getHeartRate() <= 50)
				urgency += 1;

			byte prevUrgency = this.urgency;
			this.setUrgency(urgency);
			this.setIsImproving(!(prevUrgency < urgency));
		}
	}

	/**
	 * Calculates and returns this Patient's age relative to the current date.
	 * @param dob This Patient's date of birth.
	 * @return This Patient's age relative to the current date.
	 */
	@SuppressLint("SimpleDateFormat")
	private int calculateAge(String dob) {
		int year, month, day, yearDob, monthDob, dayDob, age;

		// Date of birth
		yearDob = Integer.parseInt(dob.substring(0, 4));
		monthDob = Integer.parseInt(dob.substring(5, 7));
		dayDob = Integer.parseInt(dob.substring(8, 10));

		// Today's date
		SimpleDateFormat dateFormat;
		Date date = new Date();

		dateFormat = new SimpleDateFormat("yyyy");
		year = Integer.parseInt(dateFormat.format(date));
		dateFormat = new SimpleDateFormat("MM");
		month = Integer.parseInt(dateFormat.format(date));
		dateFormat = new SimpleDateFormat("dd");
		day = Integer.parseInt(dateFormat.format(date));

		// Calculate age
		age = year - yearDob;

		// Birthday checks
		if(month < monthDob)
			--age;
		if(month == monthDob && day < dayDob)
			--age;

		return age;
	}

	/**
	 * Returns this Patient's urgency level.
	 * @return This Patient's urgency level.
	 */
	public Byte getUrgency() {
		return urgency;
	}

	/**
	 * Sets this Patient's urgency level.
	 * @param urgency This Patient's urgency level.
	 */
	public void setUrgency(Byte urgency) {
		this.urgency = urgency;
	}

	/**
	 * Returns a list of the times this Patient's has been seen by a doctor.
	 * @return A list of the times this Patient's has been seen by a doctor.
	 */
	public List<String> getTimeDoctor() {
		return timeDoctor;
	}

	/**
	 * Adds this a time that this Patient was seen by a doctor.
	 * @param timeDoctor A time that this Patient was seen by a doctor to add
	 * to this Patient's records.
	 */
	public void addTimeDoctor(String timeDoctor) {
		this.timeDoctor.add(0, timeDoctor);
	}

	/**
	 * Returns true iff this Patient is improving.
	 * @return True iff this Patient is improving.
	 */
	public Boolean getIsImproving() {
		return isImproving;
	}

	/**
	 * Sets this Patient's condition to true if they are improving, and false
	 * otherwise.
	 * @param isImproving This Patient's improving condition. This is true if
	 * they are improving, and false otherwise.
	 */
	public void setIsImproving(boolean isImproving) {
		this.isImproving = isImproving;
	}

	/**
	 * Parses the contents of fields and instantiates a Patient object. It is
	 * then added to the ER database and returned.
	 * @param fields An array of contents used to instantiate the object.
	 * @return The instantiated Person object.
	 */
	@Override
	public Patient scan(String[] fields) {
		Patient patient = new Patient(fields[0], fields[1], fields[2],
				fields[3], fields[4]);
		String[] subset = new String[] { fields[5], fields[6], fields[7],
				fields[8], fields[9], fields[10] };
		scanRecords(patient, subset);
		ER.getInstance().addPatient(patient);
		return patient;
	}

	/**
	 * Parses the fields that contain this Patient's records and adds it to
	 * the Patient object.
	 * @param patient The Patient to add the records to.
	 * @param fields The records to add to the Patient.
	 */
	private void scanRecords(Patient patient, String[] fields) {
		// symptoms
		if (fields[0].indexOf(';') > 1) {
			for (String s : fields[0].split(";")) {
				String[] symptom = s.split("=");
				Symptoms newSymptoms = new Symptoms(
						symptom[1].replace(".", ","));
				newSymptoms.setTime(symptom[0]);
				patient.addSymptoms(newSymptoms);
			}
		}

		// vitals
		if (fields[1].indexOf(';') > 1) {
			for (String v : fields[1].split(";")) {
				String[] vital = v.split("=");
				String[] numbers = vital[1].split(",");
				Vitals newVitals = new Vitals(Double.parseDouble(numbers[0]),
						Double.parseDouble(numbers[1]),
						Double.parseDouble(numbers[2]),
						Double.parseDouble(numbers[3]));
				newVitals.setTime(vital[0]);
				patient.addVitals(newVitals);
			}
		}

		// urgency
		patient.setUrgency(Byte.valueOf(fields[2]));

		// isImproving
		patient.setIsImproving(Boolean.parseBoolean(fields[3]));

		// timeDoctor
		if (fields[4].indexOf(';') > 1)
			for (String t : fields[4].split(";"))
				patient.addTimeDoctor(t);

		// prescriptions
		if (fields[5].indexOf(';') > 1) {
			for (String p : fields[5].split(";")) {
				String[] prescription = p.split("=");
				String[] parts = prescription[1].split(",");
				Prescription newPrescription= new Prescription(
						parts[0].replace(".", ","),
						parts[1].replace(".", ","));
				newPrescription.setTime(prescription[0]);
				patient.addPrescriptions(newPrescription);
			}
		}
	}

	/**
	 * Returns this Patient's string representation, that includes of all of the
	 * Patient's information and records, in CSV format.
	 * @return This Patient's string representation, in CSV format.
	 */
	@Override
	public String toString() {
		// Symptoms records, from oldest to newest
		String symptomsList = "";
		if (!this.getSymptoms().isEmpty())
			for (Symptoms s : this.getSymptoms())
				symptomsList = s.toString() + ";" + symptomsList;
		else
			symptomsList = " ";

		// Vitals records, from oldest to newest
		String vitalsList = "";
		if (!this.getVitals().isEmpty())
			for (Vitals v : this.getVitals())
				vitalsList = v.toString() +";" + vitalsList;
		else
			vitalsList = " ";

		// Times seen by doctor, from oldest to newest
		String timeDoctorList = "";
		if (!this.getTimeDoctor().isEmpty())
			for (String t : this.getTimeDoctor())
				timeDoctorList = t + ";" + timeDoctorList;
		else
			timeDoctorList = " ";

		// Prescription records, from oldest to newest
		String prescriptionList = "";
		if (!this.getPrescriptions().isEmpty())
			for (Prescription p : this.getPrescriptions())
				prescriptionList = p.toString() + ";" + prescriptionList;
		else
			prescriptionList = " ";

		return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s",
				this.firstName, this.lastName, this.dob, this.healthCard,
				this.arrivalTime, symptomsList, vitalsList, this.urgency,
				this.isImproving, timeDoctorList, prescriptionList);
	}
}