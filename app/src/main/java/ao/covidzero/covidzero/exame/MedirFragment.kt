package ao.covidzero.covidzero.exame

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ao.covidzero.covidzero.R
import kotlinx.android.synthetic.main.dialog_temperature.view.*

/**
 * A simple [Fragment] subclass.
 */
class MedirFragment : Fragment() {
    var running = false
    var finished = false
    var graus = 37

    var timer = object: CountDownTimer(5000, 100) {

        override fun onFinish() {
            finished = true
        }

        override fun onTick(millisUntilFinished: Long) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.dialog_temperature, container, false)

        v.bt_finger.setOnTouchListener { v, event ->

            if(event.action == 2 && !running){
                running = true
                finished = false
                timer.start()
            }


            if(event.action == 1 && running){
                running = false
                timer.cancel()
            }

            Log.d("ev", event.action.toString())
            true
        }
        return v
    }

}
