package com.example.logon.misc;

import android.util.Pair;

import com.example.logon.logdata.EntryType;
import com.example.logon.util.Timeframe;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Selection {
    public ArrayList<EntryType> types;
    public Timeframe timeframe;

    public Selection() {
        this.types = new ArrayList<EntryType>();
        types.addAll(Arrays.asList(EntryType.MISC, EntryType.FOOD, EntryType.DRUGS, EntryType.HUSTLE));
        timeframe = new Timeframe(LocalDateTime.MIN, LocalDate.now().plusDays(1).atStartOfDay());
    }

    public Selection(ArrayList<EntryType> types, Timeframe timeframe) {
        this.types = types;
        this.timeframe = timeframe;
    }

    public Selection(Timeframe timeframe) {
        this.types = new ArrayList<EntryType>();
        types.addAll(Arrays.asList(EntryType.MISC, EntryType.FOOD, EntryType.DRUGS, EntryType.HUSTLE));
        this.timeframe = timeframe;
    }

    public void slct(EntryType e) {
        if(!types.contains(e)) types.add(e);
    }

    public void rm(EntryType e) {
        if(types.contains(e)) types.remove(e);
    }
}
