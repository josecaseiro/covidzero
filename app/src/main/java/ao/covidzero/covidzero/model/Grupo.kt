package ao.covidzero.covidzero.model

import java.io.Serializable

class Grupo(val icone:Int,
            val nome:String,
            val actividade:String,
            val provincia:String,
            val municipio:String,
            val bairro:String,
            val descricao:String):Serializable {

}