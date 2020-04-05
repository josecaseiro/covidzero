package ao.covidzero.covidzero.model

import java.io.Serializable

class Dado(val info:String,
           val suspeito:Int,
           val positivos:Int,
           val negativos:Int,
           val recuperados:Int,
           val mortes:Int,
           val quarentena:Int): Serializable {
}