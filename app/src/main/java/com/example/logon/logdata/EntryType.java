package com.example.logon.logdata;

import androidx.annotation.NonNull;

public enum EntryType {
    MISC, DRUGS, FOOD, HUSTLE;

    public static EntryType fromString(String s) {
        switch (s) {
            case "HUSTLE":  return HUSTLE;
            case "DRUGS": return DRUGS;
            case "FOOD":  return FOOD;
            default:    return MISC;
        }
    }

    public static EntryType shift(EntryType et) {
        switch (et) {
            case MISC:  return DRUGS;
            case DRUGS: return FOOD;
            case FOOD:  return HUSTLE;
            default:    return MISC;
        }
    }

}
