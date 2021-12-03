package com.example.letsvolunteer;

public class ModelVolunteer {

    String age, email, firstName, lastName, photoUri, uid;

    public ModelVolunteer() {
    }

    public ModelVolunteer(String age, String email, String firstName, String lastName, String photoUri, String uid) {
        this.age = age;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUri = photoUri;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
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
}
