package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.ConfirmRideRequest
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.ConfirmRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.Driver
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.DriverOption
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.repository.IRideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RideOptionsViewModel @Inject constructor(
    private val rideRepository: IRideRepository
) : ViewModel() {

    private val _rideOptions = MutableLiveData<List<DriverOption>>()
    val rideOptions: LiveData<List<DriverOption>> = _rideOptions

    private val _selectedOption = MutableLiveData<DriverOption>()
    val selectedOption: LiveData<DriverOption> = _selectedOption

    private val _rideResponse = MutableLiveData<EstimateRideResponse?>()
    val rideResponse: LiveData<EstimateRideResponse?> = _rideResponse

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _navigateToRideHistory = MutableLiveData<Boolean>()
    val navigateToRideHistory: LiveData<Boolean> = _navigateToRideHistory

    fun loadRideData(rideOptions: List<DriverOption>, rideResponse: EstimateRideResponse?) {
        _rideOptions.value = rideOptions
        _rideResponse.value = rideResponse
    }

    fun selectOption(option: DriverOption) {
        _selectedOption.value = option
    }

    fun acceptRide(customerId: String) {
        viewModelScope.launch {
            val rideResponseValue = _rideResponse.value
            val selectedOptionValue = _selectedOption.value

            if (rideResponseValue == null || selectedOptionValue == null) {
                _toastMessage.value = "Erro: Dados da corrida ou motorista não disponíveis."
                return@launch
            }

            if (selectedOptionValue.id < 0) {
                _toastMessage.value = "Erro: Motorista inválido."
                _navigateToRideHistory.value = false
                return@launch
            }

            if (!isDistanceValidForDriver(selectedOptionValue.id, rideResponseValue.distance)) {
                _toastMessage.value = "Erro: A distância é inválida para o motorista selecionado."
                _navigateToRideHistory.value = false
                return@launch
            }

            val request = createConfirmRideRequest(customerId, rideResponseValue, selectedOptionValue)

            try {
                val response = rideRepository.confirmRide(request)
                handleRideResponse(response, selectedOptionValue)
            } catch (e: Exception) {
                _toastMessage.value = "Erro de conexão: ${e.message}"
                _navigateToRideHistory.value = false
            }
        }
    }

    private fun handleRideResponse(response: Response<ConfirmRideResponse>, selectedOptionValue: DriverOption) {
        if (response.isSuccessful && response.body()?.success == true) {
            _toastMessage.value = "Corrida confirmada com ${selectedOptionValue.name}"
            _navigateToRideHistory.value = true
        } else {
            _toastMessage.value = "Falha ao confirmar corrida: ${response.errorBody()?.string() ?: "Erro desconhecido"}"
            _navigateToRideHistory.value = false
        }
    }

    private fun isDistanceValidForDriver(driverId: Int, distance: Double): Boolean {
        return when (driverId) {
            1 -> distance in 1.0..4.0
            2 -> distance in 5.0..9.0
            3 -> distance >= 10
            else -> false
        }
    }

    private fun createConfirmRideRequest(
        customerId: String,
        rideResponseValue: EstimateRideResponse,
        selectedOptionValue: DriverOption
    ): ConfirmRideRequest {
        return ConfirmRideRequest(
            customer_id = customerId,
            origin = "${rideResponseValue.origin.latitude},${rideResponseValue.origin.longitude}",
            destination = "${rideResponseValue.destination.latitude},${rideResponseValue.destination.longitude}",
            distance = rideResponseValue.distance,
            duration = rideResponseValue.duration,
            driver = Driver(id = selectedOptionValue.id, name = selectedOptionValue.name),
            value = selectedOptionValue.value
        )
    }

    fun onNavigateToRideHistoryHandled() {
        _navigateToRideHistory.value = false
    }
}