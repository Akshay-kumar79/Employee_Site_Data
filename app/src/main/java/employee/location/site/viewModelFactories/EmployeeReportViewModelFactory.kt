package employee.location.site.viewModelFactories

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import employee.location.site.viewmodels.EmployeeReportViewModel
import java.lang.IllegalArgumentException

class EmployeeReportViewModelFactory(
    private val empName: String,
    private val startDate: Long,
    private val endDate: Long,
    private val application: Application
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmployeeReportViewModel::class.java)){
            return EmployeeReportViewModel(empName, startDate, endDate, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}