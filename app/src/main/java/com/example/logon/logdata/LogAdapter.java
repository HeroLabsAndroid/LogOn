package com.example.logon.logdata;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logon.DeleteEntryInterface;
import com.example.logon.EditEntryInterface;
import com.example.logon.R;
import com.example.logon.SelectInterface;
import com.example.logon.misc.Selection;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.logViewHolder> implements SelectInterface{
        private Log log;
        private Log slct;
        private Selection sel;
        protected static FragmentManager fm;
        private DeleteEntryInterface delInt;
        private EditEntryInterface editInt;

    @Override
    public void updateSelection(Selection s) {
        sel = s;
        slct = log.select(sel);
        notifyDataSetChanged();
    }

    public static class logViewHolder extends RecyclerView.ViewHolder{
            public ConstraintLayout item;
            public TextView msgTV;
            public TextView dateTV;
            public TextView timeTV;
            public TextView typeTV;
            public FloatingActionButton removeBtn;
            public FloatingActionButton editBtn;
            DeleteEntryInterface del;
            EditEntryInterface edit;

            public int pos;;
            LogEntry l;

            public logViewHolder(ConstraintLayout itm, DeleteEntryInterface d, EditEntryInterface e) {
                super(itm);

                msgTV = itm.findViewById(R.id.msgAddTV);
                dateTV = itm.findViewById(R.id.dateTV);
                timeTV = itm.findViewById(R.id.timeTV);
                typeTV = itm.findViewById(R.id.typeTV);
                removeBtn = itm.findViewById(R.id.removeBtn);
                editBtn = itm.findViewById(R.id.editBtn);

                del = d;
                edit = e;

                item = itm;
            }

            public void setData(LogEntry l, int pos) {
                this.l = l;
                this.pos = pos;

                msgTV.setText(l.getMsg());
                dateTV.setText(l.timestamp.toLocalDate().toString());
                timeTV.setText(l.timestamp.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                typeTV.setText(l.getType().toString());
                typeTV.setClickable(true);
                typeTV.setOnClickListener(v -> {
                    EntryType now = EntryType.shift(EntryType.fromString(typeTV.getText().toString()));

                    overrideType(now);
                });

                removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        del.delete(l.logpos);
                    }
                });
                editBtn.setOnClickListener(v -> textPrompt(msgTV.getText().toString()));

            }

            public void textPrompt(String s) {

                final AlertDialog dialogBuilder = new AlertDialog.Builder(item.getContext()).create();
                LayoutInflater inflater = item.getContext().getSystemService(LayoutInflater.class);
                View dialogView = inflater.inflate(R.layout.edit_entry_dialog, null);

                final EditText editText = (EditText) dialogView.findViewById(R.id.inputEditTxt);
                editText.setText(s);
                final Button okBtn = dialogView.findViewById(R.id.okBtn);
                final Button editDTBtn = dialogView.findViewById(R.id.editDTBtn);

                final LocalDateTime[] thyme = {l.getTimestamp()};

                editDTBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog build = new AlertDialog.Builder(item.getContext()).create();
                        LayoutInflater inf = item.getContext().getSystemService(LayoutInflater.class);
                        View view = inf.inflate(R.layout.datetime_edit, null);

                        LocalDate locDT = l.getTimestamp().toLocalDate();
                        final DatePicker datePicker = view.findViewById(R.id.dateView);
                        datePicker.init(locDT.getYear(), locDT.getMonthValue() - 1, locDT.getDayOfMonth(), (view1, year, monthOfYear, dayOfMonth) -> {

                        });

                        LocalTime locT = l.getTimestamp().toLocalTime();
                        final TimePicker timePicker = view.findViewById(R.id.timeView);

                        timePicker.setHour(locT.getHour());
                        timePicker.setMinute(locT.getMinute());
                        timePicker.setIs24HourView(true);
                        final Button okBtn = view.findViewById(R.id.okBtn);
                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                itIsThyme(LocalDateTime.of(datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute()));
                                build.dismiss();
                            }
                        });



                        build.setView(view);
                        build.show();
                    }

                    public void itIsThyme(LocalDateTime t) {
                        thyme[0] = t;
                    }

                });

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        overrideMsg(editText.getText().toString());
                        overrideDateTime(thyme[0]);
                        dialogBuilder.dismiss();
                    }
                });

                dialogBuilder.setView(dialogView);
                dialogBuilder.show();
            }

        public void overrideType(EntryType t) {
                l.setType(t);
                typeTV.setText(t.toString());

                edit.editType(l.logpos, t);
        }

        private void overrideDateTime(LocalDateTime of) {
                l.setTimestamp(of);
                dateTV.setText(of.toLocalDate().toString());
                timeTV.setText(of.toLocalTime().toString());

                edit.editDateTime(l.logpos, of);
        }


        private void overrideMsg(String s) {
                l.setMsg(s);
                msgTV.setText(s);

                edit.edit(l.logpos, s);
            }

    }

        public LogAdapter(Log log, Selection s, FragmentManager fm, DeleteEntryInterface d, EditEntryInterface e) {
            delInt = d;
            editInt = e;
            this.log = log;
            sel = s;

            slct = log.select(sel);
            LogAdapter.fm = fm;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public logViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
            ConstraintLayout itm = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.log_entry_layout, parent, false);

            logViewHolder vh = new logViewHolder(itm, delInt, editInt);
            return vh;
        }

    // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(logViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            slct = log.select(sel);
            if (!slct.entries.isEmpty()) holder.setData(slct.getEntries().get(position),position);
                else holder.setData(new MiscLogEntry(LocalDateTime.now(),"There is nothing"),position);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return slct.getEntries().size();
        }
    }

