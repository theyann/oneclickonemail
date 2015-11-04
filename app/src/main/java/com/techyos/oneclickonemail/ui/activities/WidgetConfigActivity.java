package com.techyos.oneclickonemail.ui.activities;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.techyos.oneclickonemail.Constants;
import com.techyos.oneclickonemail.R;
import com.techyos.oneclickonemail.db.model.WidgetEntry;
import com.techyos.oneclickonemail.ui.widgets.WidgetProvider;

/**
 * Created by ylemin on 29/10/15.
 * <p/>
 * This is the activity dealing with the configuration of the widget (when it is first added) as
 * well as the editing that can be done from the selection of an item in the list.
 */

public class WidgetConfigActivity extends ActivityBase {

    /**
     * Attributes
     */

    private EditText editLabel;
    private EditText editTo;
    private EditText editSubjectPrefix;
    private EditText editMessagePrefix;

    private int widgetId;
    private WidgetEntry widgetEntry;

    /**
     * LifeCycle
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);

        setResult(RESULT_CANCELED);
        widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

        Intent intent = getIntent();

        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras.containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
                widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                widgetEntry = getApp().getWidgetEntryDao().findByWidgetId(widgetId);
            }
        }

        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        initViews();
    }

    /**
     * Private Methods
     */

    private void initViews() {
        editLabel = (EditText) findViewById(R.id.editLabel);
        editTo = (EditText) findViewById(R.id.editTo);
        editSubjectPrefix = (EditText) findViewById(R.id.editSubjectPrefix);
        editMessagePrefix = (EditText) findViewById(R.id.editMessagePrefix);

        fillForm();

        View button = findViewById(R.id.buttonOk);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okClicked();
            }
        });

        button = findViewById(R.id.buttonCancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClicked();
            }
        });
    }

    private void fillForm() {
        if (widgetEntry != null) {
            editLabel.setText(widgetEntry.getLabel());
            editTo.setText(TextUtils.join(Constants.SEPARATOR_COMMA, widgetEntry.getTo()));
            editSubjectPrefix.setText(widgetEntry.getSubjectPrefix());
            editMessagePrefix.setText(widgetEntry.getMessagePrefix());
        }
    }

    private void cancelClicked() {
        finish();
    }

    private void okClicked() {
        String label = editLabel.getText().toString();
        String to = editTo.getText().toString();
        String subjectPrefix = editSubjectPrefix.getText().toString();
        String messagePrefix = editMessagePrefix.getText().toString();

        boolean hasError = false;
        if (TextUtils.isEmpty(label)) {
            editLabel.setError(getString(R.string.error_mandatory));
            hasError = true;
        }
        if (TextUtils.isEmpty(to)) {
            editTo.setError(getString(R.string.error_mandatory));
            hasError = true;
        }

        if (!hasError) {
            insertOrUpdate(label, to, subjectPrefix, messagePrefix);
            broadcastWidgetUpdate();
            setResultAndFinish();
        }
    }

    private void setResultAndFinish() {
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(Activity.RESULT_OK, resultValue);
        finish();
    }

    private void broadcastWidgetUpdate() {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, this, WidgetProvider.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{widgetId});
        sendBroadcast(intent);
    }

    private void insertOrUpdate(String label, String to, String subjectPrefix, String messagePrefix) {
        boolean doInsert = false;
        if (widgetEntry == null) {
            widgetEntry = new WidgetEntry();
            doInsert = true;
        }
        widgetEntry.setWidgetId(widgetId);
        widgetEntry.setLabel(label);
        widgetEntry.setTo(new String[]{to});
        widgetEntry.setSubjectPrefix(subjectPrefix);
        widgetEntry.setMessagePrefix(messagePrefix);
        if (doInsert) {
            getApp().getWidgetEntryDao().insert(widgetEntry);
        } else {
            getApp().getWidgetEntryDao().update(widgetEntry);
        }
    }

}
