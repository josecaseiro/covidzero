package ao.covidzero.covidzero.model

import java.io.Serializable

class Grupo(val id:Int,
            val nome:String,
            val actividade:String,
            val localizacao: Localizacao,
            val descricao:String):Serializable

class Localizacao(val provincia: String,
                  val municipio: String,
                  val bairro:String):Serializable