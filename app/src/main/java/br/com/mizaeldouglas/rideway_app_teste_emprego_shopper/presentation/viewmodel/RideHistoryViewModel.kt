package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.Ride
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.repository.IRideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RideHistoryViewModel @Inject constructor(
    private val rideRepository: IRideRepository
) : ViewModel() {

    val customerId = MutableLiveData<String>()
    private val _driverId = MutableLiveData<Int?>()
    val driverId: LiveData<Int?> = _driverId

    private val _rideHistory = MutableLiveData<List<Ride>>()
    val rideHistory: LiveData<List<Ride>> = _rideHistory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun setDriverId(driverId: Int?) {
        _driverId.value = driverId
    }

    fun fetchRideHistory() {
        _isLoading.value = true
        val customerIdValue = customerId.value.orEmpty()
        val driverIdValue = driverId.value

        viewModelScope.launch {
            try {
                val response = rideRepository.getRideHistory(customerIdValue, driverIdValue)
                if (response.isSuccessful) {
                    _rideHistory.value = response.body()?.rides ?: emptyList() // Sempre atualiza com uma lista, mesmo que vazia
                    _errorMessage.value = null
                } else {
                    _rideHistory.value = emptyList() // Garante consistência
                    _errorMessage.value = "Erro ao buscar histórico de viagens: ${response.errorBody()?.string() ?: response.message()}"
                }
            } catch (e: Exception) {
                _rideHistory.value = emptyList() // Atualiza em caso de exceção também
                _errorMessage.value = "Erro de conexão: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

}
