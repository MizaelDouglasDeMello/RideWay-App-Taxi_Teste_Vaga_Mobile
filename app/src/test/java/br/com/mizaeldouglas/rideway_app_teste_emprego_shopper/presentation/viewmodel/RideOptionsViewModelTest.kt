package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.LiveDataTestUtil.getOrAwaitValue
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.ConfirmRideRequest
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.ConfirmRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.DriverOption
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.Location
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.Review
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.repository.IRideRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class RideOptionsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RideOptionsViewModel
    private lateinit var rideRepository: IRideRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        rideRepository = Mockito.mock(IRideRepository::class.java)
        viewModel = RideOptionsViewModel(rideRepository)
    }

    @Test
    fun `Deve retornar sucesso ao aceitar corrida com motorista válido e distancia valida`() =
        runBlocking {
            // Arrange
            val customerId = "CT01"
            val rideResponse = EstimateRideResponse(
                origin = Location(10.0, 10.0),
                destination = Location(11.0, 11.0),
                distance = 3.0,
                duration = "10min",
                options = listOf(
                    DriverOption(
                        1,
                        "Motorista 1",
                        "Motorista experiente",
                        "Carro 1",
                        Review(4.5, "10"),
                        15.0
                    ),
                    DriverOption(
                        2,
                        "Motorista 2",
                        "Motorista premium",
                        "Carro 2",
                        Review(4.8, "20"),
                        25.0
                    )
                )
            )
            val selectedOption = rideResponse.options[0]

            // Configurando corretamente o retorno do repositório
            whenever(rideRepository.confirmRide(any<ConfirmRideRequest>())).thenReturn(Response.success(ConfirmRideResponse(success = true)))

            // Execute a lógica de selecionar a opção e aceitar a corrida
            viewModel.loadRideData(rideResponse.options, rideResponse)
            viewModel.selectOption(selectedOption)

            // Act
            viewModel.acceptRide(customerId)
            testDispatcher.scheduler.advanceUntilIdle()

            // Captura o request enviado ao repositório
            val captor = argumentCaptor<ConfirmRideRequest>()
            verify(rideRepository).confirmRide(captor.capture())

            val request = captor.firstValue
            assertThat(request.customer_id).isEqualTo(customerId)
            assertThat(request.origin).isEqualTo("10.0,10.0")
            assertThat(request.destination).isEqualTo("11.0,11.0")
            assertThat(request.distance).isEqualTo(3.0)
            assertThat(request.driver.id).isEqualTo(selectedOption.id)

            // Verifique os resultados do Toast e navegação
            val toastMessage = viewModel.toastMessage.getOrAwaitValue()
            val navigateToRideHistory = viewModel.navigateToRideHistory.getOrAwaitValue()

            assertThat(toastMessage).isEqualTo("Corrida confirmada com ${selectedOption.name}")
            assertThat(navigateToRideHistory).isTrue()
        }


    @Test
    fun `Deve retornar erro ao aceitar corrida com motorista válido e distância inválida`() = runBlocking {
        // Arrange
        val customerId = "CT01"
        val rideResponse = EstimateRideResponse(
            origin = Location(10.0, 10.0),
            destination = Location(11.0, 11.0),
            distance = 9.0, // Distância fora da faixa para DriverOption 1
            duration = "15min",
            options = listOf(
                DriverOption(
                    1,
                    "Motorista 1",
                    "Motorista experiente",
                    "Carro 1",
                    Review(4.5, "10"),
                    20.0
                )
            )
        )
        val selectedOption = rideResponse.options[0]

        viewModel.loadRideData(rideResponse.options, rideResponse)
        viewModel.selectOption(selectedOption)

        // Act
        viewModel.acceptRide(customerId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val toastMessage = viewModel.toastMessage.getOrAwaitValue()
        val navigateToRideHistory = viewModel.navigateToRideHistory.getOrAwaitValue()

        assertThat(toastMessage).isEqualTo("Erro: A distância é inválida para o motorista selecionado.")
        assertThat(navigateToRideHistory).isFalse()
    }


    @Test
    fun `Deve retornar erro ao aceitar corrida com motorista inválido`() = runBlocking {
        // Arrange
        val customerId = "CT01"
        val rideResponse = EstimateRideResponse(
            origin = Location(10.0, 10.0),
            destination = Location(11.0, 11.0),
            distance = 3.0,
            duration = "10min",
            options = listOf(
                DriverOption(
                    -1, // ID inválido
                    "Motorista Inválido",
                    "Descrição",
                    "Carro X",
                    Review(3.0, "22"),
                    10.0
                )
            )
        )
        val selectedOption = rideResponse.options[0]

        viewModel.loadRideData(rideResponse.options, rideResponse)
        viewModel.selectOption(selectedOption)

        // Act
        viewModel.acceptRide(customerId)

        // Aguardar a atualização das LiveData
        testDispatcher.scheduler.advanceUntilIdle() // Garante que a corrotina tenha tempo para finalizar

        // Assert
        val toastMessage = viewModel.toastMessage.getOrAwaitValue(time = 5, timeUnit = TimeUnit.SECONDS) // Aumentando o tempo de espera
        val navigateToRideHistory = viewModel.navigateToRideHistory.getOrAwaitValue(time = 5, timeUnit = TimeUnit.SECONDS)

        assertThat(toastMessage).isEqualTo("Erro: Motorista inválido.")
        assertThat(navigateToRideHistory).isFalse()
    }



}
