package com.example.letsvolunteer;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.List;

public class NewNotification {
    public String eventid;
    public String eventCategory;
    public String eventCreateDate;

    public NewNotification(String eventid, String eventCategory) {
        this.eventid = eventid;
        this.eventCategory = eventCategory;
    }


    public NewNotification(HashMap<String,Object> map){

    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventCreateDate() {
        return eventCreateDate;
    }

    public void setEventCreateDate(String eventdate) {
        this.eventCreateDate = eventdate;
    }
}
