package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.repository

import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.ConfirmRideRequest
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.ConfirmRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideRequest
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.RideHistoryResponse
import retrofit2.Response

interface IRideRepository {

    suspend fun estimateRide(request: EstimateRideRequest): Response<EstimateRideResponse>
    suspend fun confirmRide(request: ConfirmRideRequest): Response<ConfirmRideResponse>
    suspend fun getRideHistory(
        customerId: String,
        driverId: Int? = null
    ): Response<RideHistoryResponse>
}
