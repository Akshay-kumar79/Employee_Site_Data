package employee.location.site.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import employee.location.site.R
import employee.location.site.database.WorkDatabase
import employee.location.site.models.Activity
import employee.location.site.models.Work
import employee.location.site.utils.Constants
import employee.location.site.utils.PreferenceUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddProductivityViewModel(application: Application) : AndroidViewModel(application) {

    private val preferenceUtils = PreferenceUtils(application)
    private val database = WorkDatabase.getInstance(application).workDao

    val time = MutableLiveData<Long>()
    val timeText = Transformations.map(time) {
        val timeValue = time.value!!

        val c = Calendar.getInstance()
        c.timeInMillis = timeValue

        val simpleTimeFormatter = SimpleDateFormat("hh:mm aa", Locale.getDefault())
        simpleTimeFormatter.format(c.time)
    }

    val date = MutableLiveData<Long>()
    val dateText = Transformations.map(date) {
        val dateValue = date.value!!

        val c = Calendar.getInstance()
        c.timeInMillis = dateValue

        val dateFormatter = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        dateFormatter.format(c.time)
    }

    val selectedEmployee = MutableLiveData<String>()
    val selectedLocation = MutableLiveData<String>()
    val selectedActivity = MutableLiveData<String>()
    var currentActivity: Activity? = null

    val allEmployees = MutableLiveData<ArrayList<String>>()
    val allLocations = MutableLiveData<ArrayList<String>>()
    val allActivities = MutableLiveData<ArrayList<Activity>>()

    val value = MutableLiveData<String>()

    val isWorkAdded = MutableLiveData<Boolean>()

    init {
        allEmployees.value = preferenceUtils.getStringArrayFromSp(Constants.EMPLOYEE_LIST)
        allLocations.value = preferenceUtils.getStringArrayFromSp(Constants.LOCATION_LIST)
        allActivities.value = preferenceUtils.getActivityArrayFromSp(Constants.ACTIVITY_LIST)
        val c = Calendar.getInstance()
        time.value = c.timeInMillis
        date.value = c.timeInMillis

        selectedEmployee.value = application.getString(R.string.select_employee_name)
        selectedLocation.value = application.getString(R.string.select_location)
        selectedActivity.value = application.getString(R.string.select_activity)

        isWorkAdded.value = false
    }

    fun onDoneClick() {
        if (isValidDetails()) {
            val workDate = Calendar.getInstance()
            val t = Calendar.getInstance()
            t.timeInMillis = time.value!!
            val d = Calendar.getInstance()
            d.timeInMillis = date.value!!

            workDate.set(Calendar.HOUR_OF_DAY, t.get(Calendar.HOUR_OF_DAY))
            workDate.set(Calendar.MINUTE, t.get(Calendar.MINUTE))
            workDate.set(Calendar.DAY_OF_MONTH, d.get(Calendar.DAY_OF_MONTH))
            workDate.set(Calendar.MONTH, d.get(Calendar.MONTH))
            workDate.set(Calendar.YEAR, d.get(Calendar.YEAR))

            viewModelScope.launch(Dispatchers.IO) {
                database.insert(
                    Work(
                        empName = selectedEmployee.value!!,
                        location = selectedLocation.value!!,
                        activity = currentActivity!!,
                        value = value.value!!.toInt(),
                        date = workDate.timeInMillis
                    )
                )
            }
            isWorkAdded.value = true
        }
    }

    private fun isValidDetails(): Boolean {
        if (selectedEmployee.value == getApplication<Application?>().getString(R.string.select_employee_name)) {
            Toast.makeText(getApplication(), "please select employee", Toast.LENGTH_SHORT).show()
            return false
        } else if (selectedLocation.value == getApplication<Application?>().getString(R.string.select_location)) {
            Toast.makeText(getApplication(), "please select location", Toast.LENGTH_SHORT).show()
            return false
        } else if (selectedActivity.value == getApplication<Application?>().getString(R.string.select_activity)) {
            Toast.makeText(getApplication(), "please select activity", Toast.LENGTH_SHORT).show()
            return false
        } else if (value.value.isNullOrEmpty()) {
            Toast.makeText(getApplication(), "please enter value", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

}