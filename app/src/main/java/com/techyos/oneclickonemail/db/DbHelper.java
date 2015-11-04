package com.techyos.oneclickonemail.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ylemin on 26/10/15.
 * <p/>
 * Database management implementing SQLHelper
 */
public class DbHelper extends SQLiteOpenHelper {

    /**
     * Static
     */

    /**
     * Bunch of SQL stuff to create and delete tables
     */

    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String SEP_COMMA = ",";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + Contract.WidgetEntry.TABLE_NAME + " (" +
                    Contract.WidgetEntry._ID + TYPE_INTEGER + " PRIMARY KEY" + SEP_COMMA +
                    Contract.WidgetEntry.COL_WIDGET_ID + TYPE_INTEGER + SEP_COMMA +
                    Contract.WidgetEntry.COL_LABEL + TYPE_TEXT + SEP_COMMA +
                    Contract.WidgetEntry.COL_TO + TYPE_TEXT + SEP_COMMA +
                    Contract.WidgetEntry.COL_SUBJECT_PREFIX + TYPE_TEXT + SEP_COMMA +
                    Contract.WidgetEntry.COL_MESSAGE_PREFIX + TYPE_TEXT + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Contract.WidgetEntry.TABLE_NAME;

    /**
     * DB Definition
     */

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ocom.db";

    /**
     * Constructors
     */

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Overridden Methods: SQLiteOpenHelper
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // We do not care about upgrading the DB in this case, we are not smart enough for that yet :)
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
