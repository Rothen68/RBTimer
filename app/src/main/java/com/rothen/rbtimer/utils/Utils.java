package com.rothen.rbtimer.utils;

import java.util.List;

/**
 * Utility class
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

    public static String millisecondToStringTimerInSecond(long m)
    {
        int heure = (int) m / 3600000 ;
        int minute = (int) (m - 3600000 * heure) / 60000;
        int seconde = (int) (m - 3600000 * heure - 60000 * minute) /1000;
        String valeur = new String();
        if (heure < 10)
            valeur = valeur + "0";
        valeur = valeur + String.valueOf(heure) + " : ";

        if (minute < 10)
            valeur = valeur + "0";
        valeur = valeur + String.valueOf(minute) + " : ";

        if (seconde < 10)
            valeur = valeur + "0";
        valeur = valeur + String.valueOf(seconde) + " s ";

        return valeur;
    }


    /**
     * Utility class to convert a byte array to a hexadecimal string.
     *
     * see : CardReader google sample
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Utility class to convert a hexadecimal string to a byte string.
     *
     * see : CardReader google sample
     *
     * <p>Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     */
    public static byte[] HexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     *
     */

    public static boolean stringArrayContainsString(String[]list, String search)
    {
        for(String s : list)
        {
            if (s.equals(search))
                return true;
        }
        return false;
    }
}
