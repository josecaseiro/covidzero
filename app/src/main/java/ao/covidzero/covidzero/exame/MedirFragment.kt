package ao.covidzero.covidzero.exame

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import ao.covidzero.covidzero.R
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.dialog_temperature.view.*
import java.util.*
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 */
class MedirFragment : Fragment() {
    var running = false
    var finished = false
    var graus = 0.0
    var pading = 0

    var timer = object: CountDownTimer(5000, 100) {

        override fun onFinish() {
            finished = true
            Alerter.create(activity)
                .setTitle("Conclúido")
                .setText("A sua temperatura actual é ${graus.toString().subSequence(0,4)}º C")
                .enableSound(true)
                .setBackgroundColorRes(R.color.green)
                .show()
        }

        override fun onTick(millisUntilFinished: Long) {
            newTemp()
            updateInfo()
        }
    }

    lateinit var v:View

    fun updateInfo(){
        if(graus > 0)
            v.graus.text = "${graus.toString().subSequence(0,4)}º C"
        else
            v.graus.text = "-º C"

        val buttonLayoutParams =
            LinearLayout.LayoutParams(2, MATCH_PARENT)
        buttonLayoutParams.setMargins(pading, 0, 0, 0)
        v.marker.layoutParams=buttonLayoutParams

        v.bt_outra_vez.setOnClickListener {
            graus = 0.0
            pading = 0
            updateInfo()
        }
    }

    private fun getRandomNumberInRange(min: Int, max: Int): Int {
        require(min < max) { "max must be greater than min" }
        val r = Random()
        return r.nextInt(max - min + 1) + min
    }

    fun getRandomWidth():Int{
        return getRandomNumberInRange(30, 99)
    }

    fun newTemp() {
        pading = getRandomWidth()
        graus = 35 + (2 * pading / 99.0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.dialog_temperature, container, false)

        v.bt_finger.setOnTouchListener { v, event ->

            if(event.action == 2 && !running){
                running = true
                finished = false
                timer.start()
            }


            if(event.action == 1 && running){
                running = false
                timer.cancel()

                if(!finished){
                    Alerter.create(activity)
                        .setTitle("Atenção")
                        .enableSound(true)
                        .setText("Continue precionando o dedo para terminar o exame.")
                        .setBackgroundColorRes(R.color.orange)
                        .show()
                }
            }

            Log.d("ev", event.action.toString())
            true
        }

        updateInfo()

        v.bt_cancelar.setOnClickListener {
            activity?.finish()
        }

        return v
    }

}
