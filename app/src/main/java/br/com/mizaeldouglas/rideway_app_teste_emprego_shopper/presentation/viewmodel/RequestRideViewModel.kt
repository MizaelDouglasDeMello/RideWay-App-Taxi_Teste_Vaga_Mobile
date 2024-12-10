package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.api.ApiClient
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideRequest
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideResponse
import kotlinx.coroutines.launch

class RequestRideViewModel : ViewModel() {

    private val _rideOptions = MutableLiveData<EstimateRideResponse?>()
    val rideOptions: LiveData<EstimateRideResponse?> = _rideOptions

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun estimateRide(customerId: String, origin: String, destination: String) {
        viewModelScope.launch {
            try {
                // Verificando se os parâmetros não estão vazios antes de fazer a requisição
                if (customerId.isBlank() || origin.isBlank() || destination.isBlank()) {
                    _errorMessage.value = "All fields must be filled."
                    return@launch
                }

                val response = ApiClient.apiService.estimateRide(
                    EstimateRideRequest(customerId, origin, destination)
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _rideOptions.value = body
                    } else {
                        _errorMessage.value = "Error: No data received from the server."
                    }
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                }
            } catch (e: Exception) {
                // Catching any exception that might occur (e.g., network error)
                _errorMessage.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }
}

