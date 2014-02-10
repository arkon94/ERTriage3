package com.csc207.triage;

import java.io.Serializable;

public class Prescription extends Record implements Serializable {

	/** Unique ID for serialization. */
	private static final long serialVersionUID = 3482145881605969967L;

	/** This Prescription's medication name. */
	private final String medicationName;

	/** This Prescription's instructions. */
	private final String instructions;

	/**
	 * Constructs a Prescriptionwith the name of the medication name and its
	 * instructions.
	 * @param medication This Prescription's medication name.
	 * @param instructions This Prescription's instructions.
	 */
	public Prescription(String medication, String instructions) {
		super();
		this.medicationName = medication;
		this.instructions = instructions;
	}

	/**
	 * Returns this Prescription's medication name.
	 * @return This Prescription's medication name.
	 */
	public String getMedicationName() {
		return medicationName;
	}

	/**
	 * Returns this Prescription's instructions.
	 * @return This Prescription's instructions.
	 */
	public String getInstructions() {
		return instructions;
	}

	/**
	 * Returns this Prescription's string representation, in a CSV format.
	 * @return This Prescription's string representation, in a CSV format.
	 */
	@Override
	public String toString() {
		return String.format("%s=%s,%s", this.getTime(),
				this.getMedicationName(),
				this.getInstructions().replace(",", "."));
	}
}