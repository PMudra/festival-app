package com.example.drachim.festivalapp.data.sqlite;

import com.example.drachim.festivalapp.data.Festival;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public final class FestivalExampleData {
    public Festival[] getFestivals() {
        return new Festival[] {
            createTanteMiaTanzt()
        };
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
        return festival;
    }

    private static Date createDate(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, date);
        return calendar.getTime();
    }
}
