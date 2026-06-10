package com.caeproject.cae.application.utils;

public class ValidacionContrasena {

    public static boolean esValida(String contrasena) {
        if (contrasena == null || contrasena.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasNum = false;
        boolean hasSymbol = false;

        for (char c : contrasena.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasNum = true;
            } else if (!Character.isWhitespace(c)) {
                hasSymbol = true;
            }
        }

        return hasUpper && hasLower && hasNum && hasSymbol;
    }
}
