package ao.covidzero.covidzero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_number_verification.*

class NumberVerification : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_verification)

        button2.setOnClickListener {
            val txt = editTextNumber.text.toString()
            if(txt.trim().isNotEmpty()){
                val intent = Intent()
                intent.putExtra("code", txt)
                setResult(1000, intent)
                finish()
            }
        }
    }
}