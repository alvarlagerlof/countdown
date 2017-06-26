package com.alvarlagerlof.countdown.AddAndEdit;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alvarlagerlof.countdown.Main.ClockRealmObject;
import com.alvarlagerlof.countdown.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddAndEdit extends AppCompatActivity {

    Toolbar toolbar;
    TextInputEditText titleEditText;
    TextView dateTextView;
    Button changeDateButton;
    Button deleteButton;

    Realm realm;

    String id;
    String title = null;
    int until;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_edit);

        // Views
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleEditText = (TextInputEditText) findViewById(R.id.title_edit_text);
        dateTextView = (TextView) findViewById(R.id.date_text_view);
        changeDateButton = (Button) findViewById(R.id.change_date_button);
        deleteButton = (Button) findViewById(R.id.delete_button);



        // Intent
        if (getIntent().getExtras() != null) {
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            until = getIntent().getIntExtra("until", 0);

            titleEditText.setText(title);

            Date date = new Date(until * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy, HH:mm");

            dateTextView.setText(sdf.format(date));

            toolbar.setTitle("Edit");
            changeDateButton.setText("Change");
        } else {
            toolbar.setTitle("Add");
            deleteButton.setVisibility(View.GONE);
        }


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Realm.init(AddAndEdit.this);
        realm = Realm.getDefaultInstance();


    }




    public void changeDate(View view) {
        final View dialogView = View.inflate(AddAndEdit.this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(AddAndEdit.this).create();

        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
        final Button button = (Button) dialogView.findViewById(R.id.date_time_set);

        timePicker.setIs24HourView(true);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (button.getText().equals("Next")) {
                    timePicker.setVisibility(View.VISIBLE);
                    datePicker.setVisibility(View.GONE);
                    button.setText("Done");

                } else {

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, datePicker.getYear());
                    cal.set(Calendar.MONTH, datePicker.getMonth());
                    cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    cal.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);

                    SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy, HH:mm");
                    dateTextView.setText(sdf.format(cal.getTime()));

                    until = (int) (cal.getTimeInMillis() / 1000L);

                    alertDialog.dismiss();
                }


            }});
        alertDialog.setView(dialogView);
        alertDialog.show();

    }






    // Sav
    public void save(View view) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (getIntent().getExtras() == null) {
                    Long tsLong = System.currentTimeMillis();
                    String ts = tsLong.toString().trim();

                    ClockRealmObject clockRealmObject = realm.createObject(ClockRealmObject.class);
                    clockRealmObject.setId(ts);
                    clockRealmObject.setTitle(String.valueOf(titleEditText.getText()));
                    clockRealmObject.setUntil(until);
                } else {
                    ClockRealmObject clockRealmObject = realm.where(ClockRealmObject.class).equalTo("id", id).findFirst();
                    clockRealmObject.setTitle(String.valueOf(titleEditText.getText()));
                    clockRealmObject.setUntil(until);
                }
            }
        });
        finish();
    }


    // Delete
    public void delete(View view) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ClockRealmObject> result = realm.where(ClockRealmObject.class)
                        .equalTo("id", id)
                        .findAll();
                result.deleteAllFromRealm();
            }
        });
        finish();
    }



    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
