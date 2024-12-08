package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model

data class EstimateRideResponse(
    val origin: Location,
    val destination: Location,
    val distance: Double,
    val duration: String,
    val options: List<DriverOption>,
    val routeResponse: Any?
)





