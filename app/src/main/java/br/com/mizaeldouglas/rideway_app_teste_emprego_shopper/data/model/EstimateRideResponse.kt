package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model

data class EstimateRideResponse(
    val origin: Location,
    val destination: Location,
    val distance: Double,
    val duration: String,
    val options: List<DriverOption>,
    val routeResponse: Any? // Mude conforme necessário
)

data class Location(
    val latitude: Double,
    val longitude: Double
)

data class DriverOption(
    val id: Int,
    val name: String,
    val description: String,
    val vehicle: String,
    val review: Review,
    val value: Double
)

data class Review(
    val rating: Double,
    val comment: String
)
