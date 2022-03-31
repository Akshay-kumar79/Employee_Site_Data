package employee.location.site.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import employee.location.site.models.Activity
import employee.location.site.utils.Constants
import employee.location.site.utils.PreferenceUtils


class AddInitialsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferenceUtils = PreferenceUtils(application)

    private val _allEmployees = MutableLiveData<ArrayList<String>>()
    val allEmployees: LiveData<ArrayList<String>>
    get() = _allEmployees

    private val _allLocations = MutableLiveData<ArrayList<String>>()
    val allLocations: LiveData<ArrayList<String>>
    get() = _allLocations

    private val _allActivities = MutableLiveData<ArrayList<Activity>>()
    val allActivities: LiveData<ArrayList<Activity>>
    get() = _allActivities


    private val sp = application.getSharedPreferences(PreferenceUtils.sp, Context.MODE_PRIVATE)
    private val spChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        when(key){
            Constants.EMPLOYEE_LIST -> {
                _allEmployees.value = preferenceUtils.getStringArrayFromSp(Constants.EMPLOYEE_LIST)
            }

            Constants.LOCATION_LIST -> {
                _allLocations.value = preferenceUtils.getStringArrayFromSp(Constants.LOCATION_LIST)
            }

            Constants.ACTIVITY_LIST -> {
                _allActivities.value = preferenceUtils.getActivityArrayFromSp(Constants.ACTIVITY_LIST)
            }
        }
    }

    init {
        _allEmployees.value = preferenceUtils.getStringArrayFromSp(Constants.EMPLOYEE_LIST)
        _allLocations.value = preferenceUtils.getStringArrayFromSp(Constants.LOCATION_LIST)
        _allActivities.value = preferenceUtils.getActivityArrayFromSp(Constants.ACTIVITY_LIST)
        sp.registerOnSharedPreferenceChangeListener(spChangeListener)
    }

    fun addEmployeeToSp(employeeName: String) {
        val newArray = allEmployees.value!!
        newArray.add(employeeName)

        preferenceUtils.addStringArrayToSp(Constants.EMPLOYEE_LIST, newArray)
    }

    fun removeEmployeeFromSp(pos: Int){
        val newArray = allEmployees.value!!
        newArray.removeAt(pos)

        preferenceUtils.addStringArrayToSp(Constants.EMPLOYEE_LIST, newArray)
    }

    fun addLocationToSp(locationName: String) {
        val newArray = allLocations.value!!
        newArray.add(locationName)

        preferenceUtils.addStringArrayToSp(Constants.LOCATION_LIST, newArray)
    }

    fun removeLocationFromSp(pos: Int){
        val newArray = allLocations.value!!
        newArray.removeAt(pos)

        preferenceUtils.addStringArrayToSp(Constants.LOCATION_LIST, newArray)
    }

    fun addActivityToSp(activity: Activity) {
        val newArray = allActivities.value!!
        newArray.add(activity)

        preferenceUtils.addActivityArrayToSp(Constants.ACTIVITY_LIST, newArray)
    }

    fun removeActivityFromSp(pos: Int){
        val newArray = allActivities.value!!
        newArray.removeAt(pos)

        preferenceUtils.addActivityArrayToSp(Constants.ACTIVITY_LIST, newArray)
    }

    override fun onCleared() {
        super.onCleared()
        sp.unregisterOnSharedPreferenceChangeListener(spChangeListener)
    }

}