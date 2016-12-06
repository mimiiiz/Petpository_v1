package com.example.petpository_v1.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 4/12/2559.
 */

public class RequestPet implements Serializable {

    private String requestID;
    private String requestUID_owner;
    private String requestUID_sitter;
    private String requestStatus;
    private String requestPlaceID;
    private String requestTimeStamp;
    private String requestStartDate;
    private String requestEndDate;
    private Pet Pet;
    private String OwnerPhoneNo;

    public com.example.petpository_v1.Model.Pet getPet() {
        return Pet;
    }

    public void setPet(com.example.petpository_v1.Model.Pet pet) {
        Pet = pet;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getRequestUID_owner() {
        return requestUID_owner;
    }

    public void setRequestUID_owner(String requestUID_owner) {
        this.requestUID_owner = requestUID_owner;
    }

    public String getRequestUID_sitter() {
        return requestUID_sitter;
    }

    public void setRequestUID_sitter(String requestUID_sitter) {
        this.requestUID_sitter = requestUID_sitter;
    }

    public String getRequestPlaceID() {
        return requestPlaceID;
    }

    public void setRequestPlaceID(String requestPlaceID) {
        this.requestPlaceID = requestPlaceID;
    }

    public String getRequestTimeStamp() {
        return requestTimeStamp;
    }

    public void setRequestTimeStamp(String requestTimeStamp) {
        this.requestTimeStamp = requestTimeStamp;
    }

    public String getRequestStartDate() {
        return requestStartDate;
    }

    public void setRequestStartDate(String requestStartDate) {
        this.requestStartDate = requestStartDate;
    }

    public String getRequestEndDate() {
        return requestEndDate;
    }

    public void setRequestEndDate(String requestEndDate) {
        this.requestEndDate = requestEndDate;
    }


}
