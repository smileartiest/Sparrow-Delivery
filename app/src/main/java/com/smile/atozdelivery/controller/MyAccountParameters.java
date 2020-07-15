package com.smile.atozdelivery.controller;

public class MyAccountParameters  {

    String id,eusname,ests,epic,epass,ename,lat,lang;

    public MyAccountParameters() {
    }

    public MyAccountParameters(String id, String eusname, String ests, String epic, String epass, String ename, String lat, String lang) {
        this.id = id;
        this.eusname = eusname;
        this.ests = ests;
        this.epic = epic;
        this.epass = epass;
        this.ename = ename;
        this.lat = lat;
        this.lang = lang;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEusname() {
        return eusname;
    }

    public void setEusname(String eusname) {
        this.eusname = eusname;
    }

    public String getEsts() {
        return ests;
    }

    public void setEsts(String ests) {
        this.ests = ests;
    }

    public String getEpic() {
        return epic;
    }

    public void setEpic(String epic) {
        this.epic = epic;
    }

    public String getEpass() {
        return epass;
    }

    public void setEpass(String epass) {
        this.epass = epass;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }
}
