package employee.location.site.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "work_table")
data class Work(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var empName: String,
    var location: String,
    var activity: Activity,
    var value: Int,
    var date: Long
)
