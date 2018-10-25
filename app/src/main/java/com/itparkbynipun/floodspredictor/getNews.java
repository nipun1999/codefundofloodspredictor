package com.itparkbynipun.floodspredictor;

/**
 * Created by agarw on 10/25/2018.
 */

public class getNews {
    public getNews(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public getNews(String title) {
        this.title = title;
    }

    String title;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    String body;

}
