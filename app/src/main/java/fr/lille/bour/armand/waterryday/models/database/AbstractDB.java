package fr.lille.bour.armand.waterryday.models.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import fr.lille.bour.armand.waterryday.models.DBObject;

/**
 * Abstract class used as a template for database interaction class.
 *
 * @author Armand (Tydax) BOUR
 */

public abstract class AbstractDB<K extends DBObject> {

    protected static final String WHERE_CLAUSE = "%s = ?";

    /**
     * Gets the table name associated with that class.
     * @return The table name.
     */
    public abstract String getTableName();

    /**
     * Gets the list of fields used to initialise the table.
     * @return A String containing the fields and the type for table creation.<br>
     *     Example: ", attr1 TEXT, attr2 INT"
     */
    public abstract String getTableFieldsAndTypes();

    /**
     * Gets the list of table field names of the table.
     * @return The list of table field names.
     */
    public abstract String[] getAllTableFields();

    /**
     * Converts the specified object into a {@link ContentValues} object for database interaction.
     * @param obj The object to convert.
     * @return A {@link ContentValues} object containing the values stored in the object.
     */
    protected abstract ContentValues convertObjectToContentValues(final K obj);

    /**
     * Converts a cursor containing the values of one #K object to an instance of #K.
     * @param c The cursor to convert.
     * @return An instance of K containing the values in the cursor.
     */
    protected abstract K convertCursorToObject(final Cursor c);

    /**
     * Gets all the objects contained in the table.
     * @param helper The database helper.
     * @return A {@link List} containing all the objects contained in the table.
     */
    public K get(final SQLiteOpenHelper helper, final long id) {
        final SQLiteDatabase db = helper.getReadableDatabase();
        final String selection = String.format(WHERE_CLAUSE, BaseColumns._ID);
        final String[] selectArgs = { String.valueOf(id) };
        final Cursor cursor = db.query(getTableName(), getAllTableFields(), selection, selectArgs, null, null, null);
        cursor.moveToFirst();
        final K object = convertCursorToObject(cursor);
        return object;
    }

    /**
     * Gets all the objects contained in the table.
     * @param helper The database helper.
     * @return A {@link List} containing all the objects contained in the table.
     */
    public List<K> getAll(final SQLiteOpenHelper helper) {
        final SQLiteDatabase db = helper.getReadableDatabase();
        final Cursor cursor = db.query(getTableName(), getAllTableFields(), null, null, null, null, null);
        cursor.moveToFirst();

        final List<K> objects = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            final K object = convertCursorToObject(cursor);
            objects.add(object);
            cursor.moveToNext();
        }

        return objects;
    }

    /**
     * Inserts the specified object in the database and updates its id.
     * @param object The object to insert in the database.
     * @param helper The database helper to use.
     * @returns The id of the object.
     */
    public long insert(final SQLiteOpenHelper helper, final K object) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        final ContentValues values = convertObjectToContentValues(object);
        final long id = db.insert(getTableName(), null, values);
        object.setId(id);
        return id;
    }


    /**
     * Deletes the specified object at the specified id in the database.
     * @param helper The database helper to use.
     * @param id The id of the object to delete.
     * @return <code>true</code> if a row was successfully deleted; <br>
     *         <code>false</code> otherwise.
     */
    public boolean delete(final SQLiteOpenHelper helper, final long id) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        final String whereClause = String.format(WHERE_CLAUSE, BaseColumns._ID);
        final String[] whereArgs = { String.valueOf(id) };
        return db.delete(getTableName(), whereClause, whereArgs) == 1;
    }

    /**
     * Updates the object in the database.
     * @param helper The database helper to use.
     * @param object The object to update.
     * @return <code>true</code> if a row was successfully updated; <br>
     *         <code>false</code> otherwise.
     */
    public boolean update(final SQLiteOpenHelper helper, final K object) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        final String whereClause = String.format(WHERE_CLAUSE, BaseColumns._ID);
        final String[] whereArgs = { String.valueOf(object.getId()) };
        final ContentValues values = convertObjectToContentValues(object);
        return db.update(getTableName(), values, whereClause, whereArgs) == 1;
    }

    public int cleanTable(final SQLiteOpenHelper helper) {
        return helper.getWritableDatabase().delete(getTableName(), "1", null);
    }
}
