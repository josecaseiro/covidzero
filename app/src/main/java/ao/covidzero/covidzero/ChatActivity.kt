package ao.covidzero.covidzero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ao.covidzero.covidzero.model.Grupo
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    var grupo: Grupo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.hide()

        grupo = intent.getSerializableExtra("grupo") as Grupo

        nome.text = "${grupo?.nome} - ${grupo?.descricao}"
    }
}
