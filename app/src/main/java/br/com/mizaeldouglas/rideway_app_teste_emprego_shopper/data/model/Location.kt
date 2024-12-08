package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val latitude: Double,
    val longitude: Double
): Parcelable
