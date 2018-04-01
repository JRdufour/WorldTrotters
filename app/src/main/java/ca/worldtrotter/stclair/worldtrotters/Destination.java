package ca.worldtrotter.stclair.worldtrotters;

import com.google.android.gms.location.places.Place;

/**
 * Created by Dufour on 2018-04-01.
 */

public class Destination {
    int id;
    private String placeId;
    private String startDateTime;
    private String endDateTime;
    private int tripId;

    private String name;

    public Destination(String placeId, String startDateTime, String endDateTime, int tripId, String name) {
        this.placeId = placeId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.tripId = tripId;
        this.name = name;
    }

    public Destination(int id, String placeId, String startDateTime, String endDateTime, int tripId, String name) {
        this.id = id;
        this.placeId = placeId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.tripId = tripId;
        this.name = name;
    }

    public Destination(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
