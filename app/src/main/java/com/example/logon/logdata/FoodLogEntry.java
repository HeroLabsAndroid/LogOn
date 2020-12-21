package com.example.logon.logdata;

import com.example.logon.util.DateTimeSave;

import java.time.LocalDateTime;

public class FoodLogEntry extends LogEntry {
    private String msg;

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String s) {
        this.msg=s;
    }


    public FoodLogEntry(LocalDateTime dateTime, String msg) {
        super(dateTime, EntryType.FOOD);
        this.msg = msg;
    }

    @Override
    public SaveEntry saveEntry() {
        return new SaveEntry(DateTimeSave.saveDateTime(timestamp), type.toString(), msg);
    }

    @Override
    public int compareTo(LogEntry o) {
        return o.timestamp.compareTo(timestamp) ;
    }
}
