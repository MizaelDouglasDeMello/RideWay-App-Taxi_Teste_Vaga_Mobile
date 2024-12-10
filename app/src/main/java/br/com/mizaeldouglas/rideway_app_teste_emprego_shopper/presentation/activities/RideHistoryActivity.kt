package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.databinding.ActivityRideHistoryBinding
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.adapters.RideHistoryAdapter
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.viewmodel.RideHistoryViewModel

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

        // Observar mudanças no histórico de viagens
        viewModel.rideHistory.observe(this) { rides ->
            adapter.updateData(rides)
        }

        // Configuração do Spinner
        val driverIds = arrayOf("1", "2", "3")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, driverIds)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDriver.adapter = spinnerAdapter

        binding.buttonApply.setOnClickListener {
            binding.spinnerDriver.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedDriverId = if (position == 0) null else driverIds[position].toIntOrNull()
                    viewModel.setDriverId(selectedDriverId)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }



        // Observar mudanças no ID do motorista e buscar histórico
        viewModel.driverId.observe(this) {
            viewModel.fetchRideHistory()
        }
    }

}