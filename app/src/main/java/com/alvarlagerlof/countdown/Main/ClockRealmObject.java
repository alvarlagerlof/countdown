package com.alvarlagerlof.countdown.Main;

import io.realm.RealmObject;

public class ClockRealmObject extends RealmObject {

    private String id;
    private String title;
    private int until;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getUntil() {
        return until;
    }

    public void setUntil(int until) {
        this.until = until;

    }


}