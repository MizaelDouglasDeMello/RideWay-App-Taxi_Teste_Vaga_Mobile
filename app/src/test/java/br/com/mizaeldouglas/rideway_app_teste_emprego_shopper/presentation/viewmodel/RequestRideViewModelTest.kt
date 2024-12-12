package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.LiveDataTestUtil.getOrAwaitValue
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.DriverOption
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.Location
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.Review
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.repository.IRideRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import retrofit2.Response

@ExperimentalCoroutinesApi
class RequestRideViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RequestRideViewModel
    private lateinit var rideRepository: IRideRepository

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        rideRepository = Mockito.mock(IRideRepository::class.java)
        viewModel = RequestRideViewModel(rideRepository)
    }

    @Test
    fun `when all fields are blank, should show error message`() {
        viewModel.estimateRide("", "", "")

        assertThat(viewModel.errorMessage.getOrAwaitValue()).isEqualTo("All fields must be filled.")
    }

    @Test
    fun `when ride estimation is successful with no drivers available, should update rideOptions`() =
        runBlocking {
            val origin = Location(-23.555, -46.660)
            val destination = Location(-23.563, -46.654)

            val response = mockResponse(
                EstimateRideResponse(
                    origin = origin,
                    destination = destination,
                    distance = 10.0,
                    duration = "20 min",
                    options = emptyList()
                )
            )

            whenever(rideRepository.estimateRide(any())).thenReturn(response)


            viewModel.estimateRide("123", "Av. Pres. Kenedy, 2385", "Av. Paulista, 1538")


            assertThat(viewModel.rideOptions.getOrAwaitValue()).isNotNull()
            assertThat(viewModel.rideOptions.getOrAwaitValue()?.options?.isEmpty()).isTrue()
        }

    @Test
    fun `when ride estimation is successful with 3 drivers available, should update rideOptions`() =
        runBlocking {
            val origin = Location(-23.549, -46.647)
            val destination = Location(-23.563, -46.654)

            val driver1 = DriverOption(
                id = 1,
                name = "driver1",
                description = "Experienced driver",
                vehicle = "Sedan",
                review = Review(rating = 4.5, comment = "Great service!"),
                value = 15.0
            )
            val driver2 = DriverOption(
                id = 2,
                name = "driver2",
                description = "Friendly driver",
                vehicle = "SUV",
                review = Review(rating = 4.8, comment = "Very comfortable ride"),
                value = 20.0
            )
            val driver3 = DriverOption(
                id = 3,
                name = "driver3",
                description = "Fast driver",
                vehicle = "Hatchback",
                review = Review(rating = 4.3, comment = "Quick and safe"),
                value = 10.0
            )

            val response = mockResponse(
                EstimateRideResponse(
                    origin = origin,
                    destination = destination,
                    distance = 15.0,
                    duration = "25 min",
                    options = listOf(driver1, driver2, driver3)
                )
            )

            whenever(rideRepository.estimateRide(any())).thenReturn(response)

            viewModel.estimateRide("123", "Av. Thomas Edison, 365", "Av. Paulista, 1538")

            assertThat(viewModel.rideOptions.getOrAwaitValue()?.options?.size).isEqualTo(3)
        }

    @Test
    fun `when ride estimation fails due to invalid customer ID, should show error message`() =
        runBlocking {
            val response =
                mockResponse<EstimateRideResponse>(null, 400, "Error: Invalid customer ID")
            whenever(rideRepository.estimateRide(any())).thenReturn(response)

            viewModel.estimateRide("123", "Av. Paulista, 1538", "Av. Remédios, Osasco")

            assertThat(viewModel.errorMessage.getOrAwaitValue()).isEqualTo("Error: Invalid customer ID")
        }


    @Test
    fun `when there is an error in the API, should show an error message`() = runBlocking {

        whenever(rideRepository.estimateRide(any())).thenThrow(RuntimeException("Network error"))


        viewModel.estimateRide("123", "Av. Paulista, 1538", "Av. Remédios, Osasco")


        assertThat(viewModel.errorMessage.getOrAwaitValue()).contains("An error occurred: Network error")
    }


    private fun <T> mockResponse(
        body: T? = null,
        errorCode: Int = 200,
        errorMessage: String = ""
    ): Response<T> {
        return if (body != null) {
            Response.success(body)
        } else {

            Response.error(errorCode, ResponseBody.create(null, errorMessage))
        }
    }

}
