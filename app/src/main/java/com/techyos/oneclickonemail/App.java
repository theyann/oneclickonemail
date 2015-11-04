package com.techyos.oneclickonemail;

import android.app.Application;

import com.techyos.oneclickonemail.db.DbHelper;
import com.techyos.oneclickonemail.db.dao.WidgetEntryDao;

/**
 * Created by ylemin on 28/10/15.
 * <p/>
 * This app extends the Application, as it is a singleton, and for this particular sample app
 * I am trying to keep things simple and as close to the roots as possible, this is the best
 * place to put singleton of our own if we need any ... like, let's say, the database stuff
 * for instance!
 */

public class App extends Application {

    /**
     * Attributes
     */

    private DbHelper dbHelper;
    private WidgetEntryDao widgetEntryDao;

    /**
     * LifeCycle
     */

    @Override
    public void onCreate() {
        super.onCreate();

        dbHelper = new DbHelper(this);
        widgetEntryDao = new WidgetEntryDao(dbHelper);
    }

    /**
     * Public Methods
     */

    public DbHelper getDbHelper() {
        return dbHelper;
    }

    public WidgetEntryDao getWidgetEntryDao() {
        return widgetEntryDao;
    }

}
