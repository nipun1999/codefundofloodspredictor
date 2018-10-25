package com.itparkbynipun.floodspredictor;

/**
 * Created by agarw on 10/22/2018.
 */

public class getFloodData {


    String lat;
    String longi;
    String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    String tag;


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }



    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public getFloodData(){

    }
    public getFloodData(String lat, String longi, String tag, String user) {
        this.lat = lat;
        this.longi = longi;
        this.tag = tag;
        this.user = user;
    }

}
