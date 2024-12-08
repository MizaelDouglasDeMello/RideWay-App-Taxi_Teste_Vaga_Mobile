package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.api

import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    val apiService: IApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IApiService::class.java)
    }
}