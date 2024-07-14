package com.ironclad.saksham.services

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ironclad.saksham.R

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class PushMessagingService: FirebaseMessagingService() {
    companion object {
        private const val CHANNEL_ID = "SakshamChannelId"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        generateNotification(message.notification!!.title!!, message.notification!!.body!!)

    }


    //Works for version >= Oreo
    private fun generateNotification(title:String, text:String){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))

        notificationManager.notify(0, notificationBuilder.build())
    }
}