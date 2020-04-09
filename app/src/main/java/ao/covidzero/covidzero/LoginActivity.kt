package ao.covidzero.covidzero

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import ao.covidzero.covidzero.model.Dado
import ao.covidzero.covidzero.network.GetDataService
import ao.covidzero.covidzero.network.HttpClient
import ao.covidzero.covidzero.network.LoginBody
import ao.covidzero.covidzero.network.RetrofitClientInstance
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.tapadoo.alerter.Alerter
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var tt:List<View>
    var tela = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        actionBar?.hide()
        supportActionBar?.hide()

        tt = mutableListOf(tt_entrar,tt_cadastrar, tt_profissionais)

        tt_entrar.setOnClickListener {
            showScreen(0)
        }

        tt_cadastrar.setOnClickListener {
            showScreen(1)
        }

        tt_profissionais.setOnClickListener {
            showScreen(2)
        }

        bt_cadastrar.setOnClickListener {

            when (tela) {
                0 -> {
                    makeLogin()
                }

                1 -> {
                    makeCadastro()
                }

                2 -> {
                    makeLoginProfissional()
                }

            }
        }


    }

    fun showScreen(pos:Int){
        tela = pos
        for (t in tt){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                (t as TextView).setTextColor(resources.getColor(R.color.cinza, null) )
            } else {
                (t as TextView).setTextColor(R.color.cinza)
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (tt[pos] as TextView).setTextColor(resources.getColor(R.color.black, null) )
        } else {
            (tt[pos] as TextView).setTextColor(R.color.black)
        }

        ordem.visibility = if(pos == 2) View.VISIBLE else  View.GONE
        nome.visibility = if(pos == 1) View.VISIBLE else  View.GONE
        phone.visibility = if(pos != 2) View.VISIBLE else  View.GONE
        bt_cadastrar.text = if(pos == 1) "Cadastrar" else "Entrar"

    }

    private fun makeCadastro() {
        Alerter.create(this@LoginActivity)
            .setTitle("A criar conta")
            .setText("Aguarde por favor")
            .setBackgroundColorRes(R.color.orange)
            .enableProgress(true)
            .enableInfiniteDuration(true)
            .show()

        val phone = edt_phone.text.toString()
        val nome = edt_nome.text.toString()
        val senha = edt_senha.text.toString()

        if(phone.isBlank() || senha.isBlank() || nome.isBlank() || phone.length < 9 || !PhoneNumberUtils.isGlobalPhoneNumber(phone))
        {
            mostrarErro("Preencha todos os campos correctamente por favor")

        }
        else
            if(senha.length < 6 )
            {
                mostrarErro("A senha precisa ter 6 caracteres no mínimo")

            }
            else {

                val params = RequestParams()
                params.put("telefone", phone)
                params.put("senha", senha)
                params.put("nome", nome)

                HttpClient.post("usuarios", params, object : JsonHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        response: JSONObject?
                    ) {
                        super.onSuccess(statusCode, headers, response)

                        Alerter.create(this@LoginActivity)
                            .setTitle("Conta criada com sucesso")
                            .setText("Inicie sessão por favor")
                            .setBackgroundColorRes(R.color.green)
                            .setDuration(10000)
                            .show()

                        tt_entrar.callOnClick()

                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseString: String?,
                        throwable: Throwable?
                    ) {
                        Alerter.hide()
                        mostrarErro("Não foi possível criar a sua conta. Tente de novo por favor.")
                        super.onFailure(statusCode, headers, responseString, throwable)
                    }
                })

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
            Alerter.create(this@LoginActivity)
                .setTitle("A iniciar sessão")
                .setText("Aguarde por favor")
                .setBackgroundColorRes(R.color.orange)
                .enableProgress(true)
                .enableInfiniteDuration(true)
                .show()

            val params = RequestParams()
            params.put("telefone", phone)
            params.put("senha", senha)

            HttpClient.post("accaoUser/login", params, object : JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    super.onSuccess(statusCode, headers, response)

                    val prefs = getSharedPreferences("COVID", Context.MODE_PRIVATE)
                    val editor = prefs.edit()

                    response?.let {

                        if( it.has( "idUser" ) ) {
                            editor.putString("telefone", phone)
                            editor.putString("senha", senha)
                            editor.putString("id", it.getString("idUser"))
                            editor.apply()

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else mostrarErro("Telefone ou senha não correspondem. Tente de novo por favor.")

                    }


                    Log.d("LOGIN", response.toString())

                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseString: String?,
                    throwable: Throwable?
                ) {
                    Alerter.hide()
                    mostrarErro("Telefone ou senha não correspondem. Tente de novo por favor.")
                    super.onFailure(statusCode, headers, responseString, throwable)
                }
            })

            return
        }

    }

    private fun makeLoginProfissional() {
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
                val call: Call<JSONObject> = service.regisgerUser(phone, senha, "")
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
            .setTitle("Lamentamos")
            .setText(s)
            .setIcon(android.R.drawable.stat_sys_warning)
            .setBackgroundColorRes(R.color.red) // Optional - Removes white tint
            .show()

    }

}
