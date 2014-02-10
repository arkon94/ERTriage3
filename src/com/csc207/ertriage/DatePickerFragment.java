package com.csc207.ertriage;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

/**
 * The date picker dialog used in the Add Patient activity.
 * Based on code from http://goo.gl/GxYXO4.
 */
@SuppressLint("NewApi")
public class DatePickerFragment extends DialogFragment
implements DatePickerDialog.OnDateSetListener {

	private final Calendar c = Calendar.getInstance();

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create and display a new instance of DatePickerDialog
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {}
}