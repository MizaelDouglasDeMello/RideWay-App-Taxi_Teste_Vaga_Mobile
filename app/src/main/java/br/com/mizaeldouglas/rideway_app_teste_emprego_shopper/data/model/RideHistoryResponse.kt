package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model

data class RideHistoryResponse(
    val customer_id: String,
    val rides: List<Ride>
)

data class Ride(
    val id: Int,
    val date: String,
    val origin: String,
    val destination: String,
    val distance: Double,
    val duration: String,
    val driver: Driver,
    val value: Double
)
