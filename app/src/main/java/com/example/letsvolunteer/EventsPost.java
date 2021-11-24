package com.example.letsvolunteer;

import com.google.firebase.firestore.GeoPoint;

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
    public String categoryinterest;
    public GeoPoint location;
    public String locationAddress;

    public String getCategoryinterest() {
        return categoryinterest;
    }

    public void setCategoryinterest(String categoryinterest) {
        this.categoryinterest = categoryinterest;
    }

    public String getEventid() {
        return eventid;
    }

    public EventsPost() {
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public EventsPost(String eventName, String eventDescription, String phoneNumber, String emailId, String organiserid, String eventDate, String categoryinterest,GeoPoint geoPoint,String locationAddress) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
        this.organiserid = organiserid;
        this.eventDate = eventDate;
        this.categoryinterest = categoryinterest;
        this.location = geoPoint;
        this.locationAddress = locationAddress;
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
        this.categoryinterest = (String) map.get("categoryinterest");
        this.location = (GeoPoint) map.get("location");
        this.locationAddress = (String) map.get("locationAddress");

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

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
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

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }
}
