package com.example.logon.logdata;

import com.example.logon.misc.Selection;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Log {
    protected ArrayList<LogEntry> entries;

    public Log() {
        entries = new ArrayList<>();
        entries.add(new MiscLogEntry(LocalDateTime.now(), "Started loggin"));
        entries.get(0).setLogpos(0);
    }

    public ArrayList<LogEntry> getEntries() {
        return entries;
    }

    public Log setEntries(ArrayList<LogEntry> entries) {
        this.entries = entries;
        return this;
    }

    public void addEntry(LogEntry entry) {
        entries.add(0, entry);
    }

    public void rmvEntry(int pos) {
        entries.remove(pos);
    }

    public SaveLog makeSave() {
        return new SaveLog(entryArr());
    }

    public LogEntry[] entryArr() {
        LogEntry[] out = new LogEntry[entries.size()];
        for (int i=0; i<entries.size(); i++) {
            out[i] = entries.get(i);
        }

        return out;
    }

    public void holdYourPositions() {
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).setLogpos(i);
        }
    }

    public static Log buildFromSave(SaveLog save) {
        Log out = new Log();
        out.entries.clear();
        SaveEntry[] ent = save.getEntries();

        for(SaveEntry se: ent) {
            out.addEntry(se.fromSave());
        }

        return out;
    }

    private void sort() {
        entries.sort(LogEntry::compareTo);
    }

    public Log select(Selection sel) {
        Log out = new MtLog();

        for(LogEntry e: entries) {
            if(e.fits(sel)) {
                out.addEntry(e);
            }
        }

        out.sort();

        return out;
    }
}
