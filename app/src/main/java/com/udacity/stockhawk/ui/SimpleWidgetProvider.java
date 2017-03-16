package com.udacity.stockhawk.ui;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.sync.QuoteWidgetIntentService;

import butterknife.BindView;

public class SimpleWidgetProvider extends AppWidgetProvider {

    @BindView(R.id.recycler_view)
    RecyclerView stockRecyclerView;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, QuoteWidgetIntentService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals("com.udacity.stockhawk.ACTION_DATA_UPDATED")) {
            context.startService(new Intent(context, QuoteWidgetIntentService.class));
        }
    }
}
