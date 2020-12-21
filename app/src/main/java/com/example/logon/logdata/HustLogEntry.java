package com.example.logon.logdata;

import com.example.logon.util.DateTimeSave;

import java.time.LocalDateTime;

public class HustLogEntry extends LogEntry {
    private String msg;

    public HustLogEntry(LocalDateTime dateTime, String msg) {
        super(dateTime, EntryType.HUSTLE);
        this.msg = msg;
    }


    @Override
    public SaveEntry saveEntry() {
        return new SaveEntry(DateTimeSave.saveDateTime(timestamp), type.toString(), msg);
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String s) {
        msg = s;
    }
}
