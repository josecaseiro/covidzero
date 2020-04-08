package ao.covidzero.covidzero

import android.net.DnsResolver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ao.covidzero.covidzero.model.Grupo
import ao.covidzero.covidzero.model.Mensagem
import ao.covidzero.covidzero.network.GetDataService
import ao.covidzero.covidzero.network.RetrofitClientInstance
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_chat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ChatActivity : AppCompatActivity() {

    private var fragment: MensagemFragment? = null
    var grupo: Grupo? = null
    var mensagens = listOf<Mensagem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.hide()

        grupo = intent.getSerializableExtra("grupo") as Grupo

        nome.text = "${grupo?.nome} - ${grupo?.descricao}"

        loadMensagens()

        bt_enviar.setOnClickListener {
            val msg = editText.text.toString()
            if(msg.isNotBlank()){
                val nova =  Mensagem("Utilizador", msg, Date().toString() )



                //fragment?.addMensagem(nova)
                editText.text.clear()
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

        val call = service.grupoSms(grupo?.id!!)

        call.enqueue(object : Callback<List<Mensagem>> {
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
