package ca.worldtrotter.stclair.worldtrotters;

/**
 * Created by Dufour on 2018-03-26.
 * This is the toDoItem class
 * ToDoItems will be contained in place objects as things the user want to do when they are in a certain locaiton
 * ie: hike this trail at this location, eat at this restaurant in this city, etc
 */

public class ToDoItem {
    private int id;
    private int destinationId;
    private String name;
    private String description;

    public ToDoItem(int id, int destinationId, String name, String description) {
        this.id = id;
        this.destinationId = destinationId;
        this.name = name;
        this.description = description;
    }

    public ToDoItem(int destinationId, String name, String description) {
        this.destinationId = destinationId;
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

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int placeId) {
        this.destinationId = destinationId;
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

    public String toString(){
        return this.getName();
    }
}
