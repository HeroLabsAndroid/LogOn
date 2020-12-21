package com.example.logon;

import com.example.logon.logdata.EntryType;

import java.time.LocalDateTime;

public interface EditEntryInterface {
    void edit(int pos, String newMsg);
    void editDateTime(int pos, LocalDateTime dt);
    void editType(int pos, EntryType type);
}
