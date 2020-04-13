package ao.covidzero.covidzero

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class InicarNotificao : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        NewMessageNotification.notify(
            context,
            "Covid-zero",
            567
        )
    }

}
