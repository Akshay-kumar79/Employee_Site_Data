package employee.location.site.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import employee.location.site.databinding.ListItemActivityBinding
import employee.location.site.databinding.ListItemNameBinding
import employee.location.site.models.Activity
import kotlin.math.cos

class ActivityAdapter(private val activityClickListener: ActivityClickListener): RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

    private var activities = ArrayList<Activity>()
    fun setData(list: ArrayList<Activity>){
        activities = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(activities[position], activityClickListener)
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    class ViewHolder(private val binding: ListItemActivityBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemActivityBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(activity: Activity, activityClickListener: ActivityClickListener){
            val costText = "cost: " + activity.cost.toString()

            binding.nameText.text = activity.activityName
            binding.costText.text = costText
            binding.position = layoutPosition
            binding.activityClickListener = activityClickListener
            binding.executePendingBindings()
        }
    }
}

class ActivityClickListener(val clickListener: (pos: Int, view: View) -> Unit){
    fun onClick(pos: Int, view: View) = clickListener(pos, view)
}

class EmptyDataObserver constructor(rv: RecyclerView?, ev: View?): RecyclerView.AdapterDataObserver() {

    private var emptyView: View? = null
    private var recyclerView: RecyclerView? = null

    init {
        recyclerView = rv
        emptyView = ev
        checkIfEmpty()
    }


    private fun checkIfEmpty() {
        if (emptyView != null && recyclerView!!.adapter != null) {
            val emptyViewVisible = recyclerView!!.adapter!!.itemCount == 0
            emptyView!!.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
            recyclerView!!.visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
        }
    }

    override fun onChanged() {
        super.onChanged()
        checkIfEmpty()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart, itemCount)
    }

}