package com.example.drachim.festivalapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.activity.FestivalActivity;
import com.example.drachim.festivalapp.data.Festival;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FestivalLineupFragment extends Fragment {

    private TextView textView;
    private List<String> lineup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_festival_lineup, container, false);

        Festival festival = ((FestivalActivity) getActivity()).getFestival();
        lineup = festival.getLineup();
        textView = (TextView) view.findViewById(R.id.tv_lineup);
        initGui();

        return view;
    }

    private void initGui() {

        if (textView.getText().toString().isEmpty()) {

            Collections.sort(lineup, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });

            for (String artist : lineup) {
                // replace spaces, dashes etc. to non-breaking ones
                artist = artist.replace(" ", "\u00A0").replace("-", "\u2011");

                // add middle dot between artists as a seperator
                textView.append(artist + " \u00B7 ");
            }

            textView.post(new Runnable() {
                @Override
                public void run() {

                    String newLines = "";

                    for (int i = 0; i < textView.getLineCount(); i++) {

                        // get line
                        int start = textView.getLayout().getLineStart(i);
                        int end = textView.getLayout().getLineEnd(i);
                        String line = textView.getText().toString().substring(start, end);

                        // remove whitespaces and middle dot at line start
                        line = line.replaceFirst("^\\s?\\u00B7?\\s?", "");

                        // remove whitespaces and middle dot at line end
                        line = line.replaceFirst("\\s?\\u00B7?\\s?$", "\n");

                        newLines += line;
                    }

                    textView.setText(newLines);
                }
            });
        }
    }
}