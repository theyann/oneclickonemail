package com.techyos.oneclickonemail.db;

import android.provider.BaseColumns;

/**
 * Created by ylemin on 26/10/15.
 *
 * Contract defining the database, this is the suggestion from the documentation. Frankly
 * I don't know how I feel about it.
 */

public final class Contract {

    /**
     * Constructors
     */

    private Contract() {
        // class should never be instantiated
    }

    /**
     * Inner Classes
     */

    public static abstract class WidgetEntry implements BaseColumns {
        public static final String TABLE_NAME = "ocomWidgetEntry";
        public static final String COL_WIDGET_ID = "widgetId";
        public static final String COL_LABEL = "label";
        public static final String COL_TO = "dest";
        public static final String COL_SUBJECT_PREFIX = "subjectPrefix";
        public static final String COL_MESSAGE_PREFIX = "messagePrefix";
    }

}
