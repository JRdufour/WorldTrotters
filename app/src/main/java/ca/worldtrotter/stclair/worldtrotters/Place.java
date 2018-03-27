package ca.worldtrotter.stclair.worldtrotters;

/**
 * Created by Dufour on 2018-03-26.
 * ca.worldtrotter.stclair.worldtrotters.Place objects will be contained in Trip objects
 * Represents a real place in the world the user wants to visit
 * Places will be pulled from the google API for places, will have to add more fields as needed
 */

public class Place {

    private int id;
    private int tripId;
    private String placeId;
    private String name;
    private String imageURL;
    private String latitude;
    private String longitude;
    private String geoTag;

    public Place(int id, int tripId, String placeId, String name, String imageURL, String latitude, String longitude, String geoTag) {
        this.id = id;
        this.tripId = tripId;
        this.placeId = placeId;
        this.name = name;
        this.imageURL = imageURL;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geoTag = geoTag;
    }

    public Place(int tripId, String placeId, String name, String imageURL, String latitude, String longitude, String geoTag) {
        this.tripId = tripId;
        this.placeId = placeId;
        this.name = name;
        this.imageURL = imageURL;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geoTag = geoTag;
    }

    public Place() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int trip_id) {
        this.tripId = trip_id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlace_id(String place_id) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getGeoTag() {
        return geoTag;
    }

    public void setGeoTag(String geoTag) {
        this.geoTag = geoTag;
    }
}
