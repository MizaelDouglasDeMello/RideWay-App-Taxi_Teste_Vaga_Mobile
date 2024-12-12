package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideRequest
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.repository.IRideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestRideViewModel @Inject constructor(
    private val rideRepository: IRideRepository
) : ViewModel() {

    private val _rideOptions = MutableLiveData<EstimateRideResponse?>()
    val rideOptions: LiveData<EstimateRideResponse?> = _rideOptions

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun estimateRide(customerId: String, origin: String, destination: String) {
        if (customerId.isBlank() || origin.isBlank() || destination.isBlank()) {
            _errorMessage.value = "All fields must be filled."
            return
        }

        val request = EstimateRideRequest(customerId, origin, destination)
        performEstimateRide(request)
    }

    private fun performEstimateRide(request: EstimateRideRequest) {
        viewModelScope.launch {
            try {
                val response = rideRepository.estimateRide(request)

                if (response.isSuccessful) {
                    handleSuccessfulResponse(response.body())
                } else {
                    handleErrorResponse(response.code(), response.message())
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }

    private fun handleSuccessfulResponse(body: EstimateRideResponse?) {
        if (body != null) {
            _rideOptions.value = body
        } else {
            _errorMessage.value = "Error: No data received from the server."
        }
    }

    private fun handleErrorResponse(code: Int, message: String) {
        _errorMessage.value = when (code) {
            400 -> "Error: Invalid customer ID"
            else -> "Error: $message"
        }
    }

}