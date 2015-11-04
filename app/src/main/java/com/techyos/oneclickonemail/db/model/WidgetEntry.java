package com.techyos.oneclickonemail.db.model;

import java.util.Arrays;

/**
 * Created by ylemin on 26/10/15.
 *
 * This is the model object representing the widget entry in the database
 */

public class WidgetEntry {

    /**
     * Attributes
     */

    private long id;
    private int widgetId;
    private String label;
    private String[] to;
    private String subjectPrefix;
    private String messagePrefix;

    /**
     * Accessors
     */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(int widgetId) {
        this.widgetId = widgetId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String getSubjectPrefix() {
        return subjectPrefix;
    }

    public void setSubjectPrefix(String subjectPrefix) {
        this.subjectPrefix = subjectPrefix;
    }

    public String getMessagePrefix() {
        return messagePrefix;
    }

    public void setMessagePrefix(String messagePrefix) {
        this.messagePrefix = messagePrefix;
    }

    /**
     * Overridden Object Methods
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WidgetEntry)) return false;

        WidgetEntry that = (WidgetEntry) o;

        if (id != that.id) return false;
        if (widgetId != that.widgetId) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(to, that.to)) return false;
        if (subjectPrefix != null ? !subjectPrefix.equals(that.subjectPrefix) : that.subjectPrefix != null)
            return false;
        return !(messagePrefix != null ? !messagePrefix.equals(that.messagePrefix) : that.messagePrefix != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + widgetId;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (to != null ? Arrays.hashCode(to) : 0);
        result = 31 * result + (subjectPrefix != null ? subjectPrefix.hashCode() : 0);
        result = 31 * result + (messagePrefix != null ? messagePrefix.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WidgetEntry{" +
                "id=" + id +
                ", widgetId=" + widgetId +
                ", label='" + label + '\'' +
                ", to=" + Arrays.toString(to) +
                ", subjectPrefix='" + subjectPrefix + '\'' +
                ", messagePrefix='" + messagePrefix + '\'' +
                '}';
    }
}
