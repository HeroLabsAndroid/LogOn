package com.example.logon.util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;

public class DateTimeSave {
    int y;
    int m;
    int d;
    int h;
    int min;
    int s;

    public DateTimeSave(LocalDate date, LocalTime time) {
        y = date.getYear();
        m = date.getMonthValue();
        d = date.getDayOfMonth();

        h = time.getHour();
        min = time.getMinute();
        s = time.getSecond();
    }

    public static DateTimeSave saveDateTime(LocalDateTime datetime) {
        return new DateTimeSave(datetime.toLocalDate(), datetime.toLocalTime());
    }

    public static LocalDateTime buildDateTimeFromSave(DateTimeSave save) {
        LocalDate date = LocalDate.of(save.y,save.m,save.d);
        LocalTime time = LocalTime.of(save.h,save.min,save.s);

        return LocalDateTime.of(date, time);
    }
}
