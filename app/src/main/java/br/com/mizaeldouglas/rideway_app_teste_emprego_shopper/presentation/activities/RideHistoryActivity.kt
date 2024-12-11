package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.activities

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.databinding.ActivityRideHistoryBinding
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.adapters.RideHistoryAdapter
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.viewmodel.RideHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RideHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRideHistoryBinding
    private lateinit var viewModel: RideHistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRideHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RideHistoryViewModel::class.java]
        binding.viewModel = viewModel

        val adapter = RideHistoryAdapter()
        binding.rvRideHistory.adapter = adapter

        viewModel.rideHistory.observe(this) { rides ->
            if (rides.isNullOrEmpty()) {
                showError("No ride history available.")
            } else {
                adapter.updateData(rides)
            }
        }
        val driverIds = arrayOf("1", "2", "3")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, driverIds)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDriver.adapter = spinnerAdapter

        binding.buttonApply.setOnClickListener {
            val customerId = binding.etCustomerId.text.toString()


            if (customerId.isEmpty()) {
                binding.tilCustomersId.error = "ID do Passageiro é obrigatório"
                binding.tilCustomersId.isErrorEnabled = true
            } else {
                binding.tilCustomersId.error = null
                binding.tilCustomersId.isErrorEnabled = false


                viewModel.customerId.value = customerId


                viewModel.fetchRideHistory()
            }


            val selectedDriverId = binding.spinnerDriver.selectedItemPosition
            if (selectedDriverId == 0) {
                binding.errorDriver.visibility = View.VISIBLE
            } else {
                binding.errorDriver.visibility = View.GONE

                val driverId = driverIds[selectedDriverId]
                viewModel.setDriverId(driverId.toInt())
            }
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                showError(it)
            }
        }

        viewModel.driverId.observe(this) {
            viewModel.fetchRideHistory()
        }
    }

    // Método auxiliar para exibir erro
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
