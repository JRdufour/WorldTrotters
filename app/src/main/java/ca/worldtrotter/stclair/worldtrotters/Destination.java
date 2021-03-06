package ca.worldtrotter.stclair.worldtrotters;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;

/**
 * Created by Dufour on 2018-04-01.
 */

public class Destination {
    int id;
    private String placeId;
    private long startDateTime;
    private long endDateTime;
    private int tripId;
    private String name;



    private ArrayList<ToDoItem> toDoItems;

    public Destination(String placeId, long startDateTime, long endDateTime, int tripId, String name) {
        this.placeId = placeId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.tripId = tripId;
        this.name = name;

        this.toDoItems = new ArrayList<>();
    }

    public Destination(int id, String placeId, long startDateTime, long endDateTime, int tripId, String name) {
        this.id = id;
        this.placeId = placeId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.tripId = tripId;
        this.name = name;
        this.toDoItems = new ArrayList<>();
    }

    public Destination(){

    }

    public ArrayList<ToDoItem> getToDoItems() {
        return toDoItems;
    }

    public void setToDoItems(ArrayList<ToDoItem> toDoItems) {
        this.toDoItems = toDoItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public long getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(long startDateTime) {
        this.startDateTime = startDateTime;
    }

    public long getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(long endDateTime) {
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


    public String toString(){
        return this.name;
    }

}
