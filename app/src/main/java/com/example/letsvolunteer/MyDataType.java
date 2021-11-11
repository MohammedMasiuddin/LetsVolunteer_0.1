package com.example.letsvolunteer;

import java.io.Serializable;

public class MyDataType implements Serializable {

public String date;
public String name;
public String picture;
public String date_of_post;

    public String get(String name) {
        return name;
    }
}

