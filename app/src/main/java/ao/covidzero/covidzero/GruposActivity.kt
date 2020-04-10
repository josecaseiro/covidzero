package ao.covidzero.covidzero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ao.covidzero.covidzero.model.Grupo
import ao.covidzero.covidzero.network.GetDataService
import ao.covidzero.covidzero.network.HttpClient
import ao.covidzero.covidzero.network.RetrofitClientInstance
import com.google.android.material.snackbar.Snackbar
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.tapadoo.alerter.Alerter
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_grupos.*
import kotlinx.android.synthetic.main.dialog_nova_comunidade.*
import org.json.JSONObject
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

        floatingActionButton.setOnClickListener {
            val dg = android.app.Dialog( this@GruposActivity )
            dg.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)

            dg.setContentView(R.layout.dialog_nova_comunidade)
            dg.bt_cancelar.setOnClickListener {
                dg.dismiss()
            }

            dg.bt_criar.setOnClickListener {
                val nome = dg.edt_nome.text.toString()
                val actividade = dg.edt_actividade.text.toString()
                val provincia = dg.edt_provincia.text.toString()
                val municipio = dg.edt_municipio.text.toString()
                val bairro = dg.edt_bairro.text.toString()
                val descricao = dg.edt_descricao.text.toString()

                if( nome.isBlank() || actividade.isBlank() || provincia.isBlank() || municipio.isBlank() ||bairro.isBlank() || descricao.isBlank() )
                    Snackbar.make(dg.bt_criar,"Preencha todos os campos correctamente.",Snackbar.LENGTH_LONG).show()
                else {

                    Alerter.create(this@GruposActivity)
                        .setTitle("Aguarde")
                        .setText("A criar grupo")
                        .setBackgroundColorRes(R.color.orange)
                        .enableProgress(true)
                        .enableInfiniteDuration(true)
                        .show()

                    val params = RequestParams()
                    params.put("nome", nome)
                    params.put("atividade", actividade)
                    params.put("provincia", provincia)
                    params.put("municipio", municipio)
                    params.put("bairro", bairro)
                    params.put("descricao", descricao)

                    HttpClient.post("grupos/", params, object : JsonHttpResponseHandler(){
                        override fun onSuccess(
                            statusCode: Int,
                            headers: Array<out Header>?,
                            response: JSONObject?
                        ) {

                            Alerter.hide()
                            loadGrupos()
                            dg.dismiss()

                        }

                        override fun onFailure(
                            statusCode: Int,
                            headers: Array<out Header>?,
                            responseString: String?,
                            throwable: Throwable?
                        ) {
                            dg.dismiss()

                            Alerter.create(this@GruposActivity)
                                .setTitle("Lamentamos")
                                .setText("Não foi possível criar o grupo. Tente de novo por favor.")
                                .setBackgroundColorRes(R.color.red)
                                .show()

                            super.onFailure(statusCode, headers, responseString, throwable)
                        }
                    })
                }
            }

            dg.show()

        }
    }

    fun addGrupo(){
        floatingActionButton.callOnClick()
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
