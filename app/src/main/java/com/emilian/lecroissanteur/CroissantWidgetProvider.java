package com.emilian.lecroissanteur;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Implementation of App Widget functionality.
 */
public class CroissantWidgetProvider extends AppWidgetProvider {

    public static final String ON_RECEIVE_TAG = "RECEIVER_LOG";
    public static final String ON_UPDATE_TAG = "UPDATE_LOG";
    public static final String ON_ENABLED_TAG = "ENABLED_LOG";
    public static final String ON_DISABLED_TAG = "DISABLED_TAG";
    public static final String WIDGET_CLICK_SOUND = "com.emilian.action.CROISSANT_CLICK_SOUND";
    static MediaPlayer croissantMP;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(ON_RECEIVE_TAG, "entered onReceive, intent action is: " + intent.getAction());

        if(WIDGET_CLICK_SOUND.equals(intent.getAction())){
            //croissantMP = MediaPlayer.create(context, R.raw.croisssssant);

            try {
                if (croissantMP.isPlaying()) {
                    croissantMP.pause();
                    croissantMP.seekTo(0);
                }
                // Previous approach which loaded the sound file into the MediaPlayer object
                // each time the user tapped on the widget, leading to memory release issues
                /*if (croissantMP.isPlaying()) {
                    croissantMP.stop();
                    croissantMP.release();
                    croissantMP = MediaPlayer.create(context, R.raw.croisssssant);
                }*/
                croissantMP.start();
                Log.d(ON_RECEIVE_TAG, "audio file started playing");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d(ON_UPDATE_TAG, "Entered updateAppWidget function");

        Intent playCroissantSoundIntent = new Intent(context, CroissantWidgetProvider.class);
        playCroissantSoundIntent.setAction(WIDGET_CLICK_SOUND);
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
        Log.d(ON_ENABLED_TAG, "Entered onEnabled");

        // Create MediaPlayer object here only once, when the widget is
        // created and load the sound file into it only once
        if(croissantMP == null) {
            croissantMP = MediaPlayer.create(context, R.raw.croisssssant);
        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d(ON_DISABLED_TAG, "Entered onDisabled");

        if(croissantMP != null) {
            croissantMP.reset();
            croissantMP.release();
            croissantMP = null;
        }
    }
}

