package ca.worldtrotter.stclair.worldtrotters;

/**
 * Created by Dufour on 2018-03-26.
 * This is the toDoItem class
 * ToDoItems will be contained in place objects as things the user want to do when they are in a certain locaiton
 * ie: hike this trail at this location, eat at this restaurant in this city, etc
 */

public class ToDoItem {
    private int id;
    private int placeId;
    private String name;
    private String description;

    public ToDoItem(int id, int placeId, String name, String description) {
        this.id = id;
        this.placeId = placeId;
        this.name = name;
        this.description = description;
    }

    public ToDoItem(int placeId, String name, String description) {
        this.placeId = placeId;
        this.name = name;
        this.description = description;
    }

    public ToDoItem() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
