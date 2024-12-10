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

                // Verificação se algum campo está em branco e exibe erro específico em cada campo
                var hasError = false
                if (customerId.isBlank()) {
                    tilCustomerId.error = "Campo obrigatório"
                    hasError = true
                } else {
                    tilCustomerId.error = null
                }

                if (origin.isBlank()) {
                    tilOrigin.error = "Campo obrigatório"
                    hasError = true
                } else {
                    tilOrigin.error = null
                }

                if (destination.isBlank()) {
                    tilDestination.error = "Campo obrigatório"
                    hasError = true
                } else {
                    tilDestination.error = null
                }

                // Se algum campo estiver vazio, não prossegue com a estimativa da corrida
                if (hasError) {
                    return@setOnClickListener
                }

                // Chama o ViewModel para estimar a corrida
                viewModel.estimateRide(customerId, origin, destination)
            }
        }

        // Observa a resposta da estimativa
        viewModel.rideOptions.observe(this) { rideOptions ->
            rideOptions?.let {
                val intent = Intent(this@RequestRideActivity, RideOptionsActivity::class.java)
                intent.putExtra("rideResponse", it)
                intent.putExtra("rideOptions", ArrayList(it.options))
                startActivity(intent)
            }
        }

        // Observa a mensagem de erro
        viewModel.errorMessage.observe(this) { errorMessage ->
            // Exibe a mensagem de erro para o usuário
            Toast.makeText(this@RequestRideActivity, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}
