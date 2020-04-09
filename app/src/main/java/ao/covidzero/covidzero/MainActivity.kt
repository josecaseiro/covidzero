package ao.covidzero.covidzero

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import ao.covidzero.covidzero.model.Dado
import ao.covidzero.covidzero.network.GetDataService
import ao.covidzero.covidzero.network.HttpClient
import ao.covidzero.covidzero.network.RetrofitClientInstance
import ao.covidzero.covidzero.widget.ItemRelatorio
import com.google.gson.Gson
import com.loopj.android.http.JsonHttpResponseHandler
import com.tapadoo.alerter.Alerter
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    var dado:Dado? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        generateMenu()

        loadDados()

        showDados()

        linearLayout.setOnClickListener {
            startActivity(Intent(this@MainActivity, MapActivity::class.java))
        }

    }

    private fun showDados() {
        dado?.let {
            relatorios.linearLayout.confirmado?.setQtd(it.positivos!!)
            //?.setQtd(it.positivos)

            var i =0;
            while ( i < relatorios.linearLayout.childCount){
                val item =relatorios.linearLayout.children.elementAt(i) as ItemRelatorio

                when (i) {
                    0 -> {
                        item.setQtd(it.positivos!!)
                    }
                    1 -> {
                        item.setQtd(it.recuperados!!)
                    }
                    2 -> {
                        item.setQtd(it.suspeitos!!)
                    }
                    3 -> {
                        item.setQtd(it.mortes!!)
                    }
                    4 -> {
                        item.setQtd(it.quarentena!!)
                    }
                    else -> {
                    }
                }

                i++
            }

        }
    }

    private fun loadDados() {

        HttpClient.get("dados", null, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {
                super.onSuccess(statusCode, headers, response)

                response?.let {
                    val gson = Gson()
                    val dad = gson.fromJson<Dado>(it.toString(), Dado::class.java)
                    dado = dad
                    showDados()
                    Alerter.hide()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?,
                throwable: Throwable?
            ) {
                Alerter.hide()
                Alerter.create(this@MainActivity)
                    .setTitle("Lamentamos")
                    .setText("Não foi possível carregar os dados")
                    .enableInfiniteDuration(true)
                    .addButton("Tentar de novo", R.style.AlertButton, View.OnClickListener {
                        loadDados()
                    })
                    .setIcon(android.R.drawable.stat_sys_warning)
                    .setBackgroundColorRes(R.color.red) // Optional - Removes white tint
                    .show()
                super.onFailure(statusCode, headers, responseString, throwable)
            }
        })

        return

        val service =
            RetrofitClientInstance.getRetrofitInstance().create(
                GetDataService::class.java
            )
        val call: Call<Dado> = service.dadosGerais()
        call.enqueue(object : Callback<Dado> {
            override fun onFailure(call: Call<Dado>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    this@MainActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<Dado>, response: Response<Dado>) {
                    dado = response.body()
                showDados()
                Log.d("res", dado.toString())
            }
        })
    }


    private fun generateMenu() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = MenuItemFragment()
        fragmentTransaction.replace(R.id.frag_frame, fragment)
        fragmentTransaction.commit()
    }

}
