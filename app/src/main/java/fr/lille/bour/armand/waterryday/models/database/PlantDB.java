package fr.lille.bour.armand.waterryday.models.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

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

    public static final String DATE_PATTERN = "yyyy/MM/dd";

    private static final PlantDB INSTANCE = new PlantDB();
    public static final String WHERE_CLAUSE = "%s = ?";

    static class PlantFields implements BaseColumns {
        public static final String FIELD_ID = "_id";
        public static final String FIELD_NAME = "name";
        public static final String FIELD_SPECIE = "specie";
        public static final String FIELD_LOCATION = "location";
        public static final String FIELD_WATERINGFREQUENCY = "wateringFrequency";
        public static final String FIELD_LASTWATEREDDATE = "lastWateredData";

        public static final String[] ALL = {
                _ID,
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
    public long insertPlant(final SQLiteOpenHelper helper, final Plant plant) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        final ContentValues values = convertPlantToContentValues(plant);
        final long id = db.insert(TABLE_NAME, null, values);
        plant.setId(id);
        return id;
    }

    /**
     * Deletes the specified plant at the specified id in the database.
     * @param helper The database helper to use.
     * @param id The id of the plant to delete.
     * @return <code>true</code> if a row was successfully deleted; <br>
     *         <code>false</code> otherwise.
     */
    public boolean deletePlant(final SQLiteOpenHelper helper, final long id) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        final String whereClause = String.format(WHERE_CLAUSE, PlantFields._ID);
        final String[] whereArgs = { String.valueOf(id) };
        return db.delete(TABLE_NAME, whereClause, whereArgs) == 1;
    }

    /**
     * Updates the plant in the database.
     * @param helper The database helper to use.
     * @param plant The plant to update.
     * @return <code>true</code> if a row was successfully updated; <br>
     *         <code>false</code> otherwise.
     */
    public boolean  updatePlant(final SQLiteOpenHelper helper, final Plant plant) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        final String whereClause = String.format(WHERE_CLAUSE, PlantFields._ID);
        final String[] whereArgs = { String.valueOf(plant.getId()) };
        final ContentValues values = convertPlantToContentValues(plant);
        return db.update(TABLE_NAME, values, whereClause, whereArgs) == 1;
    }

    /**
     * Converts a cursor resulting from a query from the database to a {@link Plant} object.
     * @param cursor The cursor containing the data.
     * @return A new {@link Plant} object containing the data.
     */
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

    /**
     * Converts a {@link Plant} object into a set of values to insert in the database.
     * @param plant The plant to insert.
     * @return
     */
    private static ContentValues convertPlantToContentValues(final Plant plant) {
        final String lastWateredDateStr = plant.getLastWateredDate().toString(DateTimeFormat.forPattern(DATE_PATTERN));
        final ContentValues values = new ContentValues();
        values.put(PlantFields.FIELD_NAME, plant.getName());
        values.put(PlantFields.FIELD_SPECIE, plant.getSpecie());
        values.put(PlantFields.FIELD_LOCATION, plant.getLocation());
        values.put(PlantFields.FIELD_WATERINGFREQUENCY, plant.getWateringFrequency());
        values.put(PlantFields.FIELD_LASTWATEREDDATE, lastWateredDateStr);
        return values;
    }

    /**
     * Fills the <code>Plants</code> table with predefined values
     * @param helper The database helper to use.
     */
    public void fillWithValues(final SQLiteOpenHelper helper) {
        final List<Plant> plants = Plant.generatePlants();
        for (final Plant plant : plants) {
            insertPlant(helper, plant);
        }
    }
}
