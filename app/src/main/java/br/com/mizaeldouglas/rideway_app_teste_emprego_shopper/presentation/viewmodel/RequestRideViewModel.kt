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
        viewModelScope.launch {
            try {
                if (customerId.isBlank() || origin.isBlank() || destination.isBlank()) {
                    _errorMessage.value = "All fields must be filled."
                    return@launch
                }

                val request = EstimateRideRequest(customerId, origin, destination)
                val response = rideRepository.estimateRide(request)

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
                _errorMessage.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }
}


