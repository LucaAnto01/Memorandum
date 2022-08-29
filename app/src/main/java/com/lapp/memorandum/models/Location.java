package com.lapp.memorandum.models;

import io.realm.RealmObject;

/*Class containing memo position data*/
public class Location extends RealmObject
{
    /*Attributes*/
    private String city;
    private String address;
    private double latitude;
    private double longitude;

    /**
     * Empty constructor method
     */
    public Location() {}

    /**
     * Constructor method for null set
     */
    public Location(int nulllSet)
    {
        setCity("");
        setAddress("");
        setLatitude(-1);
        setLongitude(-1);
    }

    /**
     * Constructor method
     * @param city
     * @param latitude
     * @param longitude
     */
    public Location(String city, double latitude, double longitude)
    {
        setCity(city);
        setAddress("");
        setLatitude(latitude);
        setLongitude(longitude);
    }

    /**
     * Constructor method
     * @param city
     * @param address
     * @param latitude
     * @param longitude
     */
    public Location(String city, String address, double latitude, double longitude)
    {
        setCity(city);
        setAddress(address);
        setLatitude(latitude);
        setLongitude(longitude);
    }



    /*Getters & Setters*/
    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }
}
