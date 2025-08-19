package br.senai.sp.jandira.clientesapp.teste

import br.senai.sp.jandira.clientesapp.model.Cliente
import br.senai.sp.jandira.clientesapp.service.RetrofitFactory

//main- funcao principal
fun main() {
    val c1 = Cliente(
        nome = "Lara",
        email = "lara@gmail.com"
    )

    val retrofit = RetrofitFactory().getClienteService()
    val cliente = retrofit.gravar(c1)
}