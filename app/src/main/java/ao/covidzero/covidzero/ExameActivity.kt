package ao.covidzero.covidzero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import ao.covidzero.covidzero.exame.*
import kotlinx.android.synthetic.main.dialog_temperature.*

class ExameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exame)

        supportActionBar?.hide()

        val fragment = ComoEstasFragment()
        show(fragment)

    }

    fun show(frag:Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frag_frame, frag)
        fragmentTransaction.commit()
    }

    fun mostrarPerguntasContacto() {
        val fragment = PerguntasContactoFragment()
        show(fragment)
    }

    fun mostrarPerguntasOqueSente() {
        val fragment = PerguntasOqueSenteFragment()
        show(fragment)
    }

    fun showBem() {
        show(BemFragment())
    }

    fun showMal() {
        show(MalFragment())
    }
}
