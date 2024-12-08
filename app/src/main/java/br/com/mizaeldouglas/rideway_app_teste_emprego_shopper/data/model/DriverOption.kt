package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DriverOption(
    val id: Int,
    val name: String,
    val description: String,
    val vehicle: String,
    val review: Review,
    val value: Double
) : Parcelable