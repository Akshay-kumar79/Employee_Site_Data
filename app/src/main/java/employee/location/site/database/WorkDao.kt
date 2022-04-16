package employee.location.site.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.ABORT
import androidx.room.Query
import androidx.room.Update
import employee.location.site.models.Activity
import employee.location.site.models.Work

@Dao
interface WorkDao {

    @Insert
    fun insert(work: Work): Long

    @Update(onConflict = ABORT)
    fun update(work: Work)

    @Query("select * from work_table where id == :id")
    fun getWork(id: Long): LiveData<Work>

    @Query("select * from work_table where empName == :employeeName and date >= :startDate and date <= :endDate")
    fun getWorkForEmployeeReport(employeeName: String, startDate: Long, endDate: Long): LiveData<List<Work>>

    @Query("select * from work_table where location == :locationName and date >= :startDate and date <= :endDate")
    fun getWorkForLocationReport(locationName: String, startDate: Long, endDate: Long): LiveData<List<Work>>

    @Query("select * from work_table where activity == :activity and date >= :startDate and date <= :endDate")
    fun getWorkForActivityReport(activity: Activity, startDate: Long, endDate: Long): LiveData<List<Work>>

    @Query("delete from work_table where id == :id")
    fun delete(id: Long)

    @Query("select * from work_table order by id desc")
    fun getAllWorks(): LiveData<List<Work>>

    @Query("delete from work_table")
    fun deleteAll()
}