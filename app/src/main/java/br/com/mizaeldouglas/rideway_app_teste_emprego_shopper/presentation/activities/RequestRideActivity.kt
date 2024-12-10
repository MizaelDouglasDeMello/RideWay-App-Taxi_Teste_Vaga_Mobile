package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.databinding.ActivityRequestRideBinding
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.viewmodel.RequestRideViewModel

class RequestRideActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRequestRideBinding.inflate(layoutInflater)
    }

    private val viewModel: RequestRideViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            btnEstimateRide.setOnClickListener {
                val customerId = etCustomerId.text.toString()
                val origin = etOrigin.text.toString()
                val destination = etDestination.text.toString()

                Log.i(
                    "RequestRideActivityTrue",
                    "Dados enviados: \ncustomerId=$customerId,\n origin=$origin, \ndestination=$destination"
                )

                if (customerId.isBlank() || origin.isBlank() || destination.isBlank()) {
                    Toast.makeText(
                        this@RequestRideActivity,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.estimateRide(customerId, origin, destination)
            }
        }

        viewModel.rideOptions.observe(this) { rideOptions ->
            rideOptions?.let {
                val intent = Intent(this@RequestRideActivity, RideOptionsActivity::class.java)
                intent.putExtra("rideResponse", it)
                intent.putExtra("rideOptions", ArrayList(it.options))
                startActivity(intent)
            }
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this@RequestRideActivity, errorMessage, Toast.LENGTH_SHORT).show()
        }

    }

}
