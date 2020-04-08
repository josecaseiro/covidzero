package ao.covidzero.covidzero

import android.content.SharedPreferences
import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView


import ao.covidzero.covidzero.MensagemFragment.OnListFragmentInteractionListener
import ao.covidzero.covidzero.dummy.DummyContent.DummyItem
import ao.covidzero.covidzero.model.Mensagem

import kotlinx.android.synthetic.main.fragment_mensagem.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MensagemAdapter(
    private var mValues: MutableList<Mensagem>,
    private val mListener: OnListFragmentInteractionListener?,
    private val prefs:SharedPreferences
) : RecyclerView.Adapter<MensagemAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Mensagem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate( if(viewType == 0) R.layout.fragment_mensagem else R.layout.fragment_mensagem_out, parent, false)
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (mValues[position].isIncome(prefs)) 0 else 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mNome.text = item.nome
        holder.mMensagem.text = item.mensagem
        holder.mData.text = item.data

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size
    fun addMsg(nova: Mensagem) {
        mValues.add(nova)
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mNome: TextView = mView.nome
        val mMensagem: TextView = mView.mensagem
        val mData: TextView = mView.data
        val mLayout: LinearLayout = mView.mylayout


    }
}
