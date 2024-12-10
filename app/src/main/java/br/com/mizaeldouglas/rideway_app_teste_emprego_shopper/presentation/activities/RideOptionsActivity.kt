package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.R
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.DriverOption
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.EstimateRideResponse
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.databinding.ActivityRideOptionsBinding
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.adapters.RideOptionsAdapter
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.viewmodel.RideOptionsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog

class RideOptionsActivity : AppCompatActivity(), OnMapReadyCallback, RideOptionsAdapter.OnOptionSelectedListener {

    private lateinit var rideOptionsViewModel: RideOptionsViewModel
    private lateinit var mMap: GoogleMap

    private val binding by lazy {
        ActivityRideOptionsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        rideOptionsViewModel = ViewModelProvider(this)[RideOptionsViewModel::class.java]

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val rideOptions = intent.getParcelableArrayListExtra<DriverOption>("rideOptions") ?: listOf()
        val rideResponse = intent.getParcelableExtra<EstimateRideResponse>("rideResponse")!!
        rideOptionsViewModel.loadRideData(rideOptions, rideResponse)

        setupObservers()
        setupRecyclerView()
    }

    private fun setupObservers() {

        rideOptionsViewModel.rideOptions.observe(this) { options ->
            binding.tvRideSummary.text = "Found ${options.size} options for your ride"
            (binding.rvRideOptions.adapter as RideOptionsAdapter).updateData(options)
        }

        rideOptionsViewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        rideOptionsViewModel.selectedOption.observe(this) { option ->
            showDriverInfoBottomSheet(option)
        }

        // Observando a navegação
        rideOptionsViewModel.navigateToRideHistory.observe(this) { shouldNavigate ->
            shouldNavigate?.let {
                if (it) {
                    // Navega para a tela RideHistoryActivity
                    val intent = Intent(this, RideHistoryActivity::class.java)
                    startActivity(intent)

                    // Resetando o evento de navegação
                    rideOptionsViewModel.onNavigateToRideHistoryHandled()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val adapter = RideOptionsAdapter(this)
        binding.rvRideOptions.layoutManager = LinearLayoutManager(this)
        binding.rvRideOptions.adapter = adapter
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        rideOptionsViewModel.rideResponse.observe(this) { rideResponse ->
            val originLat = rideResponse?.origin?.latitude
            val originLng = rideResponse?.origin?.longitude
            val destinationLat = rideResponse?.destination?.latitude
            val destinationLng = rideResponse?.destination?.longitude

            val origin = originLat?.let { originLng?.let { it1 -> LatLng(it, it1) } }
            val destination = destinationLat?.let { destinationLng?.let { it1 -> LatLng(it, it1) } }

            origin?.let { MarkerOptions().position(it).title("Rota Origem") }
                ?.let { mMap.addMarker(it) }
            destination?.let { MarkerOptions().position(it).title("Rota Destino") }
                ?.let { mMap.addMarker(it) }

            val boundsBuilder = LatLngBounds.Builder()
            origin?.let { boundsBuilder.include(it) }
            destination?.let { boundsBuilder.include(it) }
            val bounds = boundsBuilder.build()

            val padding = 200
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
        }
    }

    override fun onOptionSelected(option: DriverOption) {
        rideOptionsViewModel.selectOption(option)
    }

    private fun showDriverInfoBottomSheet(option: DriverOption) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = layoutInflater.inflate(R.layout.layout_driver_info_bottom_sheet, null)

        bottomSheetView.findViewById<TextView>(R.id.tvDriverName).text = "Driver: ${option.name}"
        bottomSheetView.findViewById<TextView>(R.id.tvRidePrice).text = "Price: $${option.value}"
        bottomSheetView.findViewById<TextView>(R.id.tvVehicle).text = "Vehicle: ${option.vehicle}"

        val btnAcceptRide = bottomSheetView.findViewById<Button>(R.id.btnAcceptRide)
        btnAcceptRide.setOnClickListener {
            bottomSheetDialog.dismiss()
            rideOptionsViewModel.acceptRide("CT01")
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }
}
