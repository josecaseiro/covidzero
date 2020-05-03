package ao.covidzero.covidzero

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import ao.covidzero.covidzero.model.Dado
import ao.covidzero.covidzero.network.GetDataService
import ao.covidzero.covidzero.network.HttpClient
import ao.covidzero.covidzero.network.LoginBody
import ao.covidzero.covidzero.network.RetrofitClientInstance
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.tapadoo.alerter.Alerter
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    lateinit var tt:List<View>
    var tela = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        actionBar?.hide()
        supportActionBar?.hide()

        checkBox2.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) statusCheck()
        }

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

    fun covidLogin(phone:String, senha:String){
        val params = RequestParams()
        params.put("telefone", phone)
        params.put("senha", phone)

        HttpClient.post("accaoUser/login", params, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {
                super.onSuccess(statusCode, headers, response)

                shoLoading(false)
                val prefs = getSharedPreferences("COVID", Context.MODE_PRIVATE)
                val editor = prefs.edit()

                response?.let {

                    if( it.has( "idUser" ) ) {
                        editor.putString("telefone", phone)
                        editor.putString("senha", edt_senha.text.toString())
                        editor.putString("id", it.getString("idUser"))
                        editor.apply()

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else

                        mostrarErro(it.getString("Mensagem"))

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

        if(pos == 1)  senha.visibility =   View.GONE
        if(pos == 2)  senha.visibility =   View.VISIBLE
        if(pos == 0)  senha.visibility =   View.INVISIBLE

        nome.visibility = if(pos == 1) View.VISIBLE else  View.GONE
        phone.visibility = if(pos != 2) View.VISIBLE else  View.GONE
        bt_cadastrar.text = if(pos == 1) "Cadastrar" else "Entrar"

    }

    fun shoLoading(boolean: Boolean = true){
        waiting.visibility =  if(boolean) View.VISIBLE else View.GONE
        bt_cadastrar.visibility =  if(!boolean) View.VISIBLE else View.GONE
    }

    private fun makeCadastro() {

        shoLoading()

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

        if(phone.isBlank() || nome.isBlank() || phone.length < 9 || !PhoneNumberUtils.isGlobalPhoneNumber(phone))
        {
            mostrarErro("Preencha todos os campos correctamente por favor")
        }

        else {

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonePrefix(phone), // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                this, object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    override fun onVerificationFailed(p0: FirebaseException) {
                        p0.printStackTrace()
                        mostrarErro("Não foi possivel verificar o seu número de telefone. Tente de novo.")
                    }

                    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        Log.i("CODIGO ENVIADO", p0)
                    }

                    override fun onCodeAutoRetrievalTimeOut(p0: String) {
                        Log.e("Terminou tempo de e", p0)
                        mostrarErro("Não foi possivel enviar mensagem para o seu número de telefone. Tente de novo.")
                    }

                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        val params = RequestParams()
                        params.put("telefone", phone)
                        params.put("senha", phone)
                        params.put("nome", nome)

                        HttpClient.post("usuarios", params, object : JsonHttpResponseHandler() {
                            override fun onSuccess(
                                statusCode: Int,
                                headers: Array<out Header>?,
                                response: JSONObject?
                            ) {
                                super.onSuccess(statusCode, headers, response)
                                Log.d("CADASTRO", "${response?.toString()}" )

                                shoLoading(false)

                                response?.let {
                                    if(it.getString("Status") != "Erro") {
                                        Alerter.create(this@LoginActivity)
                                            .setTitle("Conta criada com sucesso")
                                            .setText("Inicie sessão por favor")
                                            .setBackgroundColorRes(R.color.green)
                                            .setDuration(10000)
                                            .show()

                                        covidLogin(phone, phone)
                                    }
                                    else Alerter.create(this@LoginActivity)
                                        .setTitle("Lamentamos")
                                        .setText(it.getString("Messagem"))
                                        .setBackgroundColorRes(R.color.orange)
                                        .setDuration(10000)
                                        .show()
                                }


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
                })


            }

    }

    fun phonelogin(phone: String){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+244$phone", // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                    covidLogin(phone, phone)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    p0.printStackTrace()
                    mostrarErro("Não foi possivel verificar o seu número de telefone. Tente de novo.")
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    Log.i("CODIGO ENVIADO", p0)
                }

                override fun onCodeAutoRetrievalTimeOut(p0: String) {
                    Log.e("Terminou tempo de e", p0)
                    mostrarErro("Não foi possivel enviar mensagem para o seu número de telefone. Tente de novo.")
                }

            })
    }

    private fun makeLogin() {


        shoLoading()
        val phone = edt_phone.text.toString()
        val senha = edt_senha.text.toString()

        if(phone.isBlank() || phone.length < 9 || !PhoneNumberUtils.isGlobalPhoneNumber(phone))
        {
            mostrarErro("Preencha todos os campos correctamente por favor")
        }
        else {
            Alerter.create(this@LoginActivity)
                .setTitle("A iniciar sessão")
                .setText("Aguarde por favor")
                .setBackgroundColorRes(R.color.orange)
                .enableProgress(true)
                .enableInfiniteDuration(true)
                .show()

            phonelogin(phone)
            return
        }

    }

    public fun statusCheck() {
    val manager =  getSystemService(Context.LOCATION_SERVICE) as LocationManager

    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        buildAlertMessageNoGps();
    }
}

    fun buildAlertMessageNoGps(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Seu GPS parece estar desactivado. Active-o para obter os dados relactivos ao COVID19 na sua região.")
        builder.setPositiveButton("Activar", DialogInterface.OnClickListener { dialog, which ->
            startActivity( Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        })
        builder.setNegativeButton("Activar mais tarde", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        builder.create().show()
    }

    private fun makeLoginProfissional() {
        shoLoading()

        val ordem = edt_ordem.text.toString()
        val senha = edt_senha.text.toString()

        if(ordem.isBlank() || senha.isBlank() )
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
                params.put("numOrdem", ordem)
                params.put("senha", senha)

                HttpClient.post("accaoUser/loginPs", params, object : JsonHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        response: JSONObject?
                    ) {
                        shoLoading(false)
                        super.onSuccess(statusCode, headers, response)

                        val prefs = getSharedPreferences("COVID", Context.MODE_PRIVATE)
                        val editor = prefs.edit()

                        response?.let {

                            if( it.has( "dadosPs" ) ) {
                                editor.putString("senha", senha)
                                editor.putString("psId", it. getJSONObject("dadosPs").getString("idPs"))
                                editor.putString("id", it. getJSONObject("dadosPs").getString("idPs"))
                                editor.apply()

                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            } else mostrarErro("Número órdem ou senha não correspondem. Tente de novo por favor.")

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
                        mostrarErro("Número órdem ou senha não correspondem. Tente de novo por favor.")
                        super.onFailure(statusCode, headers, responseString, throwable)
                    }
                })


            }

    }

    private fun phonePrefix(phone: String): String {
       return if(phone.startsWith("+244")) phone else "+244$phone"
    }

    private fun mostrarErro(s: String) {
        shoLoading(false)
        Alerter.create(this@LoginActivity)
            .setTitle("Lamentamos")
            .setText(s)
            .setIcon(android.R.drawable.stat_sys_warning)
            .setBackgroundColorRes(R.color.red) // Optional - Removes white tint
            .show()

    }

}
