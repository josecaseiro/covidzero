package ao.covidzero.covidzero

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ao.covidzero.covidzero.model.Profissional
import ao.covidzero.covidzero.network.GetDataService
import ao.covidzero.covidzero.network.RetrofitClientInstance
import com.tapadoo.alerter.Alerter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfissionaisActivity : AppCompatActivity() {

     lateinit var profissionais: MutableList<Profissional>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profissionais)

        loadProfissionais()
    }

    private fun loadProfissionais() {
        Alerter.create(this@ProfissionaisActivity)
            .setTitle("Aguarde")
            .setText("A carregar profissionais")
            .setBackgroundColorRes(R.color.orange)
            .enableProgress(true)
            .enableInfiniteDuration(true)
            .show()

        val service =
            RetrofitClientInstance.getRetrofitInstance().create(
                GetDataService::class.java
            )

        val call = service.profissionais()

        call.enqueue(object: Callback<List<Profissional>> {
            override fun onFailure(call: Call<List<Profissional>>, t: Throwable) {
                Alerter.create(this@ProfissionaisActivity)
                    .setTitle("Lamentamos")
                    .enableInfiniteDuration(true)
                    .setText("Não foi possível carregar a lista de profissionais")
                    .addButton("Tentar de novo", R.style.AlertButton , View.OnClickListener {
                        loadProfissionais()
                    })                    .setBackgroundColorRes(R.color.red)
                    .show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Profissional>>, response: Response<List<Profissional>>) {
                Alerter.hide()
                response.body()?.let {
                    profissionais = mutableListOf()
                    val prefs = getSharedPreferences("COVID", Context.MODE_PRIVATE)
                    val id = prefs.getString("psId", "");
                    for (item in it){
                     if(!item.id.toString().equals(id) )
                         profissionais.add(item)
                    }

                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = ProfissionalFragment(profissionais)
                    fragmentTransaction.replace(R.id.frag_frame, fragment)
                    fragmentTransaction.commit()
                }

            }
        })


    }
}
