/**
 * Created by Dufour on 2018-03-26.
 * This is the toDoItem class
 * ToDoItems will be contained in place objects as things the user want to do when they are in a certain locaiton
 * ie: hike this trail at this location, eat at this restaurant in this city, etc
 */

public class ToDoItem {
    private int id;
    private int place_id;
    private String name;
    private String description;

    public ToDoItem(int id, int place_id, String name, String description) {
        this.id = id;
        this.place_id = place_id;
        this.name = name;
        this.description = description;
    }

    public ToDoItem(int place_id, String name, String description) {
        this.place_id = place_id;
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

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
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
