package ao.covidzero.covidzero

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.widget.Toast
import ao.covidzero.covidzero.model.Dado
import ao.covidzero.covidzero.network.GetDataService
import ao.covidzero.covidzero.network.RetrofitClientInstance
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        actionBar?.hide()
        supportActionBar?.hide()

        bt_cadastrar.setOnClickListener {
            makeLogin()


        }


    }

    private fun makeLogin() {
        val phone = edt_phone.text.toString()
        val senha = edt_senha.text.toString()

        if(phone.isBlank() || senha.isBlank() || phone.length < 9 || !PhoneNumberUtils.isGlobalPhoneNumber(phone))
        {
            mostrarErro("Preencha todos os campos correctamente por favor")

        }
else
        if(senha.length < 6 )
        {
            mostrarErro("A senha precisa ter 6 caracteres no mínimo")

        }
        else {
            val service =
                RetrofitClientInstance.getRetrofitInstance().create(
                    GetDataService::class.java
                )
            val call: Call<JSONObject> = service.regisgerUser(phone, senha)
            call.enqueue(object : Callback<JSONObject> {
                override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                    mostrarErro("Não foi possível cadastrar de momento. Tente de novo por favor.")
                    t.printStackTrace()
                    Toast.makeText(
                        this@LoginActivity,
                        "Something went wrong...Please try later!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(call: Call<JSONObject>, response: Response<JSONObject>) {
                    val prefs = getSharedPreferences("COVID", Context.MODE_PRIVATE)
                    val editor = prefs.edit()

                    editor.putString("telefone", phone)
                    editor.putString("senha", senha)

                    editor.apply()

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()

                }
            })

        }

    }

    private fun mostrarErro(s: String) {

        Alerter.create(this@LoginActivity)
            .setText(s)
            .setIcon(android.R.drawable.stat_sys_warning)
            .setBackgroundColorRes(R.color.red) // Optional - Removes white tint
            .show()

    }

}
