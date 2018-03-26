/**
 * Created by Dufour on 2018-03-26.
 * Place objects will be contained in Trip objects
 * Represents a real place in the world the user wants to visit
 * Places will be pulled from the google API for places, will have to add more fields as needed
 */

public class Place {

    private int id;
    private int trip_id;
    private String place_id;
    private String name;
    private String imageURL;
    private String latitude;
    private String longitude;
    private String geoTag;

    public Place(int id, int trip_id, String place_id, String name, String imageURL, String latitude, String longitude, String geoTag) {
        this.id = id;
        this.trip_id = trip_id;
        this.place_id = place_id;
        this.name = name;
        this.imageURL = imageURL;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geoTag = geoTag;
    }

    public Place(int trip_id, String place_id, String name, String imageURL, String latitude, String longitude, String geoTag) {
        this.trip_id = trip_id;
        this.place_id = place_id;
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

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
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
