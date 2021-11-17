package com.example.letsvolunteer;

import java.util.ArrayList;
import java.util.List;

public class EventsPost {
    public String eventName;
    public String eventDescription;
    public String phoneNumber;
    public String emailId;
    public List<String> imageUrlLists = new ArrayList<>();
    public String organiserid;

    public List<String> getImageUrlLists() {
        return imageUrlLists;
    }

    public void setImageUrlLists(List<String> imageUrlLists) {
        this.imageUrlLists = imageUrlLists;
    }

    public EventsPost(String eventName, String eventDescription, String phoneNumber, String emailId
                  ) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
//        this.imgageurl = imgageurl;
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


}
