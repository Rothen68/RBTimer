package com.rothen.rbtimer.utils;

/**
 * Created by apest on 14/03/2017.
 */

public class Utils {

    public static String millisecondToStringTimer(long m)
    {
        int heure = (int) m / 3600000 ;
        int minute = (int) (m - 3600000 * heure) / 60000;
        int seconde = (int) (m - 3600000 * heure - 60000 * minute) /1000;
        int milli = (int) m - 3600000 * heure - 60000 * minute - 1000* seconde;
        String valeur = new String();
        if (heure < 10)
            valeur = valeur + "0";
        valeur = valeur + String.valueOf(heure) + " : ";

        if (minute < 10)
            valeur = valeur + "0";
        valeur = valeur + String.valueOf(minute) + " : ";

        if (seconde < 10)
            valeur = valeur + "0";
        valeur = valeur + String.valueOf(seconde) + " : ";

        if(milli < 100)
            valeur = valeur + "0";
        if(milli<10)
            valeur = valeur + "0";
        valeur = valeur + String.valueOf(milli);
        return valeur;
    }
}
