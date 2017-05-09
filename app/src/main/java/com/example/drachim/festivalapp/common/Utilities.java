package com.example.drachim.festivalapp.common;

import android.graphics.Paint;
import android.widget.TextView;

/**
 * Created by Dr. Achim on 09.05.2017.
 */

public class Utilities {
    public static void strikeThru(TextView view, boolean strikeThru) {
        if (strikeThru) {
            view.setPaintFlags(view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            view.setPaintFlags(view.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}
