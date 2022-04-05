package employee.location.site.viewmodels

import android.app.Application
import android.content.pm.PackageManager
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import employee.location.site.MainActivity
import employee.location.site.database.WorkDatabase
import java.io.File
import java.io.FileOutputStream

class ActivityReportViewModel(application: Application) : AndroidViewModel(application) {

    private val database = WorkDatabase.getInstance(application).workDao

    val allWorksForActivityReport = database.getAllWorks()

    val totalCostText = Transformations.map(allWorksForActivityReport){ works ->
        var totalCost = 0
        for (work in works){
            totalCost += work.activity.cost
        }
        "Total Cost = $totalCost"
    }

}