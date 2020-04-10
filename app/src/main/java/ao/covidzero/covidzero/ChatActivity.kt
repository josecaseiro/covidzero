package ao.covidzero.covidzero

import android.content.Context
import android.net.DnsResolver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import ao.covidzero.covidzero.model.Grupo
import ao.covidzero.covidzero.model.Mensagem
import ao.covidzero.covidzero.model.Profissional
import ao.covidzero.covidzero.network.GetDataService
import ao.covidzero.covidzero.network.HttpClient
import ao.covidzero.covidzero.network.RetrofitClientInstance
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.tapadoo.alerter.Alerter
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_chat.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ChatActivity : AppCompatActivity() {

    private var fragment: MensagemFragment? = null
    var grupo: Grupo? = null
    var profissional:Profissional? = null
    var mensagens = listOf<Mensagem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.hide()

        grupo = intent.getSerializableExtra("grupo") as Grupo?
        profissional = intent.getSerializableExtra("profissional") as Profissional?

        if(grupo != null)
        nome.text = "${grupo?.nome} - ${grupo?.descricao}"
        else if(profissional!=null)
        nome.text = "${profissional?.nome} - ${profissional?.profissao}"

        loadMensagens()

        bt_enviar.setOnClickListener {
            val msg = editText.text.toString()
            if(msg.isNotBlank()){

                Alerter.create(this@ChatActivity)
                    .setTitle("Aguarde")
                    .setText("A enviar mensagem")
                    .setBackgroundColorRes(R.color.orange)
                    .enableProgress(true)
                    .enableInfiniteDuration(true)
                    .show()

                val id = getSharedPreferences("COVID", Context.MODE_PRIVATE).getString("id","")
                val req = RequestParams()
                req.put("grupo", grupo?.id)
                req.put("userId", id)
                req.put("sms", msg)

                HttpClient.post("insideGrupo/${grupo?.id}", req, object : JsonHttpResponseHandler(){
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        response: JSONObject?
                    ) {
                        Alerter.hide()
                        editText.text.clear()
                        Log.d("SMS", response.toString())
                        loadMensagens()
                        super.onSuccess(statusCode, headers, response)
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseString: String?,
                        throwable: Throwable?
                    ) {
                        Alerter.create(this@ChatActivity)
                            .setTitle("Lamentamos")
                            .setText("Não foi possível enviar mensagem. Tente de novo por favor.")
                            .setBackgroundColorRes(R.color.green)
                            .show()
                        Alerter.hide()

                        super.onFailure(statusCode, headers, responseString, throwable)
                    }
                })


            }
        }

    }

    private fun loadMensagens() {

        Alerter.create(this@ChatActivity)
            .setTitle("Aguarde")
            .setText("A carregar mensagens do grupo")
            .setBackgroundColorRes(R.color.orange)
            .enableProgress(true)
            .enableInfiniteDuration(true)
            .show()

        val service =
            RetrofitClientInstance.getRetrofitInstance().create(
                GetDataService::class.java
            )

        var call:Call<List<Mensagem>>? = null

        if(grupo!=null)
         call = service.grupoSms(grupo?.id!!)
        else if (profissional !=null) {
            call = service.grupoSms(profissional?.id!!)
        }

        call?.enqueue(object : Callback<List<Mensagem>> {
            override fun onFailure(call: Call<List<Mensagem>>, t: Throwable) {
                Alerter.create(this@ChatActivity)
                    .setTitle("Lamentamos")
                    .setText("Não foi possível carregar mensagens")
                    .addButton("Tentar de novo", R.style.AlertButton , View.OnClickListener {
                        loadMensagens()
                    })
                    .enableInfiniteDuration(true)
                    .setBackgroundColorRes(R.color.red)
                    .show()
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<List<Mensagem>>,
                response: Response<List<Mensagem>>
            ) {

                Alerter.hide()
                response.body()?.let {
                    mensagens = it

                    if(mensagens.size > 0) {
                        val fragmentManager = supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragment = MensagemFragment(it.toMutableList())
                        fragmentTransaction.replace(R.id.frame, fragment!!)
                        fragmentTransaction.commit()
                    }
                }


            }
        })



    }
}
