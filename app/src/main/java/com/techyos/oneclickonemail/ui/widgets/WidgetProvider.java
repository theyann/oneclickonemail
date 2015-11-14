package com.techyos.oneclickonemail.ui.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.techyos.oneclickonemail.R;
import com.techyos.oneclickonemail.db.DbHelper;
import com.techyos.oneclickonemail.db.dao.WidgetEntryDao;
import com.techyos.oneclickonemail.db.model.WidgetEntry;

/**
 * Created by ylemin on 28/10/15.
 * <p/>
 * This is the class that manages the signals from and to the widgets.
 */

public class WidgetProvider extends AppWidgetProvider {

    /**
     * Overridden Methods: AppWidgetProvider
     */

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetEntryDao dao = new WidgetEntryDao(new DbHelper(context));

        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Finding the widget entry for the app widget id
            WidgetEntry entry = dao.findByWidgetId(appWidgetId);

            if (entry != null) {

                // creating the intent with all the information we want to pre-fill
                Intent intent = new Intent(Intent.ACTION_SEND);
                // array of destination email addresses
                intent.putExtra(Intent.EXTRA_EMAIL, entry.getTo());
                // subject (optional)
                intent.putExtra(Intent.EXTRA_SUBJECT, entry.getSubjectPrefix());
                // little bonus, we're adding some kind of signature after the (optional) message
                intent.putExtra(Intent.EXTRA_TEXT, entry.getMessagePrefix() + "\n\n\n" + context.getString(R.string.signature));
                // and setting a type that will narrow the choice of apps that handle email related stuff
                intent.setType("message/rfc822");

                // creating pending intent, the flag is REALLY important, and so is the appWidgetId
                PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                // using main layout to handle the click, so the whole widget is a button
                views.setOnClickPendingIntent(R.id.layoutWidget, pendingIntent);
                // setting the label
                views.setTextViewText(R.id.textLabel, entry.getLabel());

                // updating the widget because
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        WidgetEntryDao dao = new WidgetEntryDao(new DbHelper(context));
        final int N = appWidgetIds.length;

        for (int i = 0; i < N; i++) {
            dao.deleteByWidgetId(appWidgetIds[i]);
        }
    }
}
