package com.example.aarushiarya.tryfrag.Model;

public class User {
    private String image_url;

    private String name;

    public String getImage_url ()
    {
        return image_url;
    }

    public void setImage_url (String image_url)
    {
        this.image_url = image_url;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [image_url = "+image_url+", name = "+name+"]";
    }
}
