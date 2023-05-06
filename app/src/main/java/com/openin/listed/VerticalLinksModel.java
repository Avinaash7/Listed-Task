package com.openin.listed;

public class VerticalLinksModel {

    String title;
    String link;
    String date;
    String number;

    public VerticalLinksModel(String title, String link, String date, String number) {
        this.title = title;
        this.link = link;
        this.date = date;
        this.number = number;
    }

    VerticalLinksModel(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
