package com.techyos.oneclickonemail.adapters;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techyos.oneclickonemail.Constants;
import com.techyos.oneclickonemail.R;
import com.techyos.oneclickonemail.db.model.WidgetEntry;

import java.util.Collection;
import java.util.List;

/**
 * Created by ylemin on 28/10/15.
 *
 * This is the adapter that will manage the display of the widget entries in the database.
 *
 */
public class WidgetListAdapter extends RecyclerView.Adapter<WidgetListAdapter.ViewHolder> {

    /**
     * Attributes
     */

    private Context context;
    private LayoutInflater layoutInflater;
    private List<WidgetEntry> widgetEntries;
    private View.OnClickListener onClickListener;

    /**
     * Constructors
     */

    public WidgetListAdapter(Context context, List<WidgetEntry> widgetEntries, View.OnClickListener onClickListener) {
        this.context = context;
        this.widgetEntries = widgetEntries;
        this.onClickListener = onClickListener;
    }

    /**
     * Public Methods
     */

    public void replaceAllEntries(Collection<WidgetEntry> items) {
        if (items != null && !items.isEmpty()) {
            widgetEntries.clear();
            widgetEntries.addAll(items);
            notifyDataSetChanged();
        }
    }

    /**
     * Overridden Methods: RecyclerView.Adapter
     */

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view = layoutInflater.inflate(R.layout.item_widget_entry, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WidgetEntry entry = widgetEntries.get(position);

        holder.itemView.setTag(entry.getWidgetId());
        holder.textLabel.setText(entry.getLabel());
        formatWithPrefix(R.string.text_to_prefix, TextUtils.join(Constants.SEPARATOR_COMMA, entry.getTo()), holder.textTo);
        formatWithPrefix(R.string.text_subject_prefix, entry.getSubjectPrefix(), holder.textSubjectPrefix);
        formatWithPrefix(R.string.text_message_prefix, entry.getMessagePrefix(), holder.textMessagePrefix);
    }

    @Override
    public int getItemCount() {
        return widgetEntries.size();
    }

    /**
     * Private Methods
     */

    private void formatWithPrefix(@StringRes int prefixId, String value, TextView view) {
        view.setText(String.format("%s: %s", context.getString(prefixId),
                TextUtils.isEmpty(value) ? context.getString(R.string.text_empty) : value));
    }

    /**
     * Inner Classes
     */

    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Attributes
         */

        TextView textLabel;
        TextView textTo;
        TextView textSubjectPrefix;
        TextView textMessagePrefix;

        /**
         * Constructors
         */

        public ViewHolder(View itemView) {
            super(itemView);

            textLabel = (TextView) itemView.findViewById(R.id.textLabel);
            textTo = (TextView) itemView.findViewById(R.id.textTo);
            textSubjectPrefix = (TextView) itemView.findViewById(R.id.textSubjectPrefix);
            textMessagePrefix = (TextView) itemView.findViewById(R.id.textMessagePrefix);
        }
    }

}
