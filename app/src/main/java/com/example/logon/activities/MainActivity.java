package com.example.logon.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DateKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logon.DeleteEntryInterface;
import com.example.logon.EditEntryInterface;
import com.example.logon.R;
import com.example.logon.SelectInterface;
import com.example.logon.logdata.DrugLogEntry;
import com.example.logon.logdata.EntryType;
import com.example.logon.logdata.FoodLogEntry;
import com.example.logon.logdata.HustLogEntry;
import com.example.logon.logdata.Log;
import com.example.logon.logdata.LogAdapter;
import com.example.logon.logdata.LogEntry;
import com.example.logon.logdata.MiscLogEntry;
import com.example.logon.logdata.SaveEntry;
import com.example.logon.logdata.SaveLog;
import com.example.logon.misc.Selection;
import com.example.logon.util.Timeframe;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DeleteEntryInterface, EditEntryInterface{

    RecyclerView logRecView;
    FloatingActionButton addBtn;
    Button typBtn;
    EditText msgET;

    EditText startEditText;
    EditText endEditText;
    
    Chip miscChip;
    Chip foodChip;
    Chip drugChip;
    Chip hustChip;

    Log log;

    Selection sel = new Selection();
    SelectInterface selInt;

    EntryType type = EntryType.MISC;

    Toolbar bar;
    DateTimeFormatter std = DateTimeFormatter.ofPattern("dd/MM/yy");
    DateTimeFormatter dot = DateTimeFormatter.ofPattern("dd.MM.yy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupGUI();
    }

    private void setupGUI() {
        logRecView = findViewById(R.id.logRecView);
        logRecView.setHasFixedSize(true);
        logRecView.setLayoutManager(new LinearLayoutManager(this));

        msgET = findViewById(R.id.msgEditText);

        addBtn = findViewById(R.id.confirmBtn);
        addBtn.setOnClickListener(v -> {
            switch (type) {
                case MISC:    addEntry(new MiscLogEntry(LocalDateTime.now(), msgET.getText().toString())); break;
                case DRUGS:   addEntry(new DrugLogEntry(LocalDateTime.now(), msgET.getText().toString())); break;
                case FOOD:    addEntry(new FoodLogEntry(LocalDateTime.now(), msgET.getText().toString())); break;
                case HUSTLE:  addEntry(new HustLogEntry(LocalDateTime.now(), msgET.getText().toString())); break;
            }
            msgET.setText("");
        });

        typBtn = findViewById(R.id.typeBtn);
        typBtn.setOnClickListener(v -> {
            type = EntryType.shift(type);
            updateGUI();
        });

        miscChip = findViewById(R.id.miscChip);
        miscChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchSelected(EntryType.MISC, isChecked);
        });

        foodChip = findViewById(R.id.foodChip);
        foodChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchSelected(EntryType.FOOD, isChecked);
        });

        drugChip = findViewById(R.id.drugChip);
        drugChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchSelected(EntryType.DRUGS, isChecked);
        });

        hustChip = findViewById(R.id.hustChip);
        hustChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchSelected(EntryType.HUSTLE, isChecked);
        });

        startEditText = findViewById(R.id.startEditText);
        startEditText.setSelectAllOnFocus(true);
        startEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateStart(s);
            }
        });

        endEditText = findViewById(R.id.endEditText);
        endEditText.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yy")));
        endEditText.setSelectAllOnFocus(true);
        endEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateEnd(s);
            }
        });

        setupLog();

        bar = findViewById(R.id.my_toolbar);
        bar.setSubtitle("Logged "+log.select(new Selection( new Timeframe(LocalDate.now().atStartOfDay(), LocalDateTime.now()))).getEntries().size()+" today.");
        bar.setTitle("It is "+LocalDate.now().getDayOfWeek()+", my Dudes!");
        setSupportActionBar(bar);

    }

    public void updateStart(Editable s) {
        try {
            LocalDateTime d = LocalDate.parse(s.toString(), DateTimeFormatter.ofPattern("dd/MM/yy")).atStartOfDay();
            sel.timeframe.setStart(d);
        } catch (Exception e) {
        }

        selInt.updateSelection(sel);
    }
    public void updateEnd(Editable s) {
        try {
            LocalDateTime d = LocalDate.parse(s.toString(), DateTimeFormatter.ofPattern("dd/MM/yy")).atStartOfDay().plusDays(1).minusSeconds(1);
            sel.timeframe.setEnd(d);
        } catch (Exception e) {
        }
        selInt.updateSelection(sel);
    }

    public void switchSelected(EntryType e, boolean isChecked) {
        if(isChecked) sel.slct(e); else sel.rm(e);
        selInt.updateSelection(sel);
        logRecView.getAdapter().notifyDataSetChanged();
    }

    private void updateGUI() {
        typBtn.setText(""+type);
    }

    private void setupLog() {
        log = loadSave();
        LogAdapter lAdapt = new LogAdapter(log, sel, getSupportFragmentManager(), this, this);
        selInt = lAdapt;
        logRecView.setAdapter(lAdapt);
    }

    private void addEntry(LogEntry entry) {
        log.addEntry(entry);
        bar.setSubtitle("Logged "+log.select(new Selection( new Timeframe(LocalDate.now().atStartOfDay(), LocalDateTime.now()))).getEntries().size()+" today.");
        bar.setTitle("It is "+LocalDate.now().getDayOfWeek()+", my Dudes!");
        logRecView.getAdapter().notifyDataSetChanged();
        log.holdYourPositions();
    }

    private void removeEntry(int pos) {
        log.rmvEntry(pos);
        bar.setSubtitle("Logged "+log.select(new Selection( new Timeframe(LocalDate.now().atStartOfDay(), LocalDateTime.now()))).getEntries().size()+" today.");
        bar.setTitle("It is "+LocalDate.now().getDayOfWeek()+", my Dudes!");
        logRecView.getAdapter().notifyDataSetChanged();
        log.holdYourPositions();
    }

    private void updateEntry(int pos, LogEntry entry) {
        ArrayList<LogEntry> tlog = log.getEntries();
        tlog.set(pos, entry);
        tlog.sort((o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()));

        log.setEntries(tlog);
        log.holdYourPositions();

        logRecView.getAdapter().notifyDataSetChanged();
    }


    private Log loadSave() {
        Log out;

        android.util.Log.d("loadSave()","Entered loadSave()");
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        String json = sp.getString(getResources().getString(R.string.PREF_KEY_SAVE),"");
        Gson gson = new Gson();

        if(!json.equals("")) {
            SaveLog save = gson.fromJson(json, SaveLog.class);
            out = Log.buildFromSave(save);
            Collections.sort(out.getEntries());
            out.holdYourPositions();
        } else {
            android.util.Log.d("loadSave()","No String found");
            out = new Log();
        }

        return out;
    }

    public boolean putSave() {
        SaveLog save = log.makeSave();

        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        Gson gson = new Gson();
        String json = gson.toJson(save);

        ed.putString(getResources().getString(R.string.PREF_KEY_SAVE), json);
        ed.commit();
        android.util.Log.d("putSave()","Saved successfully.");

        return true;
    }

    @Override
    protected void onStop() {
        putSave();
        super.onStop();
    }

    @Override
    public void delete(int pos) {
        removeEntry(pos);
    }

    @Override
    public void edit(int pos, String newMsg) {
        LogEntry e = log.getEntries().get(pos);
        e.setMsg(newMsg);
        updateEntry(pos, e);
    }

    @Override
    public void editDateTime(int pos, LocalDateTime dt) {
        LogEntry e = log.getEntries().get(pos);
        e.setTimestamp(dt);
        updateEntry(pos, e);
    }

    @Override
    public void editType(int pos, EntryType type) {
        LogEntry e = log.getEntries().get(pos);
        e.setType(type);
        updateEntry(pos, e);
    }

}