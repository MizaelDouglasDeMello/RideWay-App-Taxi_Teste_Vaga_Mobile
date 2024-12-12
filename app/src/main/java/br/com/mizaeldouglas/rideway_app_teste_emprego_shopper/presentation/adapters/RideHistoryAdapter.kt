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

    // Método para atualizar os dados do RecyclerView
    fun updateData(newRides: List<Ride>, driverName: String) {
        this.rides = newRides
        this.selectedDriver = driverName
        filterRidesByDriver()
        notifyDataSetChanged()
    }

    // Método para filtrar as viagens de acordo com o motorista selecionado
    private fun filterRidesByDriver() {
        filteredRides = if (selectedDriver.isNotEmpty()) {
            rides.filter { it.driver.name == selectedDriver }
        } else {
            rides
        }

        // Verifica se há viagens filtradas e aciona a visibilidade
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
        holder.bind(filteredRides[position]) // Usando filteredRides para garantir que o filtro seja aplicado
    }

    override fun getItemCount() = filteredRides.size // Usando filteredRides aqui também
}
