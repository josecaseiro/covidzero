package ao.covidzero.covidzero

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class InicarAlarme : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        Gestor.setContext(context)
        val alarme: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarme.setRepeating(
            AlarmManager.RTC, Gestor.alarme, 1000 * 60 * 15
            , PendingIntent.getActivity(
                context, 45, Intent("android.intent.action.covidzero.covidzero.ActivityAlarme")
                , PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

       // val mBuilder = NotificationCompat.Builder(context, "123")
           // .setSmallIcon(R.drawable.icon)
          //  .setContentTitle("Title")
          //  .setContentText("Executando um AlarmManager")
         //   .setAutoCancel(true)

      //  val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
     //   mNotificationManager.notify(100, mBuilder.build())
    }

}
