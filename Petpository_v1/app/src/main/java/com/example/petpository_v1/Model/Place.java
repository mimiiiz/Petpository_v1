package com.example.petpository_v1.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by User on 2/12/2559.
 */

public class Place implements Serializable {
    private String placeId;
    private String placeName;
    private String placeAddress;
    private String placeTel;
    private String placeDetail;
    private String placeWebsite;
    private String placeFacebook;
    private String placeLine;
    private String placeIg;
    private int placeStarAvg;
    private Double placePrice;
    private int reviewerAmount;
    private String placeWorkDay;
    private ArrayList<String> placePhoto;
    private Double placeLatitude;
    private Double placeLongtitude;
    private int placeNumberImg;
    private int numberImage;
    private String uidSitter;
    private  String placeActivity;

    public String getPlaceActivity() {
        return placeActivity;
    }

    public void setPlaceActivity(String placeActivity) {
        this.placeActivity = placeActivity;
    }

    public int getNumberImage() {
        return numberImage;
    }

    public void setNumberImage(int numberImage) {
        this.numberImage = numberImage;
    }

    public String getUidSitter() {
        return uidSitter;
    }

    public void setUidSitter(String uidSitter) {
        this.uidSitter = uidSitter;
    }

    public String getPlaceId() {

        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getPlaceTel() {
        return placeTel;
    }

    public void setPlaceTel(String placeTel) {
        this.placeTel = placeTel;
    }

    public String getPlaceDetail() {
        return placeDetail;
    }

    public void setPlaceDetail(String placeDetail) {
        this.placeDetail = placeDetail;
    }

    public String getPlaceWebsite() {
        return placeWebsite;
    }

    public void setPlaceWebsite(String placeWebsite) {
        this.placeWebsite = placeWebsite;
    }

    public String getPlaceFacebook() {
        return placeFacebook;
    }

    public void setPlaceFacebook(String placeFacebook) {
        this.placeFacebook = placeFacebook;
    }

    public String getPlaceLine() {
        return placeLine;
    }

    public void setPlaceLine(String placeLine) {
        this.placeLine = placeLine;
    }

    public String getPlaceIg() {
        return placeIg;
    }

    public void setPlaceIg(String placeIg) {
        this.placeIg = placeIg;
    }

    public int getPlaceStarAvg() {
        return placeStarAvg;
    }

    public void setPlaceStarAvg(int placeStarAvg) {
        this.placeStarAvg = placeStarAvg;
    }

    public Double getPlacePrice() {
        return placePrice;
    }

    public void setPlacePrice(Double placePrice) {
        this.placePrice = placePrice;
    }

    public int getReviewerAmount() {
        return reviewerAmount;
    }

    public void setReviewerAmount(int reviewerAmount) {
        this.reviewerAmount = reviewerAmount;
    }

    public String getPlaceWorkDay() {
        return placeWorkDay;
    }

    public void setPlaceWorkDay(String placeWorkDay) {
        this.placeWorkDay = placeWorkDay;
    }

    public ArrayList<String> getPlacePhoto() {
        return placePhoto;
    }

    public void setPlacePhoto(ArrayList<String> placePhoto) {
        this.placePhoto = placePhoto;
    }

    public Double getPlaceLatitude() {
        return placeLatitude;
    }

    public void setPlaceLatitude(Double placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public Double getPlaceLongtitude() {
        return placeLongtitude;
    }

    public void setPlaceLongtitude(Double placeLongtitude) {
        this.placeLongtitude = placeLongtitude;
    }

    public int getPlaceNumberImg() {
        return placeNumberImg;
    }

    public void setPlaceNumberImg(int placeNumberImg) {
        this.placeNumberImg = placeNumberImg;
    }
}
