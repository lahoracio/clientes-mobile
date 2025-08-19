package br.senai.sp.jandira.clientesapp.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

//SEMPRE A MESMA COISA, SO MIUDAR A URL NOS PROJETOS (CONECTAR A API)

class RetrofitFactory {
    private val BASE_URL = "https://srv945707.hstgr.cloud/api/"
    private val retrofitFactory =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getClienteService(): ClienteService {
        return  retrofitFactory.create(ClienteService::class.java)
    }
}