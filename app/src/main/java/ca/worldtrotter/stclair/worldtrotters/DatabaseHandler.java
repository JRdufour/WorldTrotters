package ca.worldtrotter.stclair.worldtrotters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    public static final int DATABASE_VERSION = 1;

    /**
     * Create the name of the database
     */
    public static final String DATABASE_NAME = "worldTrotters";

    /**
     * Create the names of the tables
     */

    public static final String TABLE_TRIPS = "trips";
    public static final String TABLE_PLACES = "places";
    public static final String TABLE_TO_TO_ITEMS = "to_do_items";
    /**
     * Create the names of all the table fields
     */
    //fields for trip table
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE_CREATED = "date_created";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_START_DATE = "start_date";

    //fields for places table
    public static final String COLUMN_TRIP_ID = "trip_id";
    public static final String COLUMN_PLACE_ID = "place_id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_GEOTAG = "geotag";

    //fields for toDoitem table
    public static final String COLUMN_DESCRIPTION = "description";


    /**
     *
     * Create Statements for the tables
     */

    //create statement for trip table

    public static final String CREATE_TABLE_TRIPS = "CREATE TABLE " + TABLE_TRIPS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_NAME + " TEXT, " +
            COLUMN_DATE_CREATED + " TEXT, " + COLUMN_IMAGE + " TEXT, " +
            COLUMN_START_DATE + " TEXT )";

    //create statement for places table
    public static final String CREATE_TABLE_PLACES = "CREATE TABLE " + TABLE_PLACES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_TRIP_ID + " INTEGER REFERENCES " +
            TABLE_TRIPS + "(" + COLUMN_ID + "), " + COLUMN_PLACE_ID + " TEXT, " + COLUMN_NAME +
            " TEXT, " + COLUMN_IMAGE + " TEXT, " + COLUMN_LATITUDE + " TEXT, " + COLUMN_LONGITUDE
            + " TEXT, " + COLUMN_GEOTAG + " TEXT)";

    //create statement for toDoItem table

    public static final String CREATE_TABLE_TO_DO_ITEMS = "CREATE TABLE " + TABLE_TO_TO_ITEMS + " ( " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_PLACE_ID + " INTEGER REFERENCES " + TABLE_PLACES +
            "( " + COLUMN_ID + "), " + COLUMN_NAME + " TEXT, " + COLUMN_DESCRIPTION + " TEXT";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRIPS);
        db.execSQL(CREATE_TABLE_PLACES);
        db.execSQL(CREATE_TABLE_TO_DO_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TO_TO_ITEMS);
    }

    /**
     * CRUD Operations for the database tables
     * CREATE, READ, UPDATE, DELETE
     */

    //CREATE
    public void addTrip(Trip trip){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, trip.getName());
        values.put(COLUMN_DATE_CREATED, trip.getDateCreated());
        values.put(COLUMN_IMAGE, trip.getImageURL());
        values.put(COLUMN_START_DATE, trip.getStartDate());
        db.insert(TABLE_TRIPS, null, values);
        db.close();
    }

    public void addPlace(Place place){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TRIP_ID, place.getTripId());
        values.put(COLUMN_PLACE_ID, place.getPlaceId());
        values.put(COLUMN_NAME, place.getName());
        values.put(COLUMN_IMAGE, place.getImageURL());
        values.put(COLUMN_LATITUDE, place.getLatitude());
        values.put(COLUMN_LONGITUDE, place.getLongitude());
        values.put(COLUMN_GEOTAG, place.getGeoTag());

        db.insert(TABLE_PLACES, null, values);
        db.close();
    }

    public void addToDoItem(ToDoItem item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PLACE_ID, item.getPlaceId());
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_DESCRIPTION, item.getDescription());
    }

    //READ operations


    public Trip getTrip(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Trip trip = null;

        Cursor c = db.query(TABLE_TRIPS,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_DATE_CREATED, COLUMN_IMAGE, COLUMN_START_DATE},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if(c != null){
            c.moveToFirst();
            trip = new Trip(Integer.parseInt(c.getString(0)),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4));
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
                        c.getString(2),
                        c.getString(3),
                        c.getString(4)));
            } while(c.moveToNext());
        }

        db.close();
        return tripsList;
    }


    public Place getPlace(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Place place = null;

        Cursor c = db.query(TABLE_PLACES,
                new String[]{COLUMN_ID, COLUMN_TRIP_ID, COLUMN_PLACE_ID, COLUMN_NAME,
                COLUMN_IMAGE, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_GEOTAG},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        if (c != null){
            c.moveToFirst();
            place = new Place(Integer.parseInt(c.getString(0)),
                    Integer.parseInt(c.getString(1)),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6),
                    c.getString(7));
        }
        db.close();
       return place;
    }

    //TODO getAllPlaces
    //this method returns a list of places for a specified trip
    public ArrayList<Place> getAllPlacesForTrip(int tripId){
        ArrayList<Place> placeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_PLACES,
                new String[]{COLUMN_ID, COLUMN_TRIP_ID, COLUMN_PLACE_ID, COLUMN_NAME,
                        COLUMN_IMAGE, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_GEOTAG},
                COLUMN_TRIP_ID + "=?", new String[]{String.valueOf(tripId)},
                null, null, null, null);
        if(c.moveToFirst()){
            do{
                placeList.add(new Place(Integer.parseInt(c.getString(0)),
                        Integer.parseInt(c.getString(1)),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5),
                        c.getString(6),
                        c.getString(7)));
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
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_NAME, COLUMN_DESCRIPTION},
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

    public ArrayList<ToDoItem> getAllToDoItems(int placeId){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ToDoItem> itemsList = new ArrayList<>();

        Cursor c = db.query(TABLE_TO_TO_ITEMS,
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_NAME, COLUMN_DESCRIPTION},
                COLUMN_PLACE_ID + "=?", new String[]{String.valueOf(placeId)},
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


    //TODO Delete operations
    public void deleteTrip(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRIPS, COLUMN_ID + " = ? ",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deletePlace(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLACES, COLUMN_ID + " = ? ",
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
