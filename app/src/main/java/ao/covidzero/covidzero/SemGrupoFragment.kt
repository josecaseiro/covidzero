package ao.covidzero.covidzero

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ao.covidzero.covidzero.model.Grupo
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.dialog_nova_comunidade.*
import kotlinx.android.synthetic.main.fragment_sem_grupo.*
import kotlinx.android.synthetic.main.fragment_sem_grupo.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SemGrupoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SemGrupoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_sem_grupo, container, false)

        v.bt_criar_grupo.setOnClickListener {
            val dg = android.app.Dialog( activity!! )
            dg.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)

            dg.setContentView(R.layout.dialog_nova_comunidade)
            dg.bt_cancelar.setOnClickListener {
                dg.dismiss()
            }

            dg.bt_criar.setOnClickListener {
                val nome = dg.edt_nome.text.toString()
                val actividade = dg.edt_actividade.text.toString()
                val provincia = dg.edt_provincia.text.toString()
                val municipio = dg.edt_municipio.text.toString()
                val bairro = dg.edt_bairro.text.toString()
                val descricao = dg.edt_descricao.text.toString()

                if( !
                    (nome.isBlank()
                            && actividade.isBlank()
                            && provincia.isBlank()
                            && municipio.isBlank()
                            && bairro.isBlank()
                            )
                        ){
                    val grupo = Grupo(R.drawable.grupos,
                        nome,
                        actividade,
                        provincia,
                        municipio,
                        bairro,
                        descricao
                        )

                    (activity as GruposActivity).addGrupo(grupo)
                    dg.dismiss()
                }
            }

            Alerter.create(activity!!)
                .setText("Ainda não é possível criar grupo. Tente mais por favor.")
                .setIcon(android.R.drawable.stat_sys_warning)
                .setBackgroundColor(R.color.orange) // Optional - Removes white tint
                .show()
            //dg.show()
        }

        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SemGrupoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SemGrupoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
