package ao.covidzero.covidzero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ao.covidzero.covidzero.exame.ComoEstasFragment

class ExameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exame)

        supportActionBar?.hide()

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = ComoEstasFragment()
        fragmentTransaction.replace(R.id.frag_frame, fragment)
        fragmentTransaction.commit()
    }
}
