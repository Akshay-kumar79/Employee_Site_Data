package employee.location.site.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import employee.location.site.models.Activity
import java.lang.ref.WeakReference
import java.lang.reflect.Type


class PreferenceUtils(private val context: Context) {

    companion object {
        const val sp = "my_default_shared_preference"
    }

    fun save(key: String, value: Any) {
        val contextWeakReference = WeakReference(context)
        if (contextWeakReference.get() != null) {
            val prefs = context.getSharedPreferences(sp, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            when (value) {
                is Int -> editor.putInt(key, value)
                is String -> editor.putString(key, value.toString())
                is Boolean -> editor.putBoolean(key, value)
                is Long -> editor.putLong(key, value)
                is Float -> editor.putFloat(key, value)
                is Double -> editor.putLong(key, java.lang.Double.doubleToRawLongBits(value))
            }
            editor.apply()
        }
    }

    fun get(key: String, defaultValue: Any): Any? {
        val contextWeakReference = WeakReference(context)
        if (contextWeakReference.get() != null) {
            val sharedPrefs = context.getSharedPreferences(sp, Context.MODE_PRIVATE)
            try {
                when (defaultValue) {
                    is String -> return sharedPrefs.getString(key, defaultValue.toString())
                    is Int -> return sharedPrefs.getInt(key, defaultValue)
                    is Boolean -> return sharedPrefs.getBoolean(key, defaultValue)
                    is Long -> return sharedPrefs.getLong(key, defaultValue)
                    is Float -> return sharedPrefs.getFloat(key, defaultValue)
                    is Double -> return java.lang.Double.longBitsToDouble(
                        sharedPrefs.getLong(
                            key,
                            java.lang.Double.doubleToLongBits(defaultValue)
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("Exception: ", e.message?:"0")
                return defaultValue
            }

        }
        return defaultValue
    }

    fun remove(key: String) {
        val contextWeakReference = WeakReference(context)
        if (contextWeakReference.get() != null) {
            val prefs = context.getSharedPreferences(sp, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.remove(key)
            editor.apply()
        }
    }

    fun hasKey(key: String): Boolean {
        val contextWeakReference = WeakReference(context)
        if (contextWeakReference.get() != null) {
            val prefs = context.getSharedPreferences(sp, Context.MODE_PRIVATE)
            return prefs.contains(key)
        }
        return false
    }

    fun addStringArrayToSp(arrayName: String, array: ArrayList<String>){
        val gson = Gson()
        val json: String = gson.toJson(array)

        save(arrayName, json)
    }

    fun getStringArrayFromSp(arrayName: String): ArrayList<String> {
        val gson = Gson()
        val json: String = get(arrayName, "") as String
        if (json.isEmpty()){
            return arrayListOf()
        }

        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return gson.fromJson(json, type)
    }

    fun addActivityArrayToSp(arrayName: String, array: ArrayList<Activity>){
        val gson = Gson()
        val json: String = gson.toJson(array)

        save(arrayName, json)
    }

    fun getActivityArrayFromSp(arrayName: String): ArrayList<Activity> {
        val gson = Gson()
        val json: String = get(arrayName, "") as String
        if (json.isEmpty()){
            return arrayListOf()
        }

        val type: Type = object : TypeToken<ArrayList<Activity?>?>() {}.type
        return gson.fromJson(json, type)
    }

}