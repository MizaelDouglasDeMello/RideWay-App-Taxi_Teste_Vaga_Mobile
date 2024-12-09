package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model

data class ConfirmRideRequest(
    val customer_id: String,
    val origin: String,
    val destination: String,
    val distance: Double,
    val duration: String,
    val driver: Driver,
    val value: Double
)

