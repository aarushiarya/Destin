package com.example.aarushiarya.tryfrag.Model;

public class YelpReviews {
    private String id;

    private String text;

    private String time_created;

    private String rating;

    private User user;

    private String url;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getTime_created ()
    {
        return time_created;
    }

    public void setTime_created (String time_created)
    {
        this.time_created = time_created;
    }

    public String getRating ()
    {
        return rating;
    }

    public void setRating (String rating)
    {
        this.rating = rating;
    }

    public User getUser ()
    {
        return user;
    }

    public void setUser (User user)
    {
        this.user = user;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", text = "+text+", time_created = "+time_created+", rating = "+rating+", user = "+user+", url = "+url+"]";
    }
}
