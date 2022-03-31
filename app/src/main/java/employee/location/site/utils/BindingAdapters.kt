package employee.location.site.utils

import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import employee.location.site.adapters.ActivityAdapter
import employee.location.site.adapters.EmployeeReportAdapter
import employee.location.site.adapters.LocationReportAdapter
import employee.location.site.adapters.NameAdapter
import employee.location.site.models.Activity
import employee.location.site.models.Work

@BindingAdapter("listNames")
fun listNames(recyclerView: RecyclerView, data: ArrayList<String>?){
    if (data != null){
        val adapter = recyclerView.adapter as NameAdapter
        adapter.setData(data)
    }
}

@BindingAdapter("listActivities")
fun listActivities(recyclerView: RecyclerView, data: ArrayList<Activity>?){
    if (data != null){
        val adapter = recyclerView.adapter as ActivityAdapter
        adapter.setData(data)
    }
}

// report
@BindingAdapter("listEmployeeReport")
fun listEmployeeReport(recyclerView: RecyclerView, data: List<Work>?){
    if (data != null){
        val adapter = recyclerView.adapter as EmployeeReportAdapter
        adapter.setData(data)
    }
}

@BindingAdapter("listLocationReport")
fun listLocationReport(recyclerView: RecyclerView, data: List<Work>?){
    if (data != null){
        val adapter = recyclerView.adapter as LocationReportAdapter
        adapter.setData(data)
    }
}