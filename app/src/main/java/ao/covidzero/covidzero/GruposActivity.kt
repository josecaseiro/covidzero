package ao.covidzero.covidzero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ao.covidzero.covidzero.model.Grupo

class GruposActivity : AppCompatActivity() {

    var grupos = mutableListOf<Grupo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grupos)

        supportActionBar?.hide()
        grupos.add(Grupo(12, "Miro", "","","","","dd"))
        loadGrupos()
    }

    public fun addGrupo(grupo:Grupo){
        grupos.add(grupo)
        loadGrupos()
    }


    private fun loadGrupos() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = if (grupos.size == 0)  SemGrupoFragment() else  GrupoFragment(grupos)
        fragmentTransaction.replace(R.id.frag_frame, fragment)
        fragmentTransaction.commit()
    }
}
