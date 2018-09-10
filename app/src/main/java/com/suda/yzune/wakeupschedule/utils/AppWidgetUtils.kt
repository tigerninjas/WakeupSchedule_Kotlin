package com.suda.yzune.wakeupschedule.utils

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.suda.yzune.wakeupschedule.R
import com.suda.yzune.wakeupschedule.schedule_appwidget.ScheduleAppWidgetService

object AppWidgetUtils {
    fun refreshScheduleWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val mRemoteViews = RemoteViews(context.packageName, R.layout.schedule_app_widget)

        val week = CourseUtils.countWeek(context.applicationContext)
        val date = CourseUtils.getTodayDate()
        val weekDay = CourseUtils.getWeekday()
        mRemoteViews.setTextViewText(R.id.tv_date, date)
        if (week > 0) {
            mRemoteViews.setTextViewText(R.id.tv_week, "第${week}周    $weekDay")
        } else {
            mRemoteViews.setTextViewText(R.id.tv_week, "还没有开学哦")
        }

        val showWeekend = PreferenceUtils.getBooleanFromSP(context.applicationContext, "s_show_weekend", true)
        if (showWeekend) {
            mRemoteViews.setViewVisibility(R.id.tv_title7, View.VISIBLE)
            if (PreferenceUtils.getBooleanFromSP(context.applicationContext, "s_sunday_first", false)) {
                mRemoteViews.setViewVisibility(R.id.tv_title7, View.GONE)
                mRemoteViews.setViewVisibility(R.id.title0, View.VISIBLE)
            } else {
                mRemoteViews.setViewVisibility(R.id.tv_title7, View.VISIBLE)
                mRemoteViews.setViewVisibility(R.id.title0, View.GONE)
            }
        } else {
            mRemoteViews.setViewVisibility(R.id.tv_title7, View.GONE)
        }

        val showSat = PreferenceUtils.getBooleanFromSP(context.applicationContext, "s_show_sat", true)
        if (showSat) {
            mRemoteViews.setViewVisibility(R.id.tv_title6, View.VISIBLE)
        } else {
            mRemoteViews.setViewVisibility(R.id.tv_title6, View.GONE)
        }

        if (PreferenceUtils.getBooleanFromSP(context.applicationContext, "s_widget_color", false)) {
            mRemoteViews.setTextColor(R.id.tv_date, context.resources.getColor(R.color.white))
            mRemoteViews.setTextColor(R.id.tv_week, context.resources.getColor(R.color.white))
            for (i in 0 until 7) {
                mRemoteViews.setTextColor(R.id.title1 + i, context.resources.getColor(R.color.white))
            }
        } else {
            mRemoteViews.setTextColor(R.id.tv_date, context.resources.getColor(R.color.black))
            mRemoteViews.setTextColor(R.id.tv_week, context.resources.getColor(R.color.black))
            for (i in 0 until 7) {
                mRemoteViews.setTextColor(R.id.title1 + i, context.resources.getColor(R.color.black))
            }
        }

        val lvIntent = Intent(context, ScheduleAppWidgetService::class.java)
        mRemoteViews.setRemoteAdapter(R.id.lv_schedule, lvIntent)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_schedule)
        appWidgetManager.updateAppWidget(appWidgetId, mRemoteViews)
    }
}