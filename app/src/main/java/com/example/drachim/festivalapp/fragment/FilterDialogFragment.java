package com.example.drachim.festivalapp.fragment;

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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class FilterDialogFragment extends DialogFragment implements View.OnClickListener, DateDialogFragment.OnDateListener, SeekBar.OnSeekBarChangeListener {

    public static String tag = "filter_dialog";

    enum Distance {
        KM1(1),
        KM10(10),
        KM50(50),
        KM100(100),
        KM500(500),
        ANY(Integer.MAX_VALUE);

        private int kilometres;

        Distance(int kilometres) {
            this.kilometres = kilometres;
        }

        static Distance fromProgress(int progress) {
            return Distance.values()[progress];
        }

        public String toString(Context context) {
            return this != ANY ? kilometres + " km" : context.getString(R.string.any_distance);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_filter, null);

        Spinner spinner = (Spinner) view.findViewById(R.id.genre_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.music_genres, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        view.findViewById(R.id.fromDate).setOnClickListener(this);
        view.findViewById(R.id.toDate).setOnClickListener(this);
        ((SeekBar) view.findViewById(R.id.distance_seek_bar)).setOnSeekBarChangeListener(this);

        ((RadioButton) view.findViewById(R.id.radio_date)).setChecked(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fromDate:
                final Calendar calendar = Calendar.getInstance();
                calendar.set(2017, 5, 14);
                DateDialogFragment.newInstance(calendar).show(getFragmentManager(), "from_date");
                break;
            case R.id.toDate:
                final Calendar calendar2 = Calendar.getInstance();
                calendar2.set(2017, 5, 14);
                DateDialogFragment.newInstance(calendar2).show(getFragmentManager(), "to_date");
                break;
        }
    }

    @Override
    public void onDateSet(final String tag, final Date date) {
        TextView textView;
        if (tag.equals("from_date")) {
            textView = ((TextView) getDialog().findViewById(R.id.fromDate));
        } else {
            textView = ((TextView) getDialog().findViewById(R.id.toDate));
        }
        final DateFormat dateFormat = DateFormat.getDateInstance();
        textView.setText(dateFormat.format(date));
    }

    @Override
    public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
        ((TextView) getDialog().findViewById(R.id.distance_text)).setText(Distance.fromProgress(progress).toString(getActivity()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
