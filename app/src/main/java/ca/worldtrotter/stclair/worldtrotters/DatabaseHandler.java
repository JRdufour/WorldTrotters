package ca.worldtrotter.stclair.worldtrotters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
     * the three tables we are using: Trip, Place, ToDoItem
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
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
