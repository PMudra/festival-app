package com.example.drachim.festivalapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.drachim.festivalapp.R;

public class InputParticipantDialogFragment extends DialogFragment {

    private String participantName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View viewInflated = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_input, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.tv_input_contact_name);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(viewInflated);
        builder.setTitle("New participant");

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                participantName = input.getText().toString();
                onButtonClick(which);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                onButtonClick(which);
            }
        });

        return builder.create();
    }

    private void onButtonClick(int which) {
        Log.d("TEST", "which: " + which);

        Callback callback;
        try {
            callback = (Callback) getTargetFragment();
        }
        catch (ClassCastException e) {
            Log.e(this.getClass().getSimpleName(), "Callback of this class must be implemented by target fragment!", e);
            throw e;
        }

        if (callback != null) {
            if (which == Dialog.BUTTON_POSITIVE) {
                callback.accept(participantName);
            } else {
                callback.cancel();
            }
            this.dismiss();
        }

    }

    interface Callback {
        void accept(String participantName);
        void cancel();
    }
}