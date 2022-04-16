package employee.location.site.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import employee.location.site.R
import employee.location.site.database.WorkDatabase
import employee.location.site.models.Activity
import employee.location.site.models.Work
import employee.location.site.utils.Constants
import employee.location.site.utils.PreferenceUtils
import java.text.SimpleDateFormat
import java.util.*

class ReportViewModel(application: Application) : AndroidViewModel(application) {

    private val preferenceUtils = PreferenceUtils(application)

    // Employee Report Fragment
    val allEmployees = MutableLiveData<ArrayList<String>>()

    val startDateForEmployeeReport = MutableLiveData<Long>()
    val endDateForEmployeeReport = MutableLiveData<Long>()
    val selectedEmployeeForEmployeeReport = MutableLiveData<String>()

    val startDateTextForEmployeeReport = Transformations.map(startDateForEmployeeReport) {
        val c = Calendar.getInstance()
        c.timeInMillis = it

        val dateFormatter = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        dateFormatter.format(c.time)
    }
    val endDateTextForEmployeeReport = Transformations.map(endDateForEmployeeReport) {
        val c = Calendar.getInstance()
        c.timeInMillis = it

        val dateFormatter = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        dateFormatter.format(c.time)
    }

    //Location Report Fragment
    val allLocations = MutableLiveData<ArrayList<String>>()

    val startDateForLocationReport = MutableLiveData<Long>()
    val endDateForLocationReport = MutableLiveData<Long>()
    val selectedLocationForLocationReport = MutableLiveData<String>()

    val startDateTextForLocationReport = Transformations.map(startDateForLocationReport) {
        val c = Calendar.getInstance()
        c.timeInMillis = it

        val dateFormatter = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        dateFormatter.format(c.time)
    }
    val endDateTextForLocationReport = Transformations.map(endDateForLocationReport) {
        val c = Calendar.getInstance()
        c.timeInMillis = it

        val dateFormatter = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        dateFormatter.format(c.time)
    }

    // Activity Report Fragment
    val startDateForActivityReport = MutableLiveData<Long>()
    val endDateForActivityReport = MutableLiveData<Long>()
    val selectedActivityForActivityReport = MutableLiveData<String>()

    val startDateTextForActivityReport = Transformations.map(startDateForActivityReport){
        val c = Calendar.getInstance()
        c.timeInMillis = it

        val dateFormatter = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        dateFormatter.format(c.time)
    }
    val endDateTextForActivityReport = Transformations.map(endDateForActivityReport){
        val c = Calendar.getInstance()
        c.timeInMillis = it

        val dateFormatter = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        dateFormatter.format(c.time)
    }

    init {
        // Employee Report Fragment
        allEmployees.value = preferenceUtils.getStringArrayFromSp(Constants.EMPLOYEE_LIST)

        val calendarEmployee = Calendar.getInstance()
        startDateForEmployeeReport.value = calendarEmployee.timeInMillis - 86400000 * 10
        endDateForEmployeeReport.value = calendarEmployee.timeInMillis
        selectedEmployeeForEmployeeReport.value = application.getString(R.string.select_employee_name)


        // Location Report Fragment
        allLocations.value = preferenceUtils.getStringArrayFromSp(Constants.LOCATION_LIST)

        val calendarLocation = Calendar.getInstance()
        startDateForLocationReport.value = calendarLocation.timeInMillis - 86400000 * 10
        endDateForLocationReport.value = calendarLocation.timeInMillis
        selectedLocationForLocationReport.value = application.getString(R.string.select_location)

        // Activity Report Fragment
        val calendarActivity = Calendar.getInstance()
        startDateForActivityReport.value = calendarActivity.timeInMillis - 86400000 * 10
        endDateForActivityReport.value = calendarActivity.timeInMillis
        selectedActivityForActivityReport.value = application.getString(R.string.select_activity)
    }

    // Employee Report Fragment
    fun isValidEmployeeDetails(): Boolean {
        if (startDateForEmployeeReport.value!! >= endDateForEmployeeReport.value!!) {
            Toast.makeText(getApplication(), "start date should be before end date", Toast.LENGTH_SHORT).show()
            return false
        } else if (selectedEmployeeForEmployeeReport.value!! == getApplication<Application?>().getString(R.string.select_employee_name)) {
            Toast.makeText(getApplication(), "please select employee name", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    // Location Report Fragment
    fun isValidLocationDetails(): Boolean {
        if (startDateForLocationReport.value!! >= endDateForLocationReport.value!!) {
            Toast.makeText(getApplication(), "start date should be before end date", Toast.LENGTH_SHORT).show()
            return false
        } else if (selectedLocationForLocationReport.value!! == getApplication<Application?>().getString(R.string.select_location)) {
            Toast.makeText(getApplication(), "please select location name", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    // Location Report Fragment
    fun isValidActivityDetails(): Boolean {
        if (startDateForActivityReport.value!! >= endDateForActivityReport.value!!) {
            Toast.makeText(getApplication(), "start date should be before end date", Toast.LENGTH_SHORT).show()
            return false
        } else if (selectedActivityForActivityReport.value!! == getApplication<Application?>().getString(R.string.select_activity)) {
            Toast.makeText(getApplication(), "please select activity", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }


}