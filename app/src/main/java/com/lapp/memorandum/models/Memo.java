package com.lapp.memorandum.models;

import java.text.DateFormat;
import java.util.*;
import io.realm.RealmObject;

public class Memo extends RealmObject
{
    /*Attributes*/
    private String title;
    private String description;
    public Date expiryDate;
    private Date dateOfCreation;
    private boolean isCompleted;
    public boolean isExpiry;
    private Location place;

    /**
     * Empty constructor method
     */
    public Memo() {}

    /**
     * Constructor method for null set
     * @param nullSet
     */
    public Memo(int nullSet)
    {
        setTitle("");
        setDescription("");
        setExpiryDate(null);
        setDateOfCreation(null);
        setCompleted(false);
        setExpiry(false);
        setPlace(new Location(0));
    }

    /**
     * Constructor method without location
     * @param title
     * @param description
     * @param expiryDate
     * @param dateOfCreation
     * @param isCompleted
     */
    public Memo(String title, String description, Date expiryDate, Date dateOfCreation, boolean isCompleted)
    {
        setTitle(title);
        setDescription(description);
        setExpiryDate(expiryDate);
        setDateOfCreation(dateOfCreation);
        setCompleted(isCompleted);
        setPlace(new Location(0));
        setIfIsExpiry();
    }

    /**
     * Constructor method without expiry date and location
     * @param title
     * @param description
     * @param dateOfCreation
     * @param isCompleted
     */
    public Memo(String title, String description, Date dateOfCreation, boolean isCompleted)
    {
        setTitle(title);
        setDescription(description);
        setExpiryDate(null);
        setDateOfCreation(dateOfCreation);
        setCompleted(isCompleted);
        setPlace(new Location(0));
        setExpiry(false);
    }

    /**
     * Constructor method
     * @param title
     * @param description
     * @param dateOfCreation
     * @param isCompleted
     * @param place
     */
    public Memo(String title, String description, Date expiryDate, Date dateOfCreation, boolean isCompleted, Location place)
    {
        setTitle(title);
        setDescription(description);
        setExpiryDate(expiryDate);
        setDateOfCreation(dateOfCreation);
        setCompleted(isCompleted);
        setPlace(place);
        setIfIsExpiry();
    }

    /**
     * Method to get the compose address String
     * @return
     */
    public String getComposeAddress()
    {
        String composeAddress = "";

        if(!getPlace().getAddress().equals(""))
        {
            composeAddress += getPlace().getAddress(); //Compose first part with the address

            if(!getPlace().getCity().equals(""))
                composeAddress += ", " + getPlace().getCity(); //Compose the second part with the city
        }

        return composeAddress;
    }

    /**
     * Method to get the Expiry date formatted like this --> 11/01/2001
     * @return
     */
    public String getExpiryDateFormatted()
    {
        if(getExpiryDate() != null)
            return DateFormat.getDateInstance().format(getExpiryDate());

        return "";
    }

    /**
     * Method to check if the Memo is expiry
     * true --> expiry
     * @return
     */
    public void setIfIsExpiry()
    {
        if(expiryDate != null)
        {
            Date currentDate = new Date(); //Set current date
            Calendar calendarCurrentDate = Calendar.getInstance();
            calendarCurrentDate.setTime(currentDate);

            Date check = getExpiryDate();
            Calendar calendarCheckDate = Calendar.getInstance();
            calendarCheckDate.setTime(check);

            if(calendarCheckDate.get(Calendar.YEAR) < calendarCurrentDate.get(Calendar.YEAR)) //Checking value of year
                setExpiry(true);
            else if(calendarCheckDate.get(Calendar.YEAR) == calendarCurrentDate.get(Calendar.YEAR))
            {
                if(calendarCheckDate.get(Calendar.MONTH) < calendarCurrentDate.get(Calendar.MONTH)) //Checking value of month
                    setExpiry(true);
                else if(calendarCheckDate.get(Calendar.MONTH) == calendarCurrentDate.get(Calendar.MONTH))
                {
                    if(calendarCheckDate.get(Calendar.DAY_OF_MONTH) < calendarCurrentDate.get(Calendar.DAY_OF_MONTH)) //Checking value of day of month
                        setExpiry(true);

                    else
                        setExpiry(false);
                }
            }
        }
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

    public Date getExpiryDate() { return expiryDate; }

    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; if(expiryDate != null) setIfIsExpiry(); else setExpiry(false);}

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

    public boolean getExpiry() { return isExpiry; }

    public void setExpiry(boolean expiry) { isExpiry = expiry; }

    public Location getPlace() { return place; }

    public void setPlace(Location place) { this.place = place; }
}
