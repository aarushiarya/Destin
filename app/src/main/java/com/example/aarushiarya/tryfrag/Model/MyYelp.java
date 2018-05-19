package com.example.aarushiarya.tryfrag.Model;

public class MyYelp {
    private String total;

    private String[] possible_languages;

    private YelpReviews[] reviews;

    public String getTotal ()
    {
        return total;
    }

    public void setTotal (String total)
    {
        this.total = total;
    }

    public String[] getPossible_languages ()
    {
        return possible_languages;
    }

    public void setPossible_languages (String[] possible_languages)
    {
        this.possible_languages = possible_languages;
    }

    public YelpReviews[] getReviews ()
    {
        return reviews;
    }

    public void setReviews (YelpReviews[] reviews)
    {
        this.reviews = reviews;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [total = "+total+", possible_languages = "+possible_languages+", reviews = "+reviews+"]";
    }
}
