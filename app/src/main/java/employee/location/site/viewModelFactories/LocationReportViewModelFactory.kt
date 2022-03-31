package employee.location.site.viewModelFactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import employee.location.site.viewmodels.EmployeeReportViewModel
import employee.location.site.viewmodels.LocationReportViewModel
import java.lang.IllegalArgumentException

class LocationReportViewModelFactory(
    private val locName: String,
    private val startDate: Long,
    private val endDate: Long,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationReportViewModel::class.java)){
            return LocationReportViewModel(locName, startDate, endDate, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}