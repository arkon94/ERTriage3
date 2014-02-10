package com.csc207.triage;

public interface Person<T> {

	/**
	 * Returns this Person's ID.
	 * @return This Person's ID.
	 */
	public String getID();

	/**
	 * Parses the contents of fields and instantiates a Person object, then
	 * returns the Person.
	 * @param fields An array of contents used to instantiate the object.
	 * @return The instantiated Person object.
	 */
	public T scan(String[] fields);

	/**
	 * Returns this Person's string representation, in CSV format.
	 * @return This Person's string representation, in CSV format.
	 */
	@Override
	public String toString();

}
