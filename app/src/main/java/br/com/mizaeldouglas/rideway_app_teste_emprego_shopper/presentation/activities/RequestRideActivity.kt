package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.databinding.ActivityRequestRideBinding
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.viewmodel.RequestRideViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestRideActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRequestRideBinding.inflate(layoutInflater)
    }

    private val viewModel: RequestRideViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        with(binding) {
            floatingActionButtonRequest.setOnClickListener {
                navigateToRideHistory()
            }

            btnEstimateRide.setOnClickListener {
                handleEstimateRide()
            }
        }
    }

    private fun navigateToRideHistory() {
        val intent = Intent(this, RideHistoryActivity::class.java)
        startActivity(intent)
    }

    private fun handleEstimateRide() {
        with(binding) {
            val customerId = etCustomerId.text.toString()
            val origin = etOrigin.text.toString()
            val destination = etDestination.text.toString()

            Log.i(
                "RequestRideActivity",
                "Dados enviados: \ncustomerId=$customerId,\n origin=$origin, \ndestination=$destination"
            )

            val hasError = validateInputs(customerId, origin, destination)
            if (!hasError) {
                viewModel.estimateRide(customerId, origin, destination)
            }
        }
    }

    private fun validateInputs(customerId: String, origin: String, destination: String): Boolean {
        var hasError = false

        binding.tilCustomerId.error = if (customerId.isBlank()) {
            hasError = true
            "Campo obrigatório"
        } else null

        binding.tilOrigin.error = if (origin.isBlank()) {
            hasError = true
            "Campo obrigatório"
        } else null

        binding.tilDestination.error = if (destination.isBlank()) {
            hasError = true
            "Campo obrigatório"
        } else null

        return hasError
    }

    private fun observeViewModel() {
        viewModel.rideOptions.observe(this) { rideOptions ->
            rideOptions?.let {
                navigateToRideOptions(it)
            }
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            showError(errorMessage)
        }
    }

    private fun navigateToRideOptions(rideResponse: EstimateRideResponse) {
        val intent = Intent(this, RideOptionsActivity::class.java).apply {
            putExtra("rideResponse", rideResponse)
            putExtra("rideOptions", ArrayList(rideResponse.options))
        }
        startActivity(intent)
    }

    private fun showError(errorMessage: String?) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}
