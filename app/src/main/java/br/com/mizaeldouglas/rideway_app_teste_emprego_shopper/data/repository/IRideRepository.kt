package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.repository

import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.ConfirmRideRequest
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.ConfirmRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideRequest
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.RideHistoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IRideRepository {

    @POST("ride/estimate")
    suspend fun estimateRide(@Body request: EstimateRideRequest): Response<EstimateRideResponse>

    @PATCH("ride/confirm")
    suspend fun confirmRide(@Body request: ConfirmRideRequest): Response<ConfirmRideResponse>

    @GET("ride/{customer_id}")
    suspend fun getRideHistory(
        @Path("customer_id") customerId: String,
        @Query("driver_id") driverId: Int? = null
    ): Response<RideHistoryResponse>
}