package employee.location.site.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import employee.location.site.databinding.ListItemEmployeeReportBinding
import employee.location.site.models.Work

class EmployeeReportAdapter: RecyclerView.Adapter<EmployeeReportAdapter.ViewHolder>() {

    private var workList = emptyList<Work>()

    fun setData(list: List<Work>){
        workList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(workList[position])
    }

    override fun getItemCount(): Int {
        return workList.size
    }

    class ViewHolder(private val binding: ListItemEmployeeReportBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemEmployeeReportBinding.inflate(inflater, parent, false)
                return  ViewHolder(binding)
            }
        }

        fun bind(work: Work){
            val costText = "cost: " + work.activity.cost
            val valueText = "value: " + work.value

            binding.nameText.text = work.activity.activityName
            binding.costText.text = costText
            binding.valueText.text = valueText

            binding.executePendingBindings()
        }
    }

}