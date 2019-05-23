package com.emilian.lecroissanteur;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class CroissantWidgetProvider extends AppWidgetProvider implements MediaPlayer.OnErrorListener {

    public static final String WIDGET_PROVIDER_TAG = "WID_PROVIDER_LOG";
    public static final String WIDGET_TAP_ACTION = "com.emilian.action.CROISSANT_WIDGET_TAP";
    static MediaPlayer croissantMP;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(WIDGET_PROVIDER_TAG, "entered onReceive, intent action is: " + intent.getAction());

        if(WIDGET_TAP_ACTION.equals(intent.getAction())){
            try {
                if (croissantMP == null) {
                    // perhaps some time has passed and the media player has been "detached" from
                    // widget; reinitialise the media player object, ready to play the sound again
                    croissantMP = MediaPlayer.create(context, R.raw.croisssssant);
                    croissantMP.setOnErrorListener(this);
                }
                else if (croissantMP.isPlaying()) {
                    croissantMP.pause();
                    croissantMP.seekTo(0);
                }
                croissantMP.start();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d(WIDGET_PROVIDER_TAG, "Entered updateAppWidget function");

        Intent playCroissantSoundIntent = new Intent(context, CroissantWidgetProvider.class);
        playCroissantSoundIntent.setAction(WIDGET_TAP_ACTION);
        playCroissantSoundIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent soundPendingIntent = PendingIntent.getBroadcast(
                context, appWidgetId,
                playCroissantSoundIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.croissant_sound_widget);
        views.setOnClickPendingIntent(R.id.widget_button, soundPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d(WIDGET_PROVIDER_TAG, "Entered onEnabled");

        // Create MediaPlayer object here only once, when the widget is
        // created and load the sound file into it only once
        if(croissantMP == null) {
            croissantMP = MediaPlayer.create(context, R.raw.croisssssant);
            croissantMP.setOnErrorListener(this);
        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d(WIDGET_PROVIDER_TAG, "Entered onDisabled");

        if(croissantMP != null) {
            croissantMP.stop();
            croissantMP.release();
            croissantMP = null;
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.e(WIDGET_PROVIDER_TAG, "entered onError");
        Log.e(WIDGET_PROVIDER_TAG, Integer.toString(i));
        Log.e(WIDGET_PROVIDER_TAG, Integer.toString(i1));
        return false;
    }
}

