package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.Ride
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.databinding.RideItemBinding

class RideHistoryAdapter : RecyclerView.Adapter<RideHistoryAdapter.RideViewHolder>() {

    private var rides: List<Ride> = listOf()

    fun updateData(newRides: List<Ride>) {
        rides = newRides
        notifyDataSetChanged()
    }

    class RideViewHolder(private val binding: RideItemBinding) : RecyclerView.ViewHolder(binding.root) {
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
        holder.bind(rides[position])
    }

    override fun getItemCount() = rides.size
}