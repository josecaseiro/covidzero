package ao.covidzero.covidzero

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ao.covidzero.covidzero.dummy.DummyContent
import ao.covidzero.covidzero.dummy.DummyContent.DummyItem
import ao.covidzero.covidzero.model.MenuItem
import kotlinx.android.synthetic.main.fragment_menu_item.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [MenuItemFragment.OnListFragmentInteractionListener] interface.
 */
class MenuItemFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                val lista:MutableList<MenuItem> = mutableListOf()

                lista.add(MenuItem(
                    R.drawable.exame,
                    "Exame",
                    "Febre, estado omunológico"
                ))

                lista.add(MenuItem(
                    R.drawable.mapas,
                    "Mapas em tempo Real",
                    "Propagação, áreas afectas, quarentena"
                ))

                lista.add(MenuItem(
                    R.drawable.metodos,
                    "Métodos de prevenção e sintomas",
                    "Sintomas, métodos"
                ))

                lista.add(MenuItem(
                    R.drawable.grupos,
                    "Grupos e Comunidades",
                    "Grupos comunitários"
                ))

                lista.add(MenuItem(
                    R.drawable.comunique,
                    "Comunique-se",
                    "Denúncios, Emergências"
                ))

                adapter = MenuItemAdapter(lista, listener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {

        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: MenuItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            MenuItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
