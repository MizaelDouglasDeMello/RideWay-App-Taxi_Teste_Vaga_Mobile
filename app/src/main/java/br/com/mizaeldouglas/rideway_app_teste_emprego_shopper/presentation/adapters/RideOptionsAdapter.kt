package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.DriverOption
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.databinding.RecyclerViewItemBinding
class RideOptionsAdapter(
    private val listener: OnOptionSelectedListener
) : RecyclerView.Adapter<RideOptionsAdapter.ViewHolder>() {

    interface OnOptionSelectedListener {
        fun onOptionSelected(option: DriverOption)
    }

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
                listener.onOptionSelected(option)
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

