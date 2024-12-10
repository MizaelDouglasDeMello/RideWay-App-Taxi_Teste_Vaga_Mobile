package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.viewmodel

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
            val rideResponseValue = _rideResponse.value ?: return@launch
            val selectedOptionValue = _selectedOption.value ?: return@launch

            try {
                // Verificação se a origem é a mesma que o destino
                if (rideResponseValue.origin == rideResponseValue.destination) {
                    _toastMessage.value = "Erro: A origem não pode ser igual ao destino."
                    return@launch
                }

                // Validação da distância para o motorista selecionado
                val isDistanceValid = when (selectedOptionValue.id) {
                    1 -> rideResponseValue.distance in 1.0..4.0
                    2 -> rideResponseValue.distance in 5.0..9.0
                    3 -> rideResponseValue.distance >= 10
                    else -> false
                }

                if (!isDistanceValid) {
                    _toastMessage.value = "Erro: A distância é inválida para o motorista selecionado."
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

                // Tentando realizar a confirmação da corrida
                val response = ApiClient.apiService.confirmRide(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    _toastMessage.value = "Corrida confirmada com ${selectedOptionValue.name}"
                    _navigateToRideHistory.value = true
                } else {
                    _toastMessage.value = "Falha ao confirmar corrida: ${response.errorBody()?.string() ?: "Erro desconhecido"}"
                }
            } catch (e: Exception) {
                // Captura de erro de exceção e exibição de mensagem de erro
                _toastMessage.value = "Ocorreu um erro: ${e.localizedMessage}"
            }
        }
    }

    // Função para resetar o evento de navegação
    fun onNavigateToRideHistoryHandled() {
        _navigateToRideHistory.value = false
    }
}
