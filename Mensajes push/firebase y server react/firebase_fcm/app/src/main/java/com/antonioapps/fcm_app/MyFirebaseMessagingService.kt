package com.antonioapps.fcm_app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // Obtener los datos del mensaje
        val data = message.data
        val notification = message.notification

        // Crear un canal de notificaciones (para Android 8.0+)
        val channelId = "my_channel_id"
        val channelName = "My Channel"
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Construir la notificación
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Reemplaza con tu propio ícono
            .setContentTitle(notification?.title ?: data["title"]) // Usa el título de la notificación si está disponible, de lo contrario usa el título del mensaje recibido
            .setContentText(notification?.body ?: data["mensaje"]) // Usa el cuerpo de la notificación si está disponible, de lo contrario usa el cuerpo del mensaje recibido
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Mostrar la notificación
        notificationManager.notify(0, notificationBuilder.build())
    }
}
