package com.example.logon.logdata;

import com.example.logon.util.DateTimeSave;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;

public class SaveEntry {
    DateTimeSave datetime;
    String type;
    String msg;

    public SaveEntry(DateTimeSave datetime, String type, String msg) {
        this.datetime = datetime;
        this.type = type;
        this.msg = msg;
    }

    public LogEntry fromSave() {
        switch (type) {
            case "DRUGS":   return new DrugLogEntry(DateTimeSave.buildDateTimeFromSave(datetime), msg);
            case "FOOD":    return new FoodLogEntry(DateTimeSave.buildDateTimeFromSave(datetime), msg);
            case "HUSTLE":  return new HustLogEntry(DateTimeSave.buildDateTimeFromSave(datetime), msg);
            default:        return new MiscLogEntry(DateTimeSave.buildDateTimeFromSave(datetime), msg);
        }

    }
}
