package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EstimateRideResponse(
    val origin: Location,
    val destination: Location,
    val distance: Double,
    val duration: String,
    val options: List<DriverOption>
): Parcelable





