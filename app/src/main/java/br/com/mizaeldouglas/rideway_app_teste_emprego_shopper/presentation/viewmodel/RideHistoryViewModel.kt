package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.api.ApiClient
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.Ride
import kotlinx.coroutines.launch
class RideHistoryViewModel : ViewModel() {

    private val apiClient = ApiClient.apiService


    val customerId = MutableLiveData<String>()
    private val _driverId = MutableLiveData<Int?>()
    val driverId: LiveData<Int?> = _driverId

    val rideHistory = MutableLiveData<List<Ride>>()
    private val isLoading = MutableLiveData<Boolean>()

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun setDriverId(driverId: Int?) {
        _driverId.value = driverId
    }

    fun fetchRideHistory() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = apiClient.getRideHistory(customerId.value.orEmpty(), driverId.value)
                if (response.isSuccessful) {
                    rideHistory.value = response.body()?.rides
                } else {
                    _errorMessage.value = "Erro ao buscar histórico de viagens"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erro de conexão: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }
}

