package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.repository

import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.api.IApiService
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.ConfirmRideRequest
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.ConfirmRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideRequest
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.RideHistoryResponse
import retrofit2.Response

class RideRepositoryImpl(private val apiService: IApiService) : IRideRepository {

    override suspend fun estimateRide(request: EstimateRideRequest): Response<EstimateRideResponse> =
        apiService.estimateRide(request)

    override suspend fun confirmRide(request: ConfirmRideRequest): Response<ConfirmRideResponse> =
        apiService.confirmRide(request)

    override suspend fun getRideHistory(
        customerId: String,
        driverId: Int?
    ): Response<RideHistoryResponse> = apiService.getRideHistory(customerId, driverId)
}


