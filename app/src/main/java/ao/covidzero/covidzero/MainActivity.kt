package ao.covidzero.covidzero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_relatorio.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        processarRelatorioGeral()

        generateMenu()

    }

    private fun generateMenu() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = MenuItemFragment()
        fragmentTransaction.replace(R.id.frag_frame, fragment)
        fragmentTransaction.commit()
    }

    private fun processarRelatorioGeral() {

        confirmado?.setQtd(8)
        confirmado?.setIcone(0)
        confirmado?.setNome("Confirmado")

        return



    }
}
