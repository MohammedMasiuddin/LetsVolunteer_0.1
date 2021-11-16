package com.example.letsvolunteer;

public class EventsPost {
    public String eventName;
    public String eventDescription;
    public String phoneNumber;
    public String emailId;
    public String imgageurl;

    public EventsPost() {
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

    public String getImgageurl() {
        return imgageurl;
    }

    public void setImgageurl(String imgageurl) {
        this.imgageurl = imgageurl;
    }
}
