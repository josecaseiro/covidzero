package ao.covidzero.covidzero


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity

class ActivityAlarme : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarme)


        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if ( vibrator.hasVibrator()) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(1000000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(1000000)
        }

       NewMessageNotification.notify(
         applicationContext,
         "Covid Zero",
         567
      )

    }

}
