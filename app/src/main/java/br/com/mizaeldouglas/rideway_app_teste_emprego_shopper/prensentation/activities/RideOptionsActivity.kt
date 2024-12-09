package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.prensentation.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.R
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.DriverOption
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.databinding.ActivityRideOptionsBinding
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.prensentation.adapters.RideOptionsAdapter

class RideOptionsActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRideOptionsBinding.inflate(layoutInflater)
    }

    private var selectedOption: DriverOption? = null // Definindo a variável selectedOption

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val rideOptions = intent.getParcelableArrayListExtra<DriverOption>("rideOptions")
            ?: listOf()

        binding.tvRideSummary.text = "Found ${rideOptions.size} options for your ride"

        val adapter = RideOptionsAdapter { option ->
            selectedOption = option
            // Exibir detalhes da opção selecionada
            binding.rvRideOptions.visibility = View.VISIBLE
        }

        binding.rvRideOptions.layoutManager = LinearLayoutManager(this)
        binding.rvRideOptions.adapter = adapter

        adapter.updateData(rideOptions)

        binding.btnConfirmOption.setOnClickListener {
            confirmRide()
        }
    }

    private fun confirmRide() {
        selectedOption?.let {
            Toast.makeText(this, "Selected option: ${it.name}", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(this, "No option selected", Toast.LENGTH_SHORT).show()
        }
    }
}

