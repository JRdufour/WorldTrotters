package ca.worldtrotter.stclair.worldtrotters;

import com.google.android.gms.location.places.Place;

/**
 * Created by Dufour on 2018-04-01.
 */

public class Destination {
    int id;
    Place place;
    String startDateTime;
    String endDateTime;

    public Destination(Place place, String startDateTime, String endDateTime) {
        this.place = place;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public Destination(int id, Place place, String startDateTime, String endDateTime) {
        this.id = id;
        this.place = place;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
    public Destination(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
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
}
