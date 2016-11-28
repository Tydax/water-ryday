package fr.lille.bour.armand.waterryday.models.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import fr.lille.bour.armand.waterryday.models.Plant;

/**
 * Singleton class used to interact with the Plant table.
 *
 * @author Armand (Tydax) BOUR
 */

public class PlantDB {

    /** The name of the table in the database. */
    public static final String TABLE_NAME = "plants";
    /** The query to insert for the field. */
    public static final String TABLE_FIELDS = String.format(", %s VARCHAR(100), %s VARCHAR(500), %s VARCHAR(200), %s INTEGER, %s VARCHAR(12)",
            PlantFields.FIELD_NAME,
            PlantFields.FIELD_SPECIE,
            PlantFields.FIELD_LOCATION,
            PlantFields.FIELD_WATERINGFREQUENCY,
            PlantFields.FIELD_LASTWATEREDDATE);

    private static final PlantDB INSTANCE = new PlantDB();

    static class PlantFields implements BaseColumns {
        public static final String FIELD_ID = "_id";
        public static final String FIELD_NAME = "name";
        public static final String FIELD_SPECIE = "specie";
        public static final String FIELD_LOCATION = "location";
        public static final String FIELD_WATERINGFREQUENCY = "wateringFrequency";
        public static final String FIELD_LASTWATEREDDATE = "lastWateredData";

        public static final String[] ALL = {
                FIELD_ID,
                FIELD_NAME,
                FIELD_SPECIE,
                FIELD_LOCATION,
                FIELD_WATERINGFREQUENCY,
                FIELD_LASTWATEREDDATE
        };
    }

    private PlantDB() {
        // Nothing to do here
    }

    /**
     * Gets the instance of the singleton.
     * @return The instance of the singleton.
     */
    public static PlantDB getInstance() {
        return INSTANCE;
    }

    /**
     * Gets all the plants in the database.
     * @return A list containing all the plants stored in the database.
     * @param helper The database helper to use.
     */
    public List<Plant> getAllPlants(final SQLiteOpenHelper helper) {
        final SQLiteDatabase db = helper.getReadableDatabase();
        final Cursor cursor = db.query(TABLE_NAME, PlantFields.ALL, null, null, null, null, null);
        cursor.moveToFirst();

        final List<Plant> plants = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            final Plant plant = convertCursorToPlant(cursor);
            plants.add(plant);
            cursor.moveToNext();
        }

        return plants;
    }

    /**
     * Inserts the specified plant in the database and updates its {@link Plant#id}.
     * @param plant The plant to insert in the database.
     * @param helper The database helper to use.
     * @returns The id of the plant.
     */
    public int insertPlant(final SQLiteOpenHelper helper, final Plant plant) {
        // TODO: implement
        return 0;
    }

    /**
     * Deletes the specified plant at the specified id in the database.
     * @param helper The database helper to use.
     * @param id The id of the plant to delete.
     */
    public void deletePlant(final SQLiteOpenHelper helper, final int id) {
        // TODO: implement
    }

    /**
     * Updates the plant in the database.
     * @param db The database helper to use.
     * @param plant The plant to update.
     */
    public void updatePlant(final SQLiteOpenHelper db, final Plant plant) {
        // TODO: implement
    }

    private static Plant convertCursorToPlant(final Cursor cursor) {
        final int id = cursor.getInt(0);
        final String name = cursor.getString(1);
        final String specie = cursor.getString(2);
        final String location = cursor.getString(3);
        final int wateringFrequency = cursor.getInt(4);
        final String lastWateredDateStr = cursor.getString(5);

        // Convert date
        final String[] fields = lastWateredDateStr.split("/");
        final int year = Integer.parseInt(fields[0]);
        final int month = Integer.parseInt(fields[1]);
        final int day = Integer.parseInt(fields[2]);
        final LocalDate lastWateredDate = new LocalDate(year, month, day);

        final Plant plant = new Plant(id, name, specie, location, wateringFrequency, lastWateredDate);
        return plant;
    }
}
