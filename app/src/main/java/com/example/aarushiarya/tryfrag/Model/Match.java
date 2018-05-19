package com.example.aarushiarya.tryfrag.Model;

public class Match {
    private MyBusi[] businesses;

    public MyBusi[] getBusinesses ()
    {
        return businesses;
    }

    public void setBusinesses (MyBusi[] businesses)
    {
        this.businesses = businesses;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [businesses = "+businesses+"]";
    }
}