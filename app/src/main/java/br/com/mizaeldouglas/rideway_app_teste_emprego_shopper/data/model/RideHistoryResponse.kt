package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model

data class RideHistoryResponse(
    val customer_id: String,
    val rides: List<Ride>
)

