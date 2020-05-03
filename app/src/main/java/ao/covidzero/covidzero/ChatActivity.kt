package ao.covidzero.covidzero

import android.content.Context
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
import com.google.gson.Gson
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.tapadoo.alerter.Alerter
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_chat.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ChatActivity : AppCompatActivity() {

    private var fragment: MensagemFragment? = null
    var grupo: Grupo? = null
    var profissional:Profissional? = null
    var mensagens = mutableListOf<Mensagem>()

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

                val id = getSharedPreferences("COVID", Context.MODE_PRIVATE).getString("id",null)
                val profid = getSharedPreferences("COVID", Context.MODE_PRIVATE).getString("psId",null)
                val req = RequestParams()

                var to = grupo?.id
                var route = "insideGrupo/${grupo?.id}"

                req.put("grupo", grupo?.id)
                req.put("userId", id)
                req.put("sms", msg)

                if(to == null && profissional != null){
                    route = "accaoUser/sendSms"

                    req.put("de",  if(id != null) id else profid )
                    req.put("psId", profissional!!.id)
                    req.put("userId", if(id != null) id else profid )
                }

                HttpClient.post(route, req, object : JsonHttpResponseHandler(){
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

                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        response: JSONArray?
                    ) {
                        response?.let {
                            Log.d("SMS RESPONSE", it.toString())
                        }
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


        val prefs = getSharedPreferences("COVID", Context.MODE_PRIVATE)
        val user_id = prefs.getString("id", "")
        val prof_id = prefs.getString("psId", "");

        var url = "insideGrupo/${grupo?.id}"

        if (grupo == null) {
            if (profissional !=null) {
                url = "accaoUser/sms/$user_id/${profissional!!.id}"
            }
        }

        HttpClient.get(url, null, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {

                response?.let {
                    Log.d("RES", it.toString())
                }

                Alerter.create(this@ChatActivity)
                    .setTitle("Lamentamos")
                    .setText("Sem mensagens")
                    .setBackgroundColorRes(R.color.orange)
                    .show()

            }

            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONArray?
            ) {
                Alerter.hide()

                response?.let {

                    Log.d("Msg", it.toString())
                    val gson = Gson()
                    mensagens = mutableListOf()

                    var i = 0
                    while (i < it.length()){
                        mensagens.add(gson.fromJson<Mensagem>(it.getJSONObject(i).toString(), Mensagem::class.java ))
                        i++
                    }

                    if(mensagens.size > 0) {
                        val fragmentManager = supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragment = MensagemFragment(mensagens)
                        fragmentTransaction.replace(R.id.frame, fragment!!)
                        fragmentTransaction.commit()
                    }
                }

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
                    .setText("Não foi possível carregar mensagens")
                    .addButton("Tentar de novo", R.style.AlertButton , View.OnClickListener {
                        loadMensagens()
                    })
                    .enableInfiniteDuration(true)
                    .setBackgroundColorRes(R.color.red)
                    .show()
                throwable?.printStackTrace()
            }
        })




    }
}
