package com.example.letsvolunteer;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.List;

public class NewNotification {
    public String eventid;
    public String eventCategory;
    public Timestamp eventCreateDate;
    public String eventDate;
    public String eventName;
    public String organiserName;
    public String eventImage;

    public NewNotification(String eventid, String eventCategory, String eventDate, String eventName, String organiserName, String eventImage) {
        this.eventid = eventid;
        this.eventCategory = eventCategory;
        this.eventDate = eventDate;
        this.eventName = eventName;
        this.organiserName = organiserName;
        this.eventImage = eventImage;
    }

    public NewNotification(HashMap<String,Object> map){
        this.eventid = (String) map.get("eventid");
        this.eventCategory = (String) map.get("eventCategory");
        this.eventDate = (String) map.get("eventDate");
        this.eventName = (String) map.get("eventName");
        this.organiserName = (String) map.get("organiserName");
        this.eventCreateDate = (Timestamp) map.get("eventCreateDate");
        this.eventImage = (String) map.get("eventImage");

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

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public Timestamp getEventCreateDate() {
        return eventCreateDate;
    }

    public void setEventCreateDate(Timestamp eventCreateDate) {
        this.eventCreateDate = eventCreateDate;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getOrganiserName() {
        return organiserName;
    }

    public void setOrganiserName(String organiserName) {
        this.organiserName = organiserName;
    }
}
