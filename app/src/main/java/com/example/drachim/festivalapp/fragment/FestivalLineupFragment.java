package com.example.drachim.festivalapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.drachim.festivalapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Dr. Achim on 01.05.2017.
 */

public class FestivalLineupFragment extends Fragment {

    private TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_festival_lineup, container, false);
        textView = (TextView) view.findViewById(R.id.tv_lineup);

        initGui();

        return view;
    }

    private void initGui() {

        if (textView.getText().toString().isEmpty()) {
            List<String> lineupList = new ArrayList<>();
            lineupList.add("Adaro");
            lineupList.add("Adrenalize");
            lineupList.add("Angerfist");
            lineupList.add("Atmozfears");
            lineupList.add("Audiofreq");
            lineupList.add("Audiotricz");
            lineupList.add("Bass Modulators");
            lineupList.add("B-Front");
            lineupList.add("Brennan Heart");
            lineupList.add("Coone");
            lineupList.add("Da Tweekaz");
            lineupList.add("Delete");
            lineupList.add("Detox");
            lineupList.add("Devin Wild");
            lineupList.add("D-BLOCK & S-TE-FAN");
            lineupList.add("Dr. Peacock");
            lineupList.add("Endymion");
            lineupList.add("Frequencerz");
            lineupList.add("Frontliner");
            lineupList.add("Gunz 4 Hire");
            lineupList.add("Harddriver [Live]");
            lineupList.add("Hardwell");
            lineupList.add("Headhunterz");
            lineupList.add("Max Enforcer");
            lineupList.add("Miss K8");
            lineupList.add("Myst");
            lineupList.add("Neophyte");
            lineupList.add("Noisecontrollerz");
            lineupList.add("NSCLT");
            lineupList.add("Phuture Noize");
            lineupList.add("Psyko Punkz");
            lineupList.add("Ran-D");
            lineupList.add("Radical Redemption");
            lineupList.add("Rebourne");
            lineupList.add("Sound Rush");
            lineupList.add("Tatanka");
            lineupList.add("Villain");
            lineupList.add("Wildstylez");
            lineupList.add("Zatox");

            Collections.sort(lineupList, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });

            for (String artist : lineupList) {
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
