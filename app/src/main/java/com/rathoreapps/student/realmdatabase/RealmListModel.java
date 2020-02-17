package com.rathoreapps.student.realmdatabase;

import io.realm.RealmObject;

public class RealmListModel extends RealmObject {

    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_ROLLNO = "rollNo";

    String name;
    String rollNo;

    public void setName(String name) {
        this.name = name;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public String getRollNo() {
        return rollNo;
    }
}
