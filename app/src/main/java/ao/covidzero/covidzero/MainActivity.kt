package ao.covidzero.covidzero

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import ao.covidzero.covidzero.model.Dado
import ao.covidzero.covidzero.network.GetDataService
import ao.covidzero.covidzero.network.RetrofitClientInstance
import ao.covidzero.covidzero.widget.ItemRelatorio
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
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
            relatorios.linearLayout.confirmado?.setQtd(it.positivos)
            //?.setQtd(it.positivos)

            var i =0;
            while ( i < relatorios.linearLayout.childCount){
                val item =relatorios.linearLayout.children.elementAt(i) as ItemRelatorio

                when (i) {
                    0 -> {
                        item.setQtd(it.positivos)
                    }
                    1 -> {
                        item.setQtd(it.recuperados)
                    }
                    2 -> {
                        item.setQtd(it.suspeito)
                    }
                    3 -> {
                        item.setQtd(it.mortes)
                    }
                    4 -> {
                        item.setQtd(it.quarentena)
                    }
                    else -> {
                    }
                }

                i++
            }

        }
    }

    private fun loadDados() {
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
