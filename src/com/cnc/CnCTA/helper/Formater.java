package com.cnc.CnCTA.helper;


import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Formater {
    static NumberFormat numberFormat = new DecimalFormat("#.##");
    static NumberFormat resourceFormat = new DecimalFormat("###,###.#");
    static NumberFormat resourceSmallFormat = new DecimalFormat("###,###");

    public static String number(Number val) {
        return Formater.numberFormat.format(val);
    }

    public static String resource(Number val) {
        String suffix = "";
        double value = val.doubleValue();
        NumberFormat formater = resourceSmallFormat;
        if (value > 100000) {
            value /= 1000;
            suffix = "k";
        }
        if (value > 100000) {
            value /= 1000;
            suffix = "M";
        }
        if (value > 100000) {
            value /= 1000;
            suffix = "B";
        }
        if (!suffix.isEmpty()) {
            formater = resourceFormat;
        }
        return formater.format(value) + suffix;
    }
}
