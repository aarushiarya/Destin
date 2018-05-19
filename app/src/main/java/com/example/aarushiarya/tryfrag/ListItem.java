package com.example.aarushiarya.tryfrag;

public class ListItem {
    private  String id, head, desc, imageUrl;



    public ListItem(String id, String head, String desc, String imageUrl) {
        this.id = id;
        this.head = head;
        this.desc = desc;
        this.imageUrl = imageUrl;

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
    public String getPlace_id() {
        return id;
    }
}
