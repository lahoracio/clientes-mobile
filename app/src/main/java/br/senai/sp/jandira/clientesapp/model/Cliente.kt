package br.senai.sp.jandira.clientesapp.model

//long= numero tipo inteiro
//? = Para dizer que o numero pode ser null

data class Cliente(
    val id: Long? = 0,
    val nome: String = "",
    val email: String = ""
)
