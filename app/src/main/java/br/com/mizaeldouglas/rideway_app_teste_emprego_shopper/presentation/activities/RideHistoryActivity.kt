package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.activities

import android.content.Intent
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

        setupRecyclerView()
        setupSpinner()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        val adapter = RideHistoryAdapter { hasRides ->
            if (hasRides) {
                binding.rvRideHistory.visibility = View.VISIBLE
                binding.txtNotDrive.visibility = View.GONE
            } else {
                binding.rvRideHistory.visibility = View.GONE
                binding.txtNotDrive.visibility = View.VISIBLE
            }
        }
        binding.rvRideHistory.adapter = adapter

        viewModel.rideHistory.observe(this) { rides ->
            if (rides.isNullOrEmpty()) {
                showError("Nenhum histórico de viagem disponível.")
            } else {
                val selectedDriver = binding.spinnerDriver.selectedItem.toString()
                adapter.updateData(rides, selectedDriver)
            }
        }
    }




    private fun setupSpinner() {
        val driverIds = arrayOf("Homer Simpson", "Dominic Toretto", "James Bond")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, driverIds)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDriver.adapter = spinnerAdapter
    }

    private fun setupObservers() {
        viewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                showError(it)
            }
        }

        viewModel.driverId.observe(this) {
            viewModel.fetchRideHistory()
        }
    }

    private fun setupClickListeners() {
        binding.floatingActionButton.setOnClickListener {
            navigateToRequestRide()
        }

        binding.buttonApply.setOnClickListener {
            handleApplyButtonClick()
        }
    }

    private fun handleApplyButtonClick() {
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

        val selectedDriver = binding.spinnerDriver.selectedItem.toString()
        if (selectedDriver.isEmpty()) {
            binding.errorDriver.visibility = View.VISIBLE
        } else {
            binding.errorDriver.visibility = View.GONE
            val driverId = getDriverIdByName(selectedDriver)
            viewModel.setDriverId(driverId)
            viewModel.fetchRideHistory()
        }
    }

    private fun getDriverIdByName(driverName: String): Int? {
        return when (driverName) {
            "Homer Simpson" -> 1
            "Dominic Toretto" -> 2
            "James Bond" -> 3
            else -> null
        }
    }



    private fun navigateToRequestRide() {
        val intent = Intent(this, RequestRideActivity::class.java)
        startActivity(intent)
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}