package ao.covidzero.covidzero.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Provincia(@SerializedName("nome") val nome2:String, @SerializedName("provincia") val nome:String, val latitude:String, val longitude:String):Serializable {
    fun getLat():Double{
        return latitude.substring(0, 8).toDouble()
    }

    fun getLong():Double{
        return longitude.substring(0, 8).toDouble()
    }
}