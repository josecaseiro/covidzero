package ao.covidzero.covidzero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ao.covidzero.covidzero.model.Grupo
import ao.covidzero.covidzero.model.Mensagem
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

class ChatActivity : AppCompatActivity() {

    private var fragment: MensagemFragment? = null
    var grupo: Grupo? = null
    var mensagens = mutableListOf<Mensagem>()

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
                fragment?.addMensagem(nova)
                editText.text.clear()
            }

        }

    }

    private fun loadMensagens() {

        mensagens.add(
            Mensagem("Caseiro", "Situação controlada", "02/03/2020", true)
        )

        if(mensagens.size > 0) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragment = MensagemFragment(mensagens)
            fragmentTransaction.replace(R.id.frame, fragment!!)
            fragmentTransaction.commit()
        }
    }
}
