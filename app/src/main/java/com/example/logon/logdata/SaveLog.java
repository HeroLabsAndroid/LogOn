package com.example.logon.logdata;

import java.util.ArrayList;

public class SaveLog {
    private SaveEntry[] entries;

    public SaveEntry[] getEntries() {
        return entries;
    }

    public SaveLog setEntries(SaveEntry[] entries) {
        this.entries = entries;
        return this;
    }

    public SaveLog(LogEntry[] log) {
        entries = new SaveEntry[log.length];

        for (int i=0; i<log.length; i++) {
            entries[i] = (log[i]).saveEntry();
        }
    }
}
