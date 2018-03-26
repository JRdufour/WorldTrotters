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
