package com.example.drachim.festivalapp.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, DialogInterface.OnClickListener {

    private OnDateListener onDateListener;
    private static final String CALENDAR = "CALENDAR";

    private Calendar calendar;

    public interface OnDateListener {
        void onDateSet(final String tag, final Date date);
    }

    static DateDialogFragment newInstance(Calendar calendar) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(DateDialogFragment.CALENDAR, calendar);

        DateDialogFragment dateDialogFragment = new DateDialogFragment();
        dateDialogFragment.setArguments(bundle);

        return dateDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendar = ((Calendar) getArguments().get(CALENDAR));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)) {
            // Necessary to fix bug under API 19 with orientation change
            @Override
            protected void onStop() {
            }
        };

        datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Today", this);
        return datePickerDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onDateListener = (OnDateListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDateListener");
        }
    }

    /**
     * Necessary for devices older than API 23.
     * Alternatively one could use Fragment class from SupportLibrary.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onDateListener = (OnDateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDateListener");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        onDateListener.onDateSet(getTag(), calendar.getTime());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        final Calendar calendar = Calendar.getInstance();
        onDateListener.onDateSet(getTag(), calendar.getTime());
    }
}
