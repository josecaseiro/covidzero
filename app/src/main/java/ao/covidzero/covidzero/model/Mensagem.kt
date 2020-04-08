package ao.covidzero.covidzero.model

import android.content.SharedPreferences
import com.google.gson.annotations.SerializedName

class Mensagem(@SerializedName("nomeUsuario") val nome:String,@SerializedName("sms") val mensagem:String,@SerializedName("data_sms") val data:String, val income:Boolean = false){
    fun isIncome(prefs:SharedPreferences):Boolean{
        return !prefs.getString("nome","").equals(nome)

    }
}