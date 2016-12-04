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
    private Date requestTimeStamp;
    private Date requestStartDate;
    private Date requestEndDate;
    private String requestPetID;

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

    public Date getRequestTimeStamp() {
        return requestTimeStamp;
    }

    public void setRequestTimeStamp(Date requestTimeStamp) {
        this.requestTimeStamp = requestTimeStamp;
    }

    public Date getRequestStartDate() {
        return requestStartDate;
    }

    public void setRequestStartDate(Date requestStartDate) {
        this.requestStartDate = requestStartDate;
    }

    public Date getRequestEndDate() {
        return requestEndDate;
    }

    public void setRequestEndDate(Date requestEndDate) {
        this.requestEndDate = requestEndDate;
    }

    public String getRequestPetID() {
        return requestPetID;
    }

    public void setRequestPetID(String requestPetID) {
        this.requestPetID = requestPetID;
    }
}