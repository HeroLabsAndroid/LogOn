package com.example.logon.logdata;

import android.util.Log;

import com.example.logon.misc.Selection;
import com.example.logon.util.DateTimeSave;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;

public abstract class LogEntry implements Comparable<LogEntry> {
    protected LocalDateTime timestamp;
    protected EntryType type;
    protected int logpos;

    protected LogEntry(LocalDateTime timestamp, EntryType type) {
        this.timestamp = timestamp;
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public LogEntry setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }
    public EntryType getType() {
        return type;
    }
    public LogEntry setType(EntryType type) {
        this.type = type;
        return this;
    }
    public int getLogpos() {
        return logpos;
    }
    public LogEntry setLogpos(int logpos) {
        this.logpos = logpos;
        return this;
    }

    public boolean fits(Selection sel) {
        boolean f = (sel.types.contains(type) && sel.timeframe.incl(timestamp));
        Log.d("fits: ",""+f);

        return f;
    }

    public abstract SaveEntry saveEntry();
    public abstract String getMsg();
    public abstract void setMsg(String s);

    @Override
    public int compareTo(LogEntry o) {
        return o.timestamp.compareTo(timestamp) ;
    }
}
