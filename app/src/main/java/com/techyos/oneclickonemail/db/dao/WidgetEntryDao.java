package com.techyos.oneclickonemail.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.techyos.oneclickonemail.Constants;
import com.techyos.oneclickonemail.db.Contract;
import com.techyos.oneclickonemail.db.DbHelper;
import com.techyos.oneclickonemail.db.model.WidgetEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ylemin on 26/10/15.
 * <p/>
 * This is a Data Access Object for the WidgetEntry object. The purpose of this object is to
 * simplify the insert, update and delete process of an object in the database.
 * Because let's face it, dealing with the built-in way of managing data is a pain, so that's
 * my way of making things a little cleaner.
 */

public class WidgetEntryDao {

    /**
     * Static
     */

    private static final String EMPTY_VAL = "**--EMPTY--**";
    private static final String SELECTION_QUERY = Contract.WidgetEntry._ID + " LIKE ?";
    private static final String WIDGET_ID_SELECTION_QUERY = Contract.WidgetEntry.COL_WIDGET_ID + " LIKE ?";
    private static final String SORT_ORDER = Contract.WidgetEntry.COL_LABEL + " ASC";

    /**
     * Attributes
     */

    private final DbHelper dbHelper;

    /**
     * Constructors
     */

    public WidgetEntryDao(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * Public Methods
     */

    public WidgetEntry insert(WidgetEntry entry) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            long entryId = db.insert(Contract.WidgetEntry.TABLE_NAME, null, entryToContentValues(entry));
            entry.setId(entryId);
            db.setTransactionSuccessful();

            return entry;
        } finally {
            db.endTransaction();
        }
    }

    public void update(WidgetEntry entry) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            db.update(Contract.WidgetEntry.TABLE_NAME, entryToContentValues(entry), SELECTION_QUERY, selectionArgs(entry));
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void delete(WidgetEntry entry) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            db.delete(Contract.WidgetEntry.TABLE_NAME, SELECTION_QUERY, selectionArgs(entry));
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteByWidgetId(int widgetId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            db.delete(Contract.WidgetEntry.TABLE_NAME, WIDGET_ID_SELECTION_QUERY, selectionArgs(widgetId));
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public WidgetEntry findById(long id) {
        return findBy(SELECTION_QUERY, selectionArgs(id));
    }

    public WidgetEntry findByWidgetId(int widgetId) {
        return findBy(WIDGET_ID_SELECTION_QUERY, selectionArgs(widgetId));
    }

    public List<WidgetEntry> findAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(Contract.WidgetEntry.TABLE_NAME, projection(), null, null, null, null, SORT_ORDER);

        List<WidgetEntry> entries = new ArrayList<>();

        while (cursor.moveToNext()) {
            entries.add(cursorToEntry(cursor));
        }
        cursor.close();

        return entries;
    }


    /**
     * Private Methods
     */

    private ContentValues entryToContentValues(WidgetEntry entry) {
        ContentValues values = new ContentValues();
        values.put(Contract.WidgetEntry.COL_WIDGET_ID, entry.getWidgetId());
        values.put(Contract.WidgetEntry.COL_LABEL, entry.getLabel());
        values.put(Contract.WidgetEntry.COL_TO, safeToValue(entry.getTo()));
        values.put(Contract.WidgetEntry.COL_SUBJECT_PREFIX, safeNullableValueToDb(entry.getSubjectPrefix()));
        values.put(Contract.WidgetEntry.COL_MESSAGE_PREFIX, safeNullableValueToDb(entry.getMessagePrefix()));
        return values;
    }

    private WidgetEntry cursorToEntry(Cursor cursor) {
        WidgetEntry entry = new WidgetEntry();

        entry.setId(cursor.getLong(cursor.getColumnIndex(Contract.WidgetEntry._ID)));
        entry.setWidgetId(cursor.getInt(cursor.getColumnIndex(Contract.WidgetEntry.COL_WIDGET_ID)));
        entry.setLabel(cursor.getString(cursor.getColumnIndex(Contract.WidgetEntry.COL_LABEL)));
        entry.setTo(splitToValue(cursor.getString(cursor.getColumnIndex(Contract.WidgetEntry.COL_TO))));
        entry.setSubjectPrefix(safeNullableValueToEntry(cursor.getString(cursor.getColumnIndex(Contract.WidgetEntry.COL_SUBJECT_PREFIX))));
        entry.setMessagePrefix(safeNullableValueToEntry(cursor.getString(cursor.getColumnIndex(Contract.WidgetEntry.COL_MESSAGE_PREFIX))));

        return entry;
    }

    private String[] selectionArgs(WidgetEntry entry) {
        return selectionArgs(entry.getId());
    }

    private String[] selectionArgs(long id) {
        return new String[]{String.valueOf(id)};
    }

    private String[] selectionArgs(int id) {
        return new String[]{String.valueOf(id)};
    }

    private String[] projection() {
        return new String[]{
                Contract.WidgetEntry._ID,
                Contract.WidgetEntry.COL_WIDGET_ID,
                Contract.WidgetEntry.COL_LABEL,
                Contract.WidgetEntry.COL_TO,
                Contract.WidgetEntry.COL_SUBJECT_PREFIX,
                Contract.WidgetEntry.COL_MESSAGE_PREFIX
        };
    }

    private String safeToValue(String[] fromEntry) {
        return TextUtils.join(Constants.SEPARATOR_COMMA, fromEntry);
    }

    private String[] splitToValue(String fromDb) {
        String[] split;
        if (fromDb.contains(Constants.SEPARATOR_COMMA)) {
            split = fromDb.split(Constants.SEPARATOR_COMMA);
        } else {
            split = new String[]{fromDb};
        }
        return split;
    }

    private String safeNullableValueToDb(String fromEntry) {
        return TextUtils.isEmpty(fromEntry) ? EMPTY_VAL : fromEntry;
    }

    private String safeNullableValueToEntry(String fromDb) {
        return EMPTY_VAL.equals(fromDb) ? null : fromDb;
    }

    private WidgetEntry findBy(String selectionQuery, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(Contract.WidgetEntry.TABLE_NAME,
                projection(),
                selectionQuery,
                selectionArgs,
                null,
                null,
                SORT_ORDER);

        WidgetEntry entry = null;

        if (cursor.getCount() > 0) {
            // In this case, we should have maximum one result
            cursor.moveToFirst();
            entry = cursorToEntry(cursor);
            cursor.close();
        }

        return entry;
    }
}
