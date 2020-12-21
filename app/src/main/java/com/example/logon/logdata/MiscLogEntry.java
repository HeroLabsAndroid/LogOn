package com.example.logon.logdata;

import com.example.logon.util.DateTimeSave;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MiscLogEntry extends LogEntry {
    private String msg;

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String s) {
        this.msg=s;
    }


    public MiscLogEntry(LocalDateTime dateTime, String msg) {
        super(dateTime, EntryType.MISC);
        this.msg = msg;
    }

    @Override
    public SaveEntry saveEntry() {
        return new SaveEntry(DateTimeSave.saveDateTime(timestamp), type.toString(), msg);
    }
}
