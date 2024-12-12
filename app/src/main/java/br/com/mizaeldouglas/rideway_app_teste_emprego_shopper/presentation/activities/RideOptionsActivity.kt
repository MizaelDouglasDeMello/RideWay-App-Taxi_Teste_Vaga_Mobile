package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RideOptionsActivity : AppCompatActivity(), OnMapReadyCallback, RideOptionsAdapter.OnOptionSelectedListener {

    private val viewModel: RideOptionsViewModel by viewModels()
    private lateinit var mMap: GoogleMap

    private val binding by lazy {
        ActivityRideOptionsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupMap()
        setupUI()
        observeViewModel()
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupUI() {
        binding.floatingActionButtonRideOption.setOnClickListener {
            navigateToRequestRide()
        }

        setupRecyclerView()
        loadInitialData()
    }

    private fun navigateToRequestRide() {
        val intent = Intent(this, RequestRideActivity::class.java)
        startActivity(intent)
    }

    private fun loadInitialData() {
        val rideOptions = intent.getParcelableArrayListExtra<DriverOption>("rideOptions") ?: listOf()
        val rideResponse = intent.getParcelableExtra<EstimateRideResponse>("rideResponse")
        rideResponse?.let {
            viewModel.loadRideData(rideOptions, it)
        }
    }

    private fun observeViewModel() {
        viewModel.rideOptions.observe(this) { options ->
            binding.tvRideSummary.text = "Encontradas ${options.size} opções para sua corrida"
            (binding.rvRideOptions.adapter as RideOptionsAdapter).updateData(options)
        }

        viewModel.toastMessage.observe(this) { message ->
            if (message.isNotBlank()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.selectedOption.observe(this) { option ->
            showDriverInfoBottomSheet(option)
        }

        viewModel.navigateToRideHistory.observe(this) { shouldNavigate ->
            if (shouldNavigate == true) {
                navigateToRideHistory()
                viewModel.onNavigateToRideHistoryHandled()
            }
        }
    }

    private fun navigateToRideHistory() {
        val intent = Intent(this, RideHistoryActivity::class.java)
        startActivity(intent)
    }

    private fun setupRecyclerView() {
        val adapter = RideOptionsAdapter(this)
        binding.rvRideOptions.layoutManager = LinearLayoutManager(this)
        binding.rvRideOptions.adapter = adapter
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        viewModel.rideResponse.observe(this) { rideResponse ->
            rideResponse?.let { addMarkersToMap(it) }
        }
    }

    private fun addMarkersToMap(rideResponse: EstimateRideResponse) {
        val origin = rideResponse.origin?.let { LatLng(it.latitude, it.longitude) }
        val destination = rideResponse.destination?.let { LatLng(it.latitude, it.longitude) }

        origin?.let { mMap.addMarker(MarkerOptions().position(it).title("Rota Origem")) }
        destination?.let { mMap.addMarker(MarkerOptions().position(it).title("Rota Destino")) }

        val boundsBuilder = LatLngBounds.Builder()
        origin?.let { boundsBuilder.include(it) }
        destination?.let { boundsBuilder.include(it) }
        val bounds = boundsBuilder.build()

        val padding = 200
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
    }

    override fun onOptionSelected(option: DriverOption) {
        viewModel.selectOption(option)
    }

    private fun showDriverInfoBottomSheet(option: DriverOption) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = layoutInflater.inflate(R.layout.layout_driver_info_bottom_sheet, null)

        with(bottomSheetView) {
            findViewById<TextView>(R.id.tvDriverName).text = "Motorista: ${option.name}"
            findViewById<TextView>(R.id.tvRidePrice).text = "Preço: $${option.value}"
            findViewById<TextView>(R.id.tvVehicle).text = "Veículo: ${option.vehicle}"

            findViewById<Button>(R.id.btnAcceptRide).setOnClickListener {
                bottomSheetDialog.dismiss()
                viewModel.acceptRide("CT01")
            }
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }
}