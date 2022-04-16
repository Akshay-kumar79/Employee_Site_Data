package employee.location.site.viewModelFactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import employee.location.site.models.Activity
import employee.location.site.viewmodels.ActivityReportViewModel
import employee.location.site.viewmodels.LocationReportViewModel
import java.lang.IllegalArgumentException

class ActivityReportViewModelFactory(
    private val activity: Activity,
    private val startDate: Long,
    private val endDate: Long,
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityReportViewModel::class.java)){
            return ActivityReportViewModel(activity, startDate, endDate, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}