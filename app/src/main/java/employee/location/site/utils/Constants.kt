package employee.location.site.utils

import employee.location.site.models.Activity
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet

object Constants {

    const val EMPLOYEE_LIST = "employee_list"
    const val LOCATION_LIST = "location_list"
    const val ACTIVITY_LIST = "activity_list"

    fun getDataFromExcel(sheet: Sheet): ArrayList<String>{

        val names = ArrayList<String>()

        for (row in sheet.rowIterator()){
            row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING)
            names.add(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).stringCellValue)
        }

        return names
    }

    fun getActivitiesFromExcel(sheet: Sheet): ArrayList<Activity>{

        val names = ArrayList<Activity>()

        for (row in sheet.rowIterator()){
            row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING)
            row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.NUMERIC)

            names.add(
                Activity(
                    row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).stringCellValue,
                    row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).numericCellValue.toInt()
                )
            )
        }

        return names
    }
}