package employee.location.site.database

import android.content.Context
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import employee.location.site.models.Activity
import employee.location.site.models.Work
import java.lang.reflect.Type
import java.util.*

@Database(entities = [Work::class], version = 1, exportSchema = false)
@TypeConverters(ActivityConverter::class)
abstract class WorkDatabase: RoomDatabase() {

    abstract val workDao: WorkDao

    companion object {

        @Volatile
        private var INSTANCE: WorkDatabase? = null

        fun getInstance(context: Context): WorkDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WorkDatabase::class.java,
                        "work_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}

object ActivityConverter {

    @TypeConverter
    @JvmStatic
    fun toActivity(value: String?): Activity? {
        if (value == null) {
            return null
        }
        val type: Type = object : TypeToken<Activity>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromActivity(activity: Activity): String {
        return Gson().toJson(activity)
    }
}