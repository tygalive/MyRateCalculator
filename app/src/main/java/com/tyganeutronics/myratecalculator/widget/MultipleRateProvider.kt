package com.tyganeutronics.myratecalculator.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.google.firebase.analytics.FirebaseAnalytics
import com.tyganeutronics.myratecalculator.R
import com.tyganeutronics.myratecalculator.activities.MainActivity
import com.tyganeutronics.myratecalculator.contract.CurrencyContract
import com.tyganeutronics.myratecalculator.utils.BaseUtils
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class MultipleRateProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        if (appWidgetIds != null) {
            for (appWidgetId in appWidgetIds) {

                if (context != null && appWidgetManager != null) {
                    updateWidget(context, appWidgetManager, appWidgetId)
                }

            }
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)

        if (context != null) {
            FirebaseAnalytics.getInstance(context).logEvent("add_multiple_widget", Bundle())
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (context != null) {

            val appWidgetManager = AppWidgetManager.getInstance(context)

            val componentName = ComponentName(context, MultipleRateProvider::class.java)

            val appWidgetIds: IntArray? = appWidgetManager.getAppWidgetIds(componentName)

            if (appWidgetIds != null) {

                for (appWidgetId in appWidgetIds) {
                    updateWidget(context, appWidgetManager, appWidgetId)
                }
            }
        }
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {

        val views = RemoteViews(context.packageName, R.layout.widget_multiple)

        //rates
        views.setTextViewText(
            R.id.txt_bond,
            getStoredValue(
                context, context.getString(R.string.currency_bond)
            )
        )
        views.setTextViewText(
            R.id.txt_omir,
            getStoredValue(
                context, context.getString(R.string.currency_omir)
            )
        )
        views.setTextViewText(
            R.id.txt_rtgs,
            getStoredValue(
                context, context.getString(R.string.currency_rtgs)
            )
        )
        views.setTextViewText(
            R.id.txt_rbz,
            getStoredValue(
                context, context.getString(R.string.currency_rbz)
            )
        )
        views.setTextViewText(
            R.id.txt_zar,
            getStoredValue(
                context, context.getString(R.string.currency_zar)
            )
        )

        //date
        val last = BaseUtils.getPrefs(context).getLong(
            CurrencyContract.LAST_CHECK,
            System.currentTimeMillis()
        )

        val instant = Instant.ofEpochMilli(last)
        val format =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(format)

        views.setTextViewText(R.id.txt_date_checked, date)

        //pending intent
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        views.setOnClickPendingIntent(R.id.widget_main, pendingIntent)

        //update widget
        appWidgetManager.updateAppWidget(appWidgetId, views)

    }

    private fun getStoredValue(context: Context, key: String): String? {
        return String.format("%10.2f", BaseUtils.getPrefs(context).getString(key, "1")?.toDouble())
    }
}