package ao.covidzero.covidzero.model

import java.io.Serializable

class Dado(val info:String?,
           val suspeitos:Int?,
           val positivos:Int?,
           val negativos:Int?,
           val recuperados:Int?,
           val mortes:Int?,
           val quarentena:Int?,
           val status:String?,
           val mensagem:String?,
            val data:String?,
           val provincia: Provincia?
           ): Serializable {
}