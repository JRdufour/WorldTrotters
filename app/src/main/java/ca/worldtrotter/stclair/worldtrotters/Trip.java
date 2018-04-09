package ca.worldtrotter.stclair.worldtrotters;

/**
 * Created by Dufour on 2018-03-26.
 * This is the trip class, will contain information about the trips the user wants to complete
 */

public class Trip {
    private int tripID;
    private String name;
    private long endDate;
    private String imageURL;
    private long startDate;

    public Trip(int tripID, String name, long endDate, String imageURL, long startDate) {
        this.tripID = tripID;
        this.name = name;
        this.endDate = endDate;
        this.imageURL = imageURL;
        this.startDate = startDate;
    }

    public Trip(String name, long endDate, String imageURL, long startDate) {
        this.name = name;
        this.endDate = endDate;
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

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long dateCreated) {
        this.endDate = dateCreated;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }
}
