package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.prensentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.DriverOption
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.databinding.RecyclerViewItemBinding

class RideOptionsAdapter(
    private val onOptionSelected: (DriverOption) -> Unit
) : RecyclerView.Adapter<RideOptionsAdapter.ViewHolder>() {

    private var options: List<DriverOption> = listOf()

    fun updateData(newOptions: List<DriverOption>) {
        options = newOptions
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(option: DriverOption) {
            binding.tvName.text = option.name
            binding.tvDescription.text = option.description
            binding.tvVehicle.text = option.vehicle
            binding.tvRating.text = "Rating: ${option.review.rating}"
            binding.tvPrice.text = "Price: $${option.value}"
            binding.btnChoose.setOnClickListener {
                Toast.makeText(binding.root.context, "Selected option: ${option.name}", Toast.LENGTH_SHORT).show()
                onOptionSelected(option)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(options[position])
    }

    override fun getItemCount() = options.size
}
