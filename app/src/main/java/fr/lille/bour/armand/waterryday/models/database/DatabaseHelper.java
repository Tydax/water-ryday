package fr.lille.bour.armand.waterryday.models.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Main class to use database.
 *
 * @author Armand (Tydax) BOUR
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "waterryday.db";

    private static final String REQ_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %s (_id INTEGER PRIMARY KEY %s)";
    private static final String REQ_DROP_TABLE = "DROP TABLE IF EXISTS %s";

    public DatabaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        final String reqPlants = String.format(REQ_CREATE_TABLE, PlantDB.TABLE_NAME, PlantDB.TABLE_FIELDS);
        db.execSQL(reqPlants);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int i, final int i1) {
        final String reqPLants = String.format(REQ_DROP_TABLE, PlantDB.TABLE_NAME);
        db.execSQL(reqPLants);

        onCreate(db);
    }
}
