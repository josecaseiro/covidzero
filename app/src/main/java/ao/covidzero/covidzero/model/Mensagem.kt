package ao.covidzero.covidzero.model

import android.content.SharedPreferences
import com.google.gson.annotations.SerializedName

class Mensagem(@SerializedName("nomeUsuario") val nome:String?, @SerializedName("usuario") val nome2:String?,@SerializedName("sms") val mensagem:String,@SerializedName("data_sms") val data:String, val income:Boolean = false, val user_id:String?, val userId:String?){
    fun isIncome(prefs:SharedPreferences):Boolean{
        return (!prefs.getString("id","").equals(user_id) && user_id != null)
                || (!prefs.getString("id","").equals(userId)  && userId != null)

    }
}