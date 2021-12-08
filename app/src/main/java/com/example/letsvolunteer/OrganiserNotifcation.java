package com.example.letsvolunteer;

import com.google.firebase.Timestamp;

import java.util.HashMap;

public class OrganiserNotifcation {

    public String firstName;
    public String lastName;
    public String photoUri;
    public String volunteerId;
    public String eventid;
    public String organiserid;
    public String eventName;

    public OrganiserNotifcation(String firstName, String lastName, String photoUri, String volunteerId, String  eventid , String organiserid,String eventName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUri = photoUri;
        this.volunteerId = volunteerId;
        this.eventid = eventid;
        this.organiserid = organiserid;
        this.eventName = eventName;
    }

    public OrganiserNotifcation(HashMap<String, Object> map) {
        this.eventid = (String) map.get("eventid");
        this.firstName = (String) map.get("firstName");
        this.lastName = (String) map.get("lastName");
        this.eventName = (String) map.get("eventName");
        this.photoUri = (String) map.get("photoUri");
        this.volunteerId = (String) map.get("volunteerId");
        this.organiserid = (String) map.get("organiserid");
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEventid() {
        return eventid;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getOrganiserid() {
        return organiserid;
    }

    public void setOrganiserid(String organiserid) {
        this.organiserid = organiserid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(String volunteerId) {
        this.volunteerId = volunteerId;
    }
}
