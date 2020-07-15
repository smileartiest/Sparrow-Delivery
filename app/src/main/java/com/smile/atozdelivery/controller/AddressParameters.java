package com.smile.atozdelivery.controller;

public class AddressParameters {

    String id,uid,lat,lang,address,cno,city;

    public AddressParameters() {
    }

    public AddressParameters(String id, String uid, String lat, String lang, String address, String cno, String city) {
        this.id = id;
        this.uid = uid;
        this.lat = lat;
        this.lang = lang;
        this.address = address;
        this.cno = cno;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }
}
