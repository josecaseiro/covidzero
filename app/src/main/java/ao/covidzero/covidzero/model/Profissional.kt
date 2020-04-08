package ao.covidzero.covidzero.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Profissional(val id:Int, @SerializedName("Nome") val nome:String, @SerializedName("Profissão") val profissao:String, @SerializedName("Telefone") val telefone:String):Serializable {
}