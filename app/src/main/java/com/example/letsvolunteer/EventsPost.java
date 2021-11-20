package com.example.letsvolunteer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventsPost {
    public String eventName;
    public String eventDescription;
    public String phoneNumber;
    public String emailId;
    public List<String> imageUrlLists = new ArrayList<>();
    public String organiserid;
    public String eventDate;
    public String eventid;

    public String getEventid() {
        return eventid;
    }

    public EventsPost() {
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public EventsPost(String eventName, String eventDescription, String phoneNumber, String emailId, String organiserid, String eventDate) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
        this.organiserid = organiserid;
        this.eventDate = eventDate;
    }

    public EventsPost(String eventName, String eventDescription, String phoneNumber, String emailId, List<String> imageUrlLists, String organiserid, String eventDate) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
        this.imageUrlLists = imageUrlLists;
        this.organiserid = organiserid;
        this.eventDate = eventDate;

    }

    public EventsPost(HashMap<String,Object> map, String eventid){
        this.eventName = (String) map.get("eventName");
        this.eventName = (String) map.get("eventName");
        this.eventDescription = (String) map.get("eventDescription");
        this.phoneNumber = (String) map.get("phoneNumber");
        this.emailId = (String) map.get("emailId");
        this.imageUrlLists = (List<String>) map.get("imageUrlLists");
        this.organiserid = (String) map.get("organiserid");
        this.eventDate = (String) map.get("eventDate");
        this.eventid = eventid;
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

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getOrganiserid() {
        return organiserid;
    }

    public void setOrganiserid(String organiserid) {
        this.organiserid = organiserid;
    }

    public List<String> getImageUrlLists() {
        return imageUrlLists;
    }

    public void setImageUrlLists(List<String> imageUrlLists) {
        this.imageUrlLists = imageUrlLists;
    }
}
