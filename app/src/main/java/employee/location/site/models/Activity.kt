package employee.location.site.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Activity(
    val activityName: String,
    val cost: Int
): Parcelable
