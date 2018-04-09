package ca.worldtrotter.stclair.worldtrotters;

/**
 * Created by Dufour on 2018-04-09.
 */

public class Image {
    private String placeID;
    private String imagePath;
    private String attribution;

    public Image(String placeID, String imagePath, String attribution) {
        this.placeID = placeID;
        this.imagePath = imagePath;
        this.attribution = attribution;
    }
    public Image(){

    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }
}
