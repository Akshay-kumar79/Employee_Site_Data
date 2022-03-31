package employee.location.site.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import employee.location.site.database.WorkDatabase

class ActivityReportViewModel(application: Application) : AndroidViewModel(application) {

    private val database = WorkDatabase.getInstance(application).workDao

    val allWorksForActivityReport = database.getAllWorks()

}