package com.example.drachim.festivalapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.drachim.festivalapp.R;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class FilterDialogFragment extends DialogFragment implements View.OnClickListener, DateDialogFragment.OnDateListener, SeekBar.OnSeekBarChangeListener {

    public static final String TAG = "filter_dialog";
    public static final String FILTER = "FILTER";
    private OnFilterListener onFilterListener;
    private View view;
    private Distance currentDistance;
    private Date currentFromDate;
    private Date currentToDate;

    public static class Filter implements Serializable {
        private Distance distance;
        private boolean sortByDate;
        private final Date fromDate;
        private final Date toDate;

        Filter(final Distance distance, final boolean sortByDate, final Date fromDate, final Date toDate) {
            this.distance = distance;
            this.sortByDate = sortByDate;
            this.fromDate = fromDate;
            this.toDate = toDate;
        }

        public Distance getDistance() {
            return distance;
        }

        public boolean isSortByDate() {
            return sortByDate;
        }

        public Date getFromDate() {
            return fromDate;
        }

        public Date getToDate() {
            return toDate;
        }
    }

    public interface OnFilterListener {
        void onFilterSet(Filter filter);
    }

    enum Distance {
        KM10(10),
        KM50(50),
        KM100(100),
        KM250(250),
        KM500(500),
        ANY(100000);

        private final int kilometres;

        Distance(int kilometres) {
            this.kilometres = kilometres;
        }

        static Distance fromProgress(int progress) {
            return Distance.values()[progress];
        }

        public int toProgress() {
            return java.util.Arrays.asList(Distance.values()).indexOf(this);
        }

        public int getKilometres() {
            return kilometres;
        }

        public String toString(Context context) {
            return this != ANY ? kilometres + " km" : context.getString(R.string.any_distance);
        }
    }

    static FilterDialogFragment newInstance(final Filter filter) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FilterDialogFragment.FILTER, filter);
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
        filterDialogFragment.setArguments(bundle);
        return filterDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Filter filter = (Filter) getArguments().get(FILTER);

        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_filter, null);

        // Date
        view.findViewById(R.id.fromDate).setOnClickListener(this);
        view.findViewById(R.id.toDate).setOnClickListener(this);
        onDateSet("from_date", filter.fromDate);
        onDateSet("to_date", filter.toDate);

        // Sort
        if(filter.sortByDate) {
            ((RadioButton) view.findViewById(R.id.radio_date)).setChecked(true);
        } else {
            ((RadioButton) view.findViewById(R.id.radio_distance)).setChecked(true);
        }

        // Distance
        ((SeekBar) view.findViewById(R.id.distance_seek_bar)).setOnSeekBarChangeListener(this);
        ((SeekBar) view.findViewById(R.id.distance_seek_bar)).setProgress(filter.distance.toProgress());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onFilterListener.onFilterSet(new Filter(currentDistance, ((RadioButton) view.findViewById(R.id.radio_date)).isChecked(), currentFromDate, currentToDate));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setTitle(R.string.filter);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onFilterListener = (OnFilterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFilterListener");
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
            onFilterListener = (OnFilterListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFilterListener");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fromDate:
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentFromDate);
                DateDialogFragment.newInstance(calendar).show(getFragmentManager(), "from_date");
                break;
            case R.id.toDate:
                final Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(currentToDate);
                DateDialogFragment.newInstance(calendar2).show(getFragmentManager(), "to_date");
                break;
        }
    }

    @Override
    public void onDateSet(final String tag, final Date date) {
        TextView textView;
        if (tag.equals("from_date")) {
            textView = ((TextView) view.findViewById(R.id.fromDate));
            currentFromDate = date;
        } else {
            textView = ((TextView) view.findViewById(R.id.toDate));
            currentToDate = date;
        }
        final DateFormat dateFormat = DateFormat.getDateInstance();
        textView.setText(dateFormat.format(date));
    }

    @Override
    public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
        currentDistance = Distance.fromProgress(progress);
        ((TextView) view.findViewById(R.id.distance_text)).setText(currentDistance.toString(getActivity()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
