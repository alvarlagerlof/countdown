package com.alvarlagerlof.countdown.Main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.alvarlagerlof.countdown.AddAndEdit.AddAndEdit;
import com.alvarlagerlof.countdown.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;

    ClockAdapter clockAdapter;
    ArrayList<ClockObject> list = new ArrayList<>();

    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fix stuff
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        setSupportActionBar(toolbar);
        toolbar.setTitle("CountDown");
        Realm.init(MainActivity.this);
        realm = Realm.getDefaultInstance();



        // Recyclerview
        clockAdapter = new ClockAdapter(list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(clockAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this));



        final Handler h = new Handler();
        final int delay = 1000; //milliseconds

        h.postDelayed(new Runnable(){
            public void run(){
                updateList();
                h.postDelayed(this, delay);
            }
        }, delay);


    }

    public void updateList() {

        list.clear();

        final RealmResults<ClockRealmObject> objects = realm.where(ClockRealmObject.class).findAllSorted("until", Sort.ASCENDING);
        for (int i = 0; i < objects.size(); i++) {

            String id = objects.get(i).getId();
            String title = objects.get(i).getTitle();
            int until = objects.get(i).getUntil();


            // Add to list
            list.add(new ClockObject(id, title, until));
        }

        clockAdapter.notifyDataSetChanged();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void add(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, AddAndEdit.class);
        startActivity(intent);
    }
}
