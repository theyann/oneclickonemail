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

            WidgetEntry entry = dao.findByWidgetId(appWidgetId);

            if (entry != null) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, entry.getTo());
                intent.putExtra(Intent.EXTRA_SUBJECT, entry.getSubjectPrefix());
                intent.putExtra(Intent.EXTRA_TEXT, entry.getMessagePrefix());
                intent.setType("text/plain");

                PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, Intent.createChooser(intent, "Send Email"), 0);

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                views.setOnClickPendingIntent(R.id.layoutWidget, pendingIntent);
                views.setTextViewText(R.id.textLabel, entry.getLabel());

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
