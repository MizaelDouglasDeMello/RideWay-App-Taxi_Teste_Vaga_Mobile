package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model

data class EstimateRideRequest(
    val customer_id: String,
    val origin: String,
    val destination: String
)
