package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.prensentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.model.DriverOption
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.databinding.RecyclerViewItemBinding

class RideOptionsAdapter(
    private val onOptionSelected: (DriverOption) -> Unit
) : RecyclerView.Adapter<RideOptionsAdapter.RideOptionViewHolder>() {

    private var options = emptyList<DriverOption>()
    private var selectedPosition = -1

    fun updateData(newOptions: List<DriverOption>) {
        options = newOptions
        notifyDataSetChanged()
    }

    inner class RideOptionViewHolder(private val binding: RecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(option: DriverOption, isSelected: Boolean) {
            binding.radioButton.apply {
                text = "${option.name} - ${option.vehicle} - $${option.value}"
                isChecked = isSelected
                setOnClickListener {
                    selectedPosition = adapterPosition
                    notifyDataSetChanged()
                    onOptionSelected(option)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideOptionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewItemBinding.inflate(layoutInflater, parent, false)
        return RideOptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RideOptionViewHolder, position: Int) {
        val option = options[position]
        holder.bind(option, position == selectedPosition)
    }

    override fun getItemCount(): Int = options.size
}
