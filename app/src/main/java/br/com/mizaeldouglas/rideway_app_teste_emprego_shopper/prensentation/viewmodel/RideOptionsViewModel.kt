package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.prensentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.api.ApiClient
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.ConfirmRideRequest
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.Driver
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.DriverOption
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideResponse
import kotlinx.coroutines.launch

class RideOptionsViewModel : ViewModel() {

    private val _rideOptions = MutableLiveData<List<DriverOption>>()
    val rideOptions: LiveData<List<DriverOption>> = _rideOptions

    private val _selectedOption = MutableLiveData<DriverOption>()
    val selectedOption: LiveData<DriverOption> = _selectedOption

    private val _rideResponse = MutableLiveData<EstimateRideResponse?>()
    val rideResponse: LiveData<EstimateRideResponse?> = _rideResponse

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    fun loadRideData(rideOptions: List<DriverOption>, rideResponse: EstimateRideResponse?) {
        _rideOptions.value = rideOptions
        _rideResponse.value = rideResponse
    }

    fun selectOption(option: DriverOption) {
        _selectedOption.value = option
    }

    fun acceptRide(customerId: String) {
        viewModelScope.launch {
            val rideResponseValue = _rideResponse.value ?: return@launch
            val selectedOptionValue = _selectedOption.value ?: return@launch

            try {
                if (rideResponseValue.origin == rideResponseValue.destination) {
                    _toastMessage.value = "Error: Destination cannot be the same as the origin."
                    return@launch
                }

                val isDistanceValid = when (selectedOptionValue.id) {
                    1 -> rideResponseValue.distance in 1.0..4.0
                    2 -> rideResponseValue.distance in 5.0..9.0
                    3 -> rideResponseValue.distance >= 10
                    else -> false
                }

                if (!isDistanceValid) {
                    _toastMessage.value = "Error: Distance is invalid for the selected driver."
                    return@launch
                }

                val request = ConfirmRideRequest(
                    customer_id = customerId,
                    origin = "${rideResponseValue.origin.latitude},${rideResponseValue.origin.longitude}",
                    destination = "${rideResponseValue.destination.latitude},${rideResponseValue.destination.longitude}",
                    distance = rideResponseValue.distance,
                    duration = rideResponseValue.duration,
                    driver = Driver(
                        id = selectedOptionValue.id,
                        name = selectedOptionValue.name
                    ),
                    value = selectedOptionValue.value
                )

                val response = ApiClient.apiService.confirmRide(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    _toastMessage.value = "Ride confirmed with ${selectedOptionValue.name}"
                } else {
                    _toastMessage.value = "Failed to confirm ride: ${response.errorBody()?.string() ?: "Unknown error"}"
                }
            } catch (e: Exception) {
                _toastMessage.value = "An error occurred: ${e.message}"
            }
        }
    }
}
