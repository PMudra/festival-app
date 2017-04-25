package com.example.drachim.festivalapp.utils;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;

/**
 * Created by Dr. Achim on 25.04.2017.
 */

public class ViewUtils {

    public void updateToolbarBackground(Activity activity, Toolbar toolbar,
                                               int alpha, String title, int color) {

        int defaultTitleTextColor = this.fetchAccentColor(activity);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(adjustAlpha(defaultTitleTextColor, alpha));
        int toolbarColor = adjustAlpha(color, alpha);
        int topColor = darkenColor(color, alpha / 255f);
        topColor = adjustAlpha(topColor, Math.max(125, alpha));
        int[] colors = {topColor, toolbarColor};
        toolbar.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors));
    }

    private int fetchAccentColor(Activity activity) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = activity.getApplicationContext().obtainStyledAttributes(typedValue.data, new int[] {android.R.attr.colorAccent });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    public static int adjustAlpha(int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static int darkenColor(int color, float factor) {
        final float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= factor;
        return Color.HSVToColor(hsv);
    }


}
