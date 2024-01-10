package com.example.tigerwidgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class TigerImageWidget : AppWidgetProvider() {


    companion object {

        private var mediaPlayer: MediaPlayer? = null
        private var currentSoundIndex = 0
        private val soundResources = intArrayOf(R.raw.dog, R.raw.rocket)
        private var isPlaying = false

        private val images = arrayOf(R.drawable.resource_do, R.drawable.dont);
        private var currentIndex = 0;
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private fun makePendingIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(context, TigerImageWidget::class.java)
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.tiger_image_widget)

        val url = "https://pja.edu.pl/dla-kandydata/"

        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.parse(url)

        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        views.setOnClickPendingIntent(R.id.button2, pendingIntent)

        // IMAGE
        views.setImageViewResource(R.id.imageView, images[currentIndex]);
        views.setOnClickPendingIntent(R.id.imageView, makePendingIntent(context, "CHANGE_IMAGE"))

        // MUSIC
        views.setTextViewText(R.id.playStatus, (if(isPlaying) "Playing: " else "") + "Track $currentSoundIndex")
        views.setOnClickPendingIntent(R.id.btnPlay, makePendingIntent(context, "PLAY"))
        views.setOnClickPendingIntent(R.id.btnStop, makePendingIntent(context, "STOP"))
        views.setOnClickPendingIntent(R.id.btnNext, makePendingIntent(context, "NEXT"))

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun triggerUpdate(context: Context)
    {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds =
            appWidgetManager.getAppWidgetIds(
                ComponentName(
                    context,
                    TigerImageWidget::class.java
                )
            )
        onUpdate(context, appWidgetManager, appWidgetIds)
    }

    private fun play(context: Context) {

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, soundResources[currentSoundIndex])
            mediaPlayer?.setOnCompletionListener {
                if(currentSoundIndex == soundResources.size - 1)
                    isPlaying = false
                next(context)
                triggerUpdate(context)
            }
        }
        isPlaying = true
        mediaPlayer?.start()
    }

    private fun stop() {

        if (isPlaying) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            isPlaying = false
        }
    }

    private fun next(context: Context) {
        var wasPlaying = isPlaying
        stop();
        currentSoundIndex = (currentSoundIndex + 1) % soundResources.size
        if (wasPlaying)
            play(context);
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            "CHANGE_IMAGE" -> {
                currentIndex = 1 - currentIndex
            }

            "PLAY" -> {
                play(context);
            }

            "STOP" -> {
                stop();
            }

            "NEXT" -> {
                next(context);
            }
        }

        triggerUpdate(context)
    }
}