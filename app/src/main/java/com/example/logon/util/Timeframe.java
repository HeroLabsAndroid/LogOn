package com.example.logon.util;

import android.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Timeframe {
    private Pair<LocalDateTime, LocalDateTime> startEnd;

    public Timeframe(Pair<LocalDateTime, LocalDateTime> startEnd) {
        this.startEnd = startEnd;
    }

    public Timeframe(LocalDateTime start, LocalDateTime end) {
        this.startEnd = new Pair<>(start, end);
    }

    public LocalDateTime getStart() {
        return startEnd.first;
    }
    public LocalDateTime getEnd() {
        return startEnd.second;
    }
    public void setStart(LocalDateTime date) {
        startEnd = new Pair<LocalDateTime, LocalDateTime>(date,startEnd.second);
    }
    public void setEnd(LocalDateTime date) {
        startEnd = new Pair<LocalDateTime, LocalDateTime>(startEnd.first, date);
    }

    public boolean incl(LocalDateTime datetime) {
        return (dist(datetime) == 0);
    }

    /**
     * @param   date    is this date within the timeframe
     * @return  the distance to the timeframe, negative if before, positive if after, 0 if within.
     */
    public long dist(LocalDateTime date) {
        return (date.compareTo(startEnd.first) < 0) ? -ChronoUnit.MINUTES.between(date, startEnd.first)/60 : (
                    (date.compareTo(startEnd.second) > 0) ? -ChronoUnit.MINUTES.between(date, startEnd.second)/60 : 0
                );
    }
}
