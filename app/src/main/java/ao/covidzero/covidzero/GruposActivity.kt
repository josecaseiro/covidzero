package ao.covidzero.covidzero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ao.covidzero.covidzero.model.Grupo
import ao.covidzero.covidzero.network.GetDataService
import ao.covidzero.covidzero.network.RetrofitClientInstance
import com.tapadoo.alerter.Alerter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GruposActivity : AppCompatActivity() {

    var grupos:List<Grupo> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grupos)

        supportActionBar?.hide()
        //grupos.add(Grupo(12, "Miro", "","","","","dd"))
        loadGrupos()
    }

    public fun addGrupo(grupo:Grupo){
        loadGrupos()
    }


    private fun loadGrupos() {
        Alerter.create(this@GruposActivity)
            .setTitle("Aguarde")
            .setText("A carregar grupos")
            .setBackgroundColorRes(R.color.orange)
            .enableProgress(true)
            .enableInfiniteDuration(true)
            .show()

        val service =
            RetrofitClientInstance.getRetrofitInstance().create(
                GetDataService::class.java
            )

        val call = service.grupos()

        call.enqueue(object: Callback<List<Grupo>>{
            override fun onFailure(call: Call<List<Grupo>>, t: Throwable) {
                Alerter.create(this@GruposActivity)
                    .setTitle("Lamentamos")
                    .enableInfiniteDuration(true)
                    .setText("Não foi possível descarregar a lista de grupos comunitários")
                    .addButton("Tentar de novo", R.style.AlertButton , View.OnClickListener {
                        loadGrupos()
                    })                    .setBackgroundColorRes(R.color.red)
                    .show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Grupo>>, response: Response<List<Grupo>>) {
               Alerter.hide()
                response.body()?.let {
                    grupos = it

                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = if (grupos.size == 0)  SemGrupoFragment() else  GrupoFragment(grupos)
                    fragmentTransaction.replace(R.id.frag_frame, fragment)
                    fragmentTransaction.commit()
                }

            }
        })

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = if (grupos.size == 0)  SemGrupoFragment() else  GrupoFragment(grupos)
        fragmentTransaction.replace(R.id.frag_frame, fragment)
        fragmentTransaction.commit()
    }
}
