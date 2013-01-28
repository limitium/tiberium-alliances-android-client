package com.cnc.CnCTA.helper;


import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Formater {
    static NumberFormat numberFormat = new DecimalFormat("#.##");

    public static String number(Number val) {
        return Formater.numberFormat.format(val);
    }
}
