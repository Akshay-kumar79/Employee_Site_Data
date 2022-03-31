package employee.location.site.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import employee.location.site.databinding.ListItemNameBinding

class NameAdapter(private val nameClickListener: NameClickListener): RecyclerView.Adapter<NameAdapter.ViewHolder>() {

    private var list = ArrayList<String>()
    fun setData(nameList: ArrayList<String>){
        list = nameList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], nameClickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(private val binding: ListItemNameBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemNameBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(name: String, nameClickListener: NameClickListener){
            binding.nameText.text = name
            binding.position = layoutPosition
            binding.nameClickListener = nameClickListener
            binding.executePendingBindings()
        }
    }
}

class NameClickListener(val clickListener: (pos: Int, view: View) -> Unit){
    fun onClick(pos: Int, view: View) = clickListener(pos, view)
}