package ao.covidzero.covidzero.model

import org.json.JSONObject
import java.io.Serializable

class Dado(val info:String="",
           val suspeitos:Int=0,
           val positivos:Int=0,
           val negativos:Int=0,
           val recuperados:Int=0,
           val mortes:Int=0,
           val quarentena:Int=0,
           val status:String="",
           val mensagem:String="",
            val data:String="",
           val provincia: Provincia?
           ): Serializable {

}