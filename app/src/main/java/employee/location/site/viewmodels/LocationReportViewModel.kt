package employee.location.site.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import employee.location.site.database.WorkDatabase

class LocationReportViewModel(
    private val locName: String,
    private val startDate: Long,
    private val endDate: Long,
    application: Application
) : AndroidViewModel(application) {


    private val database = WorkDatabase.getInstance(application).workDao

    val allWorksForLocationReport = database.getWorkForLocationReport(locName, startDate, endDate)

}