package com.example.aarushiarya.tryfrag;

class ListItem2 {
    private  String url, head, desc, imageUrl;
    private String rating,time;

    public ListItem2(String author_url, String head, String desc, String imageUrl, String rating, String time) {
        this.url = author_url;
        this.head = head;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.time = time;
    }

    public String getHead() {

        return head;
    }

    public String getDesc() {
        return desc;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getAuthor_url() {
        return url;
    }
    public String getRating() {
        return rating;
    }
    public String getTime() {
        return time;
    }

}