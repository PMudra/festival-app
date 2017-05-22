package com.example.drachim.festivalapp.data.sqlite;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.data.Festival;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public final class FestivalExampleData {
    public Festival[] getFestivals() {
        return new Festival[]{
                createQontinent(),
                createTanteMiaTanzt(),
                createDefqon(),
                createMysteryland()
        };
    }

    private static Festival createQontinent() {
        Festival festival = new Festival();
        festival.setName("The Qontinent");
        festival.setDescription("On 11-12-13 August 2017 Bass Events and Q-dance present The Qontinent - Decade of Dedication. An extra spectacular 10th anniversary weekend.");
        festival.setCountry("bg");
        festival.setPlace("Provinciaal domein Puyenbroeck");
        festival.setPostalCode("9185");
        festival.setStreet("Puyenbrug 1a");
        festival.setStartDate(createDate(2017, 8, 11));
        festival.setEndDate(createDate(2017, 8, 13));
        // unvollständig
        festival.setLineup(Arrays.asList("Adrenalize", "Audiofreq", "B-Front", "Brennan Heart", "Hard Driver", "Mandy", "Paul Elstak", "Crisis Era", "Refuzion", "Atmosfears", "Audiotricz", "Code Black", "Cyber", "Da Tweekaz", "Gunz For Hire [Live]", "Radical Redemption", "Rebourne", "Sephix", "Sub Zero Project", "Wasted Penguinz", "Angerfist [Live]", "Decypher", "Adaro", "Crypsis", "Deetox", "Delete", "Phuture Noize"));
        festival.setProfileImage(R.drawable.festival_qontinent_profile);
        festival.setTitleImage(R.drawable.festival_qontinent_title);
        return festival;
    }

    private static Festival createTanteMiaTanzt() {
        Festival festival = new Festival();
        festival.setName("Tante Mia tanzt");
        festival.setDescription("Tante Mia tanzt am 25. Mai 2017 mit all den Besuchern, ihren Nichten und Neffen, die elektronische Musik und ausgelassene Festival-Stimmung lieben. Ihre Türen sind ab 12 Uhr für alle ab 18 Jahren geöffnet. Nach der grandiosen ersten Ausgabe freut sich Tante Mia Euch auch 2017 wiederzusehen oder neu kennenzulernen.");
        festival.setCountry("de");
        festival.setPlace("Stoppelmarkt Vechta");
        festival.setPostalCode("49377");
        festival.setStreet("Stoppelmarkt");
        festival.setStartDate(createDate(2017, 5, 25));
        festival.setEndDate(createDate(2017, 5, 25));
        // unvollständig
        festival.setLineup(Arrays.asList("Yellow Claw", "Icona Pop", "Firebeatz", "DJ Juicy M", "Mark Bale", "Housedestroyer"));
        festival.setProfileImage(R.drawable.festival_tantemia_profile);
        festival.setTitleImage(R.drawable.festival_tantemia_title);
        return festival;
    }

    private static Festival createDefqon() {
        Festival festival = new Festival();
        festival.setName("Defqon.1");
        festival.setDescription("Weekend Warriors! We stand on the verge of a memorable milestone. This summer, our tribe reaches its fifteen-year existence. The moment to pay tribute to the legacy has come.");
        festival.setCountry("nl");
        festival.setPlace("Evenemententerrein Biddinghuizen");
        festival.setPostalCode("8256");
        festival.setStreet("Spijkweg 30");
        festival.setStartDate(createDate(2017, 6, 23));
        festival.setEndDate(createDate(2017, 6, 26));
        // unvollständig
        festival.setLineup(Arrays.asList("Frequencerz", "Brennan Heart & Zatox", "NCBM a.k.a. Noisecontrollers & Bass Modulators", "Atmozfears", "Da Tweekaz", "Psyko Punkz", "TNT a.k.a. Technoboy & Tuneboy", "Audiotricz", "Cyber", "Sound Rush", "Adrenalize"));
        festival.setProfileImage(R.drawable.festival_defqon_profile);
        festival.setTitleImage(R.drawable.festival_defqon_title);
        return festival;
    }

    private static Festival createMysteryland() {
        Festival festival = new Festival();
        festival.setName("Mysteryland");
        festival.setDescription("Guided by the rhythm of the drum, we escape into a mysterious world of endless possibilities...");
        festival.setCountry("nl");
        festival.setPlace("Haarlemmermeer");
        festival.setPostalCode("2131");
        festival.setStreet("Paviljoenlaan 1");
        festival.setStartDate(createDate(2017, 8, 26));
        festival.setEndDate(createDate(2017, 8, 27));
        // unvollständig
        festival.setLineup(Arrays.asList("Deadmau5", "Alesso", "Alok", "Broederliefde", "Charming Horses", "Craig David presents TS5", "Hannah Wants ", "KSHMR", "Showtek", "Tinlicker", "Yellow Claw", "Armin van Buuren", "Axwell Λ Ingrosso", "Benny Rodrigues", "Digital Farm Animals", "Made in June", "Oliver Heldens", "Sam Feldt (live)", "Sunnery James & Ryan Marciano"));
        festival.setProfileImage(R.drawable.festival_mysteryland_profile);
        festival.setTitleImage(R.drawable.festival_mysteryland_title);
        return festival;
    }

    private static Date createDate(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, date);
        return calendar.getTime();
    }
}
