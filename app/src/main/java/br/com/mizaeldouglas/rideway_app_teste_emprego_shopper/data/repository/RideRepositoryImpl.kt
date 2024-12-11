package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.repository

import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.ConfirmRideRequest
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.ConfirmRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideRequest
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.RideHistoryResponse
import retrofit2.Response
import retrofit2.Retrofit

class RideRepositoryImpl(private val retrofit: Retrofit) : IRideRepository {

    private val service: IRideRepository = retrofit.create(IRideRepository::class.java)


    override suspend fun estimateRide(request: EstimateRideRequest): Response<EstimateRideResponse> =
        service.estimateRide(request)

    override suspend fun confirmRide(request: ConfirmRideRequest): Response<ConfirmRideResponse> =
        service.confirmRide(request)

    override suspend fun getRideHistory(
        customerId: String,
        driverId: Int?
    ): Response<RideHistoryResponse> = service.getRideHistory(customerId, driverId)
}

