package com.example.wellbeingapplocal

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import kotlinx.coroutines.channels.Channel
import kotlin.random.Random

class AlarmReceiver : BroadcastReceiver() {

    private val CHANNELID = "WellbeingAppLocal_Channel"
    private val ChannelDescription = "Notification channel for Wellbeing Reminders"

    //private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            showPushNotification(context)
        }
    }

    private fun showPushNotification(context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val remindersEnabled = sharedPreferences.getBoolean("reminders-enabled", true)

        if(remindersEnabled) {
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            //Check if notification channel already exists, if not create it
            if (notificationManager.getNotificationChannel(CHANNELID) != null) {
                createNotificationChannel(context)
            }

            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            var builder = NotificationCompat.Builder(context, CHANNELID)
                .setContentTitle("Daily Check=in Reminder")
                .setContentText("Looks like it's time for your daily check-in! Tap here to open the app and get started.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            //Generate random notificationId
            val notificationId = Random.nextInt(100, 1000)

            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build())
            }
        } else {

        }
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNELID
            val descriptionText = ChannelDescription
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNELID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}