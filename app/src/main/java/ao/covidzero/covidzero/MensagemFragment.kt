package ao.covidzero.covidzero

import android.content.Context
import android.content.Context.MODE_PRIVATE
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
import ao.covidzero.covidzero.model.Mensagem

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [MensagemFragment.OnListFragmentInteractionListener] interface.
 */
class MensagemFragment() : Fragment() {
    var mensagens = mutableListOf<Mensagem>()

    constructor(
        mens:MutableList<Mensagem>) : this(){
        this.mensagens = mens
    }

    private lateinit var mens_adapter: MensagemAdapter
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
        val view = inflater.inflate(R.layout.fragment_mensagem_list, container, false)

        mens_adapter = MensagemAdapter(mensagens, listener, activity!!.getSharedPreferences("COVID", MODE_PRIVATE))
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = mens_adapter
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            listener = object : OnListFragmentInteractionListener {
                override fun onListFragmentInteraction(item: Mensagem?) {

                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun addMensagem(nova: Mensagem) {
        mens_adapter.addMsg(nova)
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
        fun onListFragmentInteraction(item: Mensagem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            MensagemFragment(mutableListOf()).apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
