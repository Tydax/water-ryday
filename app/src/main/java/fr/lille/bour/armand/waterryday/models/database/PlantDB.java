package fr.lille.bour.armand.waterryday.models.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import fr.lille.bour.armand.waterryday.models.Plant;

/**
 * Single class to access the {@link Plant} table.
 *
 * @author Armand (Tydax) BOUR
 */

public class PlantDB extends AbstractDB<Plant> {

    /** The fields of the table. */
    protected static class PlantFields implements BaseColumns {
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


    /**
     * Gets the instance of the singleton.
     * @return The instance of the singleton.
     */
    public static PlantDB getInstance() {
        return INSTANCE;
    }

    /**
     * Gets the table name associated with that class.
     *
     * @return The table name.
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * Gets the list of fields used to initialise the table.
     *
     * @return A String containing the fields and the type for table creation.<br>
     * Example: ", attr1 TEXT, attr2 INT"
     */
    @Override
    public String getTableFieldsAndTypes() {
        return TABLE_FIELDS;
    }

    /**
     * Gets the list of table field names of the table.
     *
     * @return The list of table field names.
     */
    @Override
    public String[] getAllTableFields() {
        return PlantFields.ALL;
    }

    /**
     * Converts the specified plant into a {@link ContentValues} object for database interaction.
     *
     * @param plant The plant to convert.
     * @return A {@link ContentValues} object containing the values stored in the object.
     */
    @Override
    protected ContentValues convertObjectToContentValues(final Plant plant) {
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
     * Converts a cursor containing the values of one #K object to an instance of #K.
     *
     * @param cursor The cursor to convert.
     * @return An instance of K containing the values in the cursor.
     */
    @Override
    protected Plant convertCursorToObject(Cursor cursor) {
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
     * Fills the <code>Plants</code> table with predefined values
     * @param helper The database helper to use.
     */
    public void fillWithValues(final SQLiteOpenHelper helper) {
        final List<Plant> plants = Plant.generatePlants();
        for (final Plant plant : plants) {
            insert(helper, plant);
        }
    }

}
