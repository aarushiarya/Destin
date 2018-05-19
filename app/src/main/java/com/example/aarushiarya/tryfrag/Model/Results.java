package com.example.aarushiarya.tryfrag.Model;

public class Results {private String icon;

    private String place_id;
    private Geom geometry;

    private String vicinity;
    private String name;


    public String getIcon ()
    {
        return icon;
    }

    public void setIcon (String icon)
    {
        this.icon = icon;
    }

    public String getPlace_id ()
    {
        return place_id;
    }

    public void setPlace_id (String place_id)
    {
        this.place_id = place_id;
    }

    public Geom getGeom()
    {
        return geometry;
    }

    public void setGeom(Geom geom)
    {
        this.geometry = geom;
    }

    public String getVicinity ()
    {
        return vicinity;
    }

    public void setVicinity (String vicinity)
    {
        this.vicinity = vicinity;
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
        return "ClassPojo [icon = "+icon+", place_id = "+place_id+", vicinity = "+vicinity+", name = "+name+"]";
    }
}