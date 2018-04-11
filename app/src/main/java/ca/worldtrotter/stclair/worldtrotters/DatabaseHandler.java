package ca.worldtrotter.stclair.worldtrotters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.function.LongToIntFunction;

/**
 * Created by Dufour on 2018-03-26.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    /**
     * @author Dufour
     *
     * This class facilitates the creation and manipulation
     * of all the database tables we are going to use in the
     * WorldTrotters project
     *
     * This class will have all the CRUD functionality for each of
     * the three tables we are using: Trip, ca.worldtrotter.stclair.worldtrotters.Place, ca.worldtrotter.stclair.worldtrotters.ToDoItem
     */

    /**
     * Keep track of the database version
     */
    public static final int DATABASE_VERSION = 2;

    /**
     * Create the name of the database
     */
    public static final String DATABASE_NAME = "worldTrotters";

    /**
     * Create the names of the tables
     */

    public static final String TABLE_TRIPS = "trips";
    public static final String TABLE_DESTINATIONS = "destinations";
    public static final String TABLE_TO_TO_ITEMS = "to_do_items";
    public static final String TABLE_IMAGES = "images";
    /**
     * Create the names of all the table fields
     */
    //fields for trip table
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_START_DATE = "start_date";

    //fields for destinations table
    public static final String COLUMN_TRIP_ID = "trip_id";
    public static final String COLUMN_PLACE_ID = "place_id";
    public static final String COLUMN_START_DATE_TIME = "startTime";
    public static final String COLUMN_END_DATE_TIME = "endTime";



    //fields for toDoitem table
    public static final String COLUMN_DESTINATION_ID = "destination_id";
    public static final String COLUMN_DESCRIPTION = "description";

    //fields for images table
    public static final String COLUMN_IMAGE_PATH = "img_path";
    public static final String COLUMN_ATTRIBUTION = "attr";

    /**
     *
     * Create Statements for the tables
     */

    //create statement for trip table

    public static final String CREATE_TABLE_TRIPS = "CREATE TABLE " + TABLE_TRIPS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " +
            COLUMN_END_DATE + " LONG, " +
            COLUMN_START_DATE + " LONG )";

    //create statement for places table
    public static final String CREATE_TABLE_DESTINATIONS = "CREATE TABLE " + TABLE_DESTINATIONS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PLACE_ID + " TEXT, " +
            COLUMN_START_DATE_TIME + " LONG, " +
            COLUMN_END_DATE_TIME + " LONG, " +
            COLUMN_TRIP_ID + " INTEGER REFERENCES " + TABLE_TRIPS + "(" + COLUMN_ID + ")," +
            COLUMN_NAME + " TEXT)";

    //create statement for toDoItem table

    public static final String CREATE_TABLE_TO_DO_ITEMS = "CREATE TABLE " + TABLE_TO_TO_ITEMS + " ( " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_DESTINATION_ID + " INTEGER REFERENCES " + TABLE_DESTINATIONS +
            "( " + COLUMN_ID + "), " + COLUMN_NAME + " TEXT, " + COLUMN_DESCRIPTION + " TEXT)";

    public static final String CREATE_TABLE_IMAGES = "CREATE TABLE " + TABLE_IMAGES + " ( " +
            COLUMN_PLACE_ID + " TEXT PRIMARY KEY, " + COLUMN_IMAGE_PATH + " TEXT, " + COLUMN_ATTRIBUTION + " TEXT)";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRIPS);
        db.execSQL(CREATE_TABLE_DESTINATIONS);
        db.execSQL(CREATE_TABLE_TO_DO_ITEMS);
        db.execSQL(CREATE_TABLE_IMAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESTINATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TO_TO_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
    }

    /**
     * CRUD Operations for the database tables
     * CREATE, READ, UPDATE, DELETE
     */

    //CREATE
    public int addTrip(Trip trip){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, trip.getName());
        values.put(COLUMN_END_DATE, trip.getEndDate());
        values.put(COLUMN_START_DATE, trip.getStartDate());
        int id = (int) db.insert(TABLE_TRIPS, null, values);

        db.close();

        return id;
    }

    public int addDestination(Destination destination){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(COLUMN_PLACE_ID, destination.getPlaceId());
        values.put(COLUMN_START_DATE_TIME, destination.getStartDateTime());
        values.put(COLUMN_END_DATE_TIME, destination.getEndDateTime());
        values.put(COLUMN_TRIP_ID, destination.getTripId());
        values.put(COLUMN_NAME, destination.getName());

        int id = (int) db.insert(TABLE_DESTINATIONS, null, values);
        db.close();
        return id;
    }

    public void addToDoItem(ToDoItem item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_DESTINATION_ID, item.getDestinationId());
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_DESCRIPTION, item.getDescription());

        db.insert(TABLE_TO_TO_ITEMS, null, values);
        db.close();
    }

    public void addImage(Image img){



            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(COLUMN_PLACE_ID, img.getPlaceID());
            values.put(COLUMN_IMAGE_PATH, img.getImagePath());
            values.put(COLUMN_ATTRIBUTION, img.getAttribution());

            db.insert(TABLE_IMAGES, null, values);
            db.close();

    }

    //READ operations

    public Image getImage(String placeID){
        SQLiteDatabase db = this.getReadableDatabase();
        Image img = null;
        Cursor c = db.query(TABLE_IMAGES,
                new String[]{COLUMN_PLACE_ID, COLUMN_IMAGE_PATH, COLUMN_ATTRIBUTION},
                COLUMN_PLACE_ID + " =? ", new String[]{placeID},
                null, null, null, null);


        if(c.moveToFirst()){
            img = new Image(c.getString(0), c.getString(1), c.getString(2));
            db.close();
            return img;
        }
        db.close();
        return null;
    }

    public Image getImageForTrip(int tripId){
        Image image = null;
        ArrayList<Destination> destinations = this.getAllPlacesForTrip(tripId);
        if(destinations.size() != 0 && destinations != null) {
            image = this.getImage(destinations.get(0).getPlaceId());
        }
        return image;
    }

    public Trip getTrip(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Trip trip = null;

        Cursor c = db.query(TABLE_TRIPS,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_END_DATE, COLUMN_START_DATE},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if(c != null){
            c.moveToFirst();
            trip = new Trip(Integer.parseInt(c.getString(0)),
                    c.getString(1),
                    Long.parseLong(c.getString(2)),
                    Long.parseLong(c.getString(3)));
        }
        db.close();

        return trip;
    }

    public ArrayList<Trip> getAllTrips(){
        ArrayList<Trip> tripsList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_TRIPS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()){
            do{
                tripsList.add(new Trip(Integer.parseInt(c.getString(0)),
                        c.getString(1),
                        Long.parseLong(c.getString(2)),
                        Long.parseLong(c.getString(3))));
            } while(c.moveToNext());
        }

        db.close();
        return tripsList;
    }


    public Destination getDestination(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Destination place = null;

        Cursor c = db.query(TABLE_DESTINATIONS,
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_START_DATE_TIME, COLUMN_END_DATE_TIME,
                COLUMN_TRIP_ID, COLUMN_NAME},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        if (c.moveToFirst()){
            place = new Destination(Integer.parseInt(c.getString(0)),
                    c.getString(1),
                    Long.parseLong(c.getString(2)),
                    Long.parseLong(c.getString(3)),
                    Integer.parseInt(c.getString(4)),
                    c.getString(5));
        }
        db.close();
       return place;
    }

    //TODO getAllDestinations
    //this method returns a list of destinations for a specified trip
    public ArrayList<Destination> getAllPlacesForTrip(int tripId){
        ArrayList<Destination> placeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DESTINATIONS + " WHERE " + COLUMN_TRIP_ID + " = " + tripId;
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()){
            do{
                placeList.add(new Destination(Integer.parseInt(c.getString(0)),
                        c.getString(1),
                        Long.parseLong(c.getString(2)),
                        Long.parseLong(c.getString(3)),
                        Integer.parseInt(c.getString(4)),
                        c.getString(5)));
            } while (c.moveToNext());
        }
        db.close();
        return placeList;
    }
    //TODO get toDoItem
    public ToDoItem getToDoItem(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        ToDoItem item = null;

        Cursor c = db.query(TABLE_TO_TO_ITEMS,
                new String[]{COLUMN_ID, COLUMN_DESTINATION_ID, COLUMN_NAME, COLUMN_DESCRIPTION},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        if(c != null){
            c.moveToFirst();
            item = new ToDoItem(Integer.parseInt(c.getString(0)),
                    Integer.parseInt(c.getString(1)),
                    c.getString(2),
                    c.getString(3));
        }
        db.close();
        return item;
    }
    //TODO get allToDoItems

    public ArrayList<ToDoItem> getAllToDoItems(int destId){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ToDoItem> itemsList = new ArrayList<>();

        Cursor c = db.query(TABLE_TO_TO_ITEMS,
                new String[]{COLUMN_ID, COLUMN_DESTINATION_ID, COLUMN_NAME, COLUMN_DESCRIPTION},
                COLUMN_DESTINATION_ID + "=?", new String[]{String.valueOf(destId)},
                null, null, null, null);

        if(c .moveToFirst()){
            do{
                itemsList.add(new ToDoItem(Integer.parseInt(c.getString(0)),
                        Integer.parseInt(c.getString(1)),
                        c.getString(2),
                        c.getString(3)));
            } while(c.moveToNext());
        }
        db.close();
        return itemsList;
    }


    //TODO Update operations

    public void updateTrip(Trip trip){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(COLUMN_NAME, trip.getName());
        vals.put(COLUMN_END_DATE, trip.getEndDate());
        vals.put(COLUMN_START_DATE, trip.getStartDate());
        db.update(TABLE_TRIPS, vals, COLUMN_ID + "= ?",
                new String[]{String.valueOf(trip.getTripID())});

    }

    public void upDateDestination(Destination destination){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues vals = new ContentValues();

        vals.put(COLUMN_PLACE_ID, destination.getPlaceId());
        vals.put(COLUMN_START_DATE_TIME, destination.getStartDateTime());
        vals.put(COLUMN_END_DATE_TIME, destination.getEndDateTime());
        vals.put(COLUMN_TRIP_ID, destination.getTripId());
        vals.put(COLUMN_NAME, destination.getName());

        db.update(TABLE_DESTINATIONS, vals, COLUMN_ID + "= ?", new String[]{String.valueOf(destination.getId())});
    }

    public void updateImage(Image image){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLACE_ID, image.getPlaceID());
        values.put(COLUMN_IMAGE_PATH, image.getImagePath());
        values.put(COLUMN_ATTRIBUTION, image.getAttribution());

        db.update(TABLE_IMAGES, values, COLUMN_PLACE_ID + " = ? ", new String[]{image.getPlaceID()});
    }
    //TODO Delete operations
    public void deleteTrip(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRIPS, COLUMN_ID + " = ? ",
                new String[]{String.valueOf(id)});
        db.close();
    }
    public void deleteAllTrips(){
        String query = "DELETE FROM " + TABLE_TRIPS;
        SQLiteDatabase db = this.getWritableDatabase();

        db.close();
    }

    public void deleteDestination(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DESTINATIONS, COLUMN_ID + " = ? ",
                new String[]{String.valueOf(id)});
        db.close();
    }
    public void deleteToDoItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TO_TO_ITEMS, COLUMN_ID + " = ? ",
                new String[]{String.valueOf(id)});
        db.close();
    }

}
