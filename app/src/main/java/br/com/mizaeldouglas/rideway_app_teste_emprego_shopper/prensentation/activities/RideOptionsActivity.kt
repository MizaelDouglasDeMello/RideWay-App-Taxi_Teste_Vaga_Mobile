package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.prensentation.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.R
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.DriverOption
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.databinding.ActivityRideOptionsBinding
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.prensentation.adapters.RideOptionsAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class RideOptionsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var selectedOption: DriverOption
    private lateinit var mMap: GoogleMap


    private val binding by lazy {
        ActivityRideOptionsBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val rideOptions = intent.getParcelableArrayListExtra<DriverOption>("rideOptions")
            ?: listOf()

        Log.d("MapDebug", "rideOptions: $rideOptions")

        binding.tvRideSummary.text = "Found ${rideOptions.size} options for your ride"

        val adapter = RideOptionsAdapter { option ->
            selectedOption = option
            binding.btnConfirmOption.visibility = Button.VISIBLE
        }
        binding.rvRideOptions.layoutManager = LinearLayoutManager(this)
        binding.rvRideOptions.adapter = adapter

        adapter.updateData(rideOptions)

        binding.btnConfirmOption.setOnClickListener {
            confirmRide()
        }
    }

    private fun confirmRide() {
        Toast.makeText(this, "Selected option: ${selectedOption.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val rideResponse = intent.getParcelableExtra<EstimateRideResponse>("rideResponse")

        val originLat = rideResponse?.origin?.latitude
        val originLng = rideResponse?.origin?.longitude

        val destinationLat = rideResponse?.destination?.latitude
        val destinationLng = rideResponse?.destination?.longitude

        Log.d("MapDebug", "Origin: $originLat, $originLng")
        Log.d("MapDebug", "Destination: $destinationLat, $destinationLng")

        if (originLat != null && originLng != null && destinationLat != null && destinationLng != null) {

            val origin = LatLng(originLat, originLng)
            val destination = LatLng(destinationLat, destinationLng)

            mMap.addMarker(MarkerOptions().position(origin).title("Rota Origem"))
            mMap.addMarker(MarkerOptions().position(destination).title("Rota Destino"))

            val boundsBuilder = LatLngBounds.Builder()
            boundsBuilder.include(origin)
            boundsBuilder.include(destination)
            val bounds = boundsBuilder.build()


            val padding = 200
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
        }
    }

}
