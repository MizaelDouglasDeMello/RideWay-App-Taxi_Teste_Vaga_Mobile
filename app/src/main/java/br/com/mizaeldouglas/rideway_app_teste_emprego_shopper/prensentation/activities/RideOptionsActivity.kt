package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.prensentation.activities

import android.os.Bundle
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
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.prensentation.adapters.RideOptionsAdapter

class RideOptionsActivity : AppCompatActivity() {
    private lateinit var selectedOption: DriverOption
    private lateinit var btnConfirmOption: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_options)

        val tvRideSummary = findViewById<TextView>(R.id.tvRideSummary)
        val rvRideOptions = findViewById<RecyclerView>(R.id.rvRideOptions)
        btnConfirmOption = findViewById(R.id.btnConfirmOption)

        val rideOptions = intent.getParcelableArrayListExtra<DriverOption>("rideOptions")
            ?: listOf()

        tvRideSummary.text = "Found ${rideOptions.size} options for your ride"

        val adapter = RideOptionsAdapter { option ->
            selectedOption = option
            btnConfirmOption.visibility = Button.VISIBLE
        }
        rvRideOptions.layoutManager = LinearLayoutManager(this)
        rvRideOptions.adapter = adapter

        adapter.updateData(rideOptions)

        btnConfirmOption.setOnClickListener {
            confirmRide()
        }
    }

    private fun confirmRide() {
        Toast.makeText(this, "Selected option: ${selectedOption.name}", Toast.LENGTH_SHORT).show()
    }
}
