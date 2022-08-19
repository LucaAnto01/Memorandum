package com.example.memorandum;

import java.util.*;

public class Memo
{
    /*Attributes*/
    private String title;
    private String description;
    private Date dateOfCreation;
    private boolean isCompleted;
    private Location place;

    /**
     * Empty constructor method
     */
    public Memo()
    {
        setTitle("");
        setDescription("");
        setDateOfCreation(null);
        setCompleted(false);
        setPlace(new Location());
    }

    /**
     * Constructor method
     * @param title
     * @param description
     * @param dateOfCreation
     * @param isCompleted
     */
    public Memo(String title, String description, Date dateOfCreation, boolean isCompleted)
    {
        setTitle(title);
        setDescription(description);
        setDateOfCreation(dateOfCreation);
        setCompleted(isCompleted);
        setPlace(new Location());
    }

    /**
     * Constructor method
     * @param title
     * @param description
     * @param dateOfCreation
     * @param isCompleted
     * @param place
     */
    public Memo(String title, String description, Date dateOfCreation, boolean isCompleted, Location place)
    {
        setTitle(title);
        setDescription(description);
        setDateOfCreation(dateOfCreation);
        setCompleted(isCompleted);
        setPlace(place);
    }

    /*Getters & Setters*/
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getDateOfCreation()
    {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation)
    {
        this.dateOfCreation = dateOfCreation;
    }

    public boolean isCompleted() { return isCompleted; }

    public void setCompleted(boolean completed) { isCompleted = completed; }

    public Location getPlace() { return place; }

    public void setPlace(Location place) { this.place = place; }
}
