package com.csc207.triage;

import java.io.Serializable;

public class Symptoms extends Record implements Serializable {

	/** Unique ID for serialization. */
	private static final long serialVersionUID = -2956492706919679414L;

	/** This Symptoms's descriptions. */
	private final String descriptions;

	/**
	 * Constructs a Symptomswith the descriptions of the Patient's symptoms.
	 * @param descriptions The descriptions of this Patient's symptoms.
	 */
	public Symptoms(String descriptions) {
		super();
		this.descriptions = descriptions;
	}

	/**
	 * Returns this Symptoms descriptions.
	 * @return This Symptoms descriptions.
	 */
	public String getDescriptions() {
		return descriptions;
	}

	/**
	 * Returns this Symptoms's string representation, in a CSV format.
	 * @return This Symptoms's string representation, in a CSV format.
	 */
	@Override
	public String toString() {
		return String.format("%s=%s", this.getTime(),
				this.getDescriptions().replace(",", "."));
	}
}