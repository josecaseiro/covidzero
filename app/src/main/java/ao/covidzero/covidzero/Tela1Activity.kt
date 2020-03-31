package ao.covidzero.covidzero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_tela.*

class Tela1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela)

        actionBar?.hide()
        supportActionBar?.hide()

        bt_comecar.setOnClickListener { startActivity(Intent(this@Tela1Activity, LoginActivity::class.java))
            finish()
        }
    }
}
