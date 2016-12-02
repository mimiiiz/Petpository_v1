package com.example.petpository_v1.Model;

import java.util.ArrayList;

/**
 * Created by User on 2/12/2559.
 */

public class Pet {

    private String petID;
    private String petName;
    private String petSize;
    private String petType; //พันธุ์หมา
    private ArrayList<String> petPhoto;

    public String getPetID() {
        return petID;
    }

    public void setPetID(String petID) {
        this.petID = petID;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetSize() {
        return petSize;
    }

    public void setPetSize(String petSize) {
        this.petSize = petSize;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public ArrayList<String> getPetPhoto() {
        return petPhoto;
    }

    public void setPetPhoto(ArrayList<String> petPhoto) {
        this.petPhoto = petPhoto;
    }
}
