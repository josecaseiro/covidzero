package ao.covidzero.covidzero

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*


class InicarAlarme : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        val calendar = Calendar.getInstance()

        calendar.set(
            calendar.get(Calendar.YEAR)
            , calendar.get(Calendar.MONTH)
            , calendar.get(Calendar.DAY_OF_MONTH)
            , calendar.get(Calendar.HOUR_OF_DAY), 0
        )

        val alarme: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarme.setRepeating(
            AlarmManager.RTC, calendar.timeInMillis, 1000 * 60 * 15
            , PendingIntent.getActivity(
                context, 45, Intent("android.intent.actionao.covidzero.covidzero.InicarNotificao")
                , PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }

}
