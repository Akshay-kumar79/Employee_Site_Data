package employee.location.site.screens.reports.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import employee.location.site.R
import employee.location.site.databinding.FragmentActivityReportFilterBinding
import employee.location.site.models.Activity
import employee.location.site.utils.Constants
import employee.location.site.utils.PreferenceUtils
import employee.location.site.viewmodels.ReportViewModel
import java.util.*
import kotlin.collections.ArrayList

class ActivityReportFilterFragment : Fragment() {

    private lateinit var binding: FragmentActivityReportFilterBinding
    private lateinit var viewModel: ReportViewModel
    private lateinit var preferenceUtils: PreferenceUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityReportFilterBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ReportViewModel::class.java]
        preferenceUtils = PreferenceUtils(requireContext().applicationContext)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setClickListeners()
        setObservers()

        return binding.root
    }

    private fun setObservers() {
        viewModel.allActivities.observe(viewLifecycleOwner) {
            val stringArray = ArrayList<String>()
            for(activity in it){
                stringArray.add(activity.activityName)
            }

            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, stringArray)
            binding.selectLocationDropdown.setAdapter(arrayAdapter)
        }
    }

    private fun setClickListeners() {
        binding.selectStartDateButton.setOnClickListener {
            val c = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(requireContext(), { view, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                viewModel.startDateForActivityReport.value = calendar.timeInMillis
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.show()
        }

        binding.selectEndDateButton.setOnClickListener {
            val c = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(requireContext(), { view, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                viewModel.endDateForActivityReport.value = calendar.timeInMillis
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.show()
        }

        binding.goButton.setOnClickListener {
            if (viewModel.isValidActivityDetails()) {

                var activity = Activity(getString(R.string.select_activity), 0)
                for (act in preferenceUtils.getActivityArrayFromSp(Constants.ACTIVITY_LIST)) {
                    if (act.activityName == viewModel.selectedActivityForActivityReport.value!!) {
                        activity = act
                        break
                    }
                }
                findNavController().navigate(
                    ActivityReportFilterFragmentDirections.actionActivityReportFilterFragmentToActivityReportFragment(
                        activity,
                        activity.activityName,
                        viewModel.startDateForActivityReport.value!!,
                        viewModel.endDateForActivityReport.value!!
                    )
                )

            }
        }
    }

}