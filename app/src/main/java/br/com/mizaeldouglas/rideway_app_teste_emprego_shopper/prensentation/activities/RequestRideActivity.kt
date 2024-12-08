package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.prensentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.api.ApiClient
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideRequest
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.databinding.ActivityRequestRideBinding
import kotlinx.coroutines.launch

class RequestRideActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRequestRideBinding.inflate(
            layoutInflater
        )
    }


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
                    "Dados enviados: customerId=$customerId, origin=$origin, destination=$destination"
                )

                if (customerId.isBlank() || origin.isBlank() || destination.isBlank()) {
                    Toast.makeText(
                        this@RequestRideActivity,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    try {
                        val response = ApiClient.apiService.estimateRide(
                            EstimateRideRequest(customerId, origin, destination)
                        )
                        if (response.isSuccessful) {
                            val rideOptions = response.body()

                            if (rideOptions != null && rideOptions.options != null) {
                                Toast.makeText(
                                    this@RequestRideActivity,
                                    "Ride options: ${rideOptions.options}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(
                                    this@RequestRideActivity,
                                    RideOptionsActivity::class.java
                                )
                                intent.putExtra("rideOptions", ArrayList(rideOptions.options))
                                startActivity(intent)
                            }
                        } else {
                            Toast.makeText(
                                this@RequestRideActivity,
                                "Error: ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@RequestRideActivity,
                            "An error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("RequestRideActivityTrue", "Error while estimating ride", e)
                    }
                }
            }
        }
    }
}