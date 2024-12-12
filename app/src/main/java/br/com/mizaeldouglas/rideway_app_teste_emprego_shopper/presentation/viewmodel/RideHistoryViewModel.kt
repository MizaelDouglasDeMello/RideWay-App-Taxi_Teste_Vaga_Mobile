package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.viewmodel

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.Ride
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.RideHistoryResponse
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

    private val _selectedDriverName = MutableLiveData<String?>()
    val selectedDriverName: LiveData<String?> = _selectedDriverName

    fun setDriverId(driverId: Int?) {
        _driverId.value = driverId
        _selectedDriverName.value = getDriverNameById(driverId)
    }

    fun fetchRideHistory() {
        val customerIdValue = customerId.value.orEmpty()

        if (customerIdValue.isBlank()) {
            _errorMessage.value = "Customer ID não pode estar vazio."
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = rideRepository.getRideHistory(customerIdValue, driverId.value)
                handleResponse(response)
            } catch (e: Exception) {
                handleException(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun handleResponse(response: retrofit2.Response<*>?) {
        if (response?.isSuccessful == true) {
            val rides = (response.body() as? RideHistoryResponse)?.rides.orEmpty()
            _rideHistory.value = rides
            _errorMessage.value = null
        } else {
            _rideHistory.value = emptyList()
            _errorMessage.value = "Erro ao buscar histórico: ${response?.errorBody()?.string() ?: response?.message()}"
        }
    }

    private fun getDriverNameById(driverId: Int?): String {
        return when (driverId) {
            1 -> "James Bond"
            2 -> "Homer Simpson"
            3 -> "Dominic Toretto"
            else -> ""
        }
    }

    private fun handleException(exception: Exception) {
        _rideHistory.value = emptyList()
        _errorMessage.value = "Erro de conexão: ${exception.localizedMessage}"
    }

    @BindingAdapter("app:driverVisibility")
    fun setDriverVisibility(view: TextView, driverName: String?, selectedDriverName: String?) {
        if (driverName != selectedDriverName) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

}

