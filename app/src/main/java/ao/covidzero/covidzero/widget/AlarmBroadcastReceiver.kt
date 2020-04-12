package ao.covidzero.covidzero.widget

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import ao.covidzero.covidzero.MainActivity
import ao.covidzero.covidzero.R

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create the NotificationChannel
            val name = "Alarme"
            val descriptionText = "Detalhes do Alarme"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("AlarmId", name, importance)
            mChannel.description = descriptionText
            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        // Create the notification to be shown
        val mBuilder = NotificationCompat.Builder(context!!, "AlarmId")
            .setSmallIcon(R.mipmap.ic_covid)
            .setContentTitle("Covid Zero")
            .setContentText("Lave as maoes com agua e sabao")
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(
                context, // Context from onReceive method.
                0,
                Intent(context, MainActivity::class.java), // Activity you want to launch onClick.
                0
               )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        // Get the Notification manager service
        val am = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Generate an Id for each notification
        val id = System.currentTimeMillis() / 1000

        // Show a notification
        am.notify(id.toInt(), mBuilder.build())
    }
}