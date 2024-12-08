package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    val rating: Double,
    val comment: String
): Parcelable