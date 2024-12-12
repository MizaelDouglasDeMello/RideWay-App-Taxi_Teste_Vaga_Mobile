package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.Ride
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.databinding.RideItemBinding
class RideHistoryAdapter(private val onFilterComplete: (Boolean) -> Unit) : RecyclerView.Adapter<RideHistoryAdapter.RideViewHolder>() {

    private var rides: List<Ride> = listOf()
    private var filteredRides: List<Ride> = listOf()
    private var selectedDriver: String = ""

    fun updateData(newRides: List<Ride>, driverName: String) {
        this.rides = newRides
        this.selectedDriver = driverName
        filterRidesByDriver()
        notifyDataSetChanged()
    }

    private fun filterRidesByDriver() {
        filteredRides = if (selectedDriver == "Todos" || selectedDriver.isEmpty()) {
            rides
        } else {
            rides.filter { it.driver.name == selectedDriver }
        }

        onFilterComplete(filteredRides.isNotEmpty())
    }


    class RideViewHolder(private val binding: RideItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ride: Ride) {
            binding.txtDate.text = ride.date
            binding.txtDriverName.text = ride.driver.name
            binding.txtOrigin.text = ride.origin
            binding.txtDestination.text = ride.destination
            binding.txtDistance.text = "${ride.distance} km"
            binding.txtDuration.text = "${ride.duration} min"
            binding.txtValue.text = "R$ ${ride.value}"
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RideItemBinding.inflate(layoutInflater, parent, false)
        return RideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RideViewHolder, position: Int) {
        holder.bind(filteredRides[position])
    }

    override fun getItemCount() = filteredRides.size
}
