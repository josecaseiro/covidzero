package ao.covidzero.covidzero

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


object Gestor  {
    private lateinit var mPreferences: SharedPreferences
    private lateinit var mConfiguracao: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor

    private val PREFER_NOME = "Nome app"
    private val LISTA = "alarme"


    /**
     * Setar usuario logado
     * @param isDentro true se sim false se nao
     */
    fun setContext(mContext: Context) {
        mConfiguracao = PreferenceManager.getDefaultSharedPreferences(mContext)
        mPreferences = mContext.getSharedPreferences(PREFER_NOME, Context.MODE_PRIVATE)
        mEditor = mPreferences.edit()
    }

    var alarme: Long
        get() = mPreferences.getLong(LISTA, 0L)
        set(listaAlarme) {

            mEditor.putLong(LISTA, listaAlarme)
            mEditor.commit()
        }
}