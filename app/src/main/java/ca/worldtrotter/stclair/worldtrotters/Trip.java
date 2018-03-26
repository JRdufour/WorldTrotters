package ca.worldtrotter.stclair.worldtrotters;

/**
 * Created by Dufour on 2018-03-26.
 * This is the trip class, will contain information about the trips the user wants to complete
 */

public class Trip {
    private int tripID;
    private String name;
    private String dateCreated;
    private String imageURL;
    private String startDate;

    public Trip(int tripID, String name, String dateCreated, String imageURL, String startDate) {
        this.tripID = tripID;
        this.name = name;
        this.dateCreated = dateCreated;
        this.imageURL = imageURL;
        this.startDate = startDate;
    }

    public Trip(String name, String dateCreated, String imageURL, String startDate) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.imageURL = imageURL;
        this.startDate = startDate;
    }
    public Trip(){

    }

    public int getTripID() {
        return tripID;
    }

    public void setTripID(int tripID) {
        this.tripID = tripID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
