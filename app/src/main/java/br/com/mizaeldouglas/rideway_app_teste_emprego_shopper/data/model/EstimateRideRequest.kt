package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EstimateRideRequest(
    val customer_id: String,
    val origin: String,
    val destination: String
) : Parcelable
