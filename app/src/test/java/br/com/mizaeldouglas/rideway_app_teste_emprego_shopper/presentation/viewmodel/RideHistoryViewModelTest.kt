package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.LiveDataTestUtil.getOrAwaitValue
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.Driver
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.Ride
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.RideHistoryResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.repository.IRideRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import retrofit2.Response

@ExperimentalCoroutinesApi
class RideHistoryViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RideHistoryViewModel
    private lateinit var rideRepository: IRideRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        rideRepository = Mockito.mock(IRideRepository::class.java)
        viewModel = RideHistoryViewModel(rideRepository)
    }


    @Test
    fun `CT01 - Deve retornar sucesso com lista aleatoria de viagens quando customerId for CT01 e driverId for nulo ou valido`() =
        runBlocking {
            val customerId = "CT01"
            val driverId = null
            val rideHistoryResponse = RideHistoryResponse(
                customer_id = customerId,
                rides = listOf(
                    Ride(1, "2024-12-01", "Origem 1", "Destino 1", 10.0, "15min", Driver(1, "Motorista 1"), 50.0),
                    Ride(2, "2024-12-02", "Origem 2", "Destino 2", 20.0, "30min", Driver(2, "Motorista 2"), 100.0)
                )
            )

            whenever(rideRepository.getRideHistory(customerId, driverId))
                .thenReturn(Response.success(rideHistoryResponse))

            viewModel.customerId.value = customerId
            viewModel.setDriverId(driverId)

            viewModel.fetchRideHistory()
            testDispatcher.scheduler.advanceUntilIdle()

            val rideHistory = viewModel.rideHistory.getOrAwaitValue()
            val errorMessage = viewModel.errorMessage.getOrAwaitValue()

            assertThat(rideHistory).isEqualTo(rideHistoryResponse.rides)
            assertThat(errorMessage).isNull()
        }

    @Test
    fun `Deve retornar erro sem viagens salvas quando customerId for diferente de CT01`() = runBlocking {

        val customerId = "OutroID"
        val driverId = null

        val errorBody = ResponseBody.create(null, "Not Found")
        whenever(rideRepository.getRideHistory(customerId, driverId))
            .thenReturn(Response.error(404, errorBody))

        viewModel.customerId.value = customerId
        viewModel.setDriverId(driverId)

        viewModel.fetchRideHistory()
        testDispatcher.scheduler.advanceUntilIdle()

        val rideHistory = viewModel.rideHistory.getOrAwaitValue(time = 5)
        val errorMessage = viewModel.errorMessage.getOrAwaitValue()

        assertThat(rideHistory).isEmpty()
        assertThat(errorMessage).isEqualTo("Erro ao buscar histórico: Not Found") // Atualizado
    }

    @Test
    fun `Deve retornar erro de motorista inválido quando driverId for inválido`() = runBlocking {

        val customerId = "CT01"
        val driverId = -1

        val errorBody = ResponseBody.create(null, "Motorista inválido")
        whenever(rideRepository.getRideHistory(customerId, driverId))
            .thenReturn(Response.error(400, errorBody))

        viewModel.customerId.value = customerId
        viewModel.setDriverId(driverId)

        viewModel.fetchRideHistory()
        testDispatcher.scheduler.advanceUntilIdle()

        val rideHistory = viewModel.rideHistory.getOrAwaitValue(time = 5)
        val errorMessage = viewModel.errorMessage.getOrAwaitValue()

        assertThat(rideHistory).isEmpty()
        assertThat(errorMessage).isEqualTo("Erro ao buscar histórico: Motorista inválido") // Atualizado
    }


}

