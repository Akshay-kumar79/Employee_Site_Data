package employee.location.site.screens.reports.location

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import employee.location.site.R
import employee.location.site.databinding.FragmentEmployeeReportFilterBinding
import employee.location.site.databinding.FragmentLocationReportFilterBinding
import employee.location.site.screens.reports.employee.EmployeeReportFilterFragmentDirections
import employee.location.site.viewmodels.ReportViewModel
import java.util.*


class LocationReportFilterFragment : Fragment() {

    lateinit var binding: FragmentLocationReportFilterBinding
    lateinit var viewModel: ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationReportFilterBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ReportViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setClickListeners()
        setObservers()

        return binding.root
    }

    private fun setObservers() {
        viewModel.allLocations.observe(viewLifecycleOwner){
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, it)
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
                viewModel.startDateForLocationReport.value = calendar.timeInMillis
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
                viewModel.endDateForLocationReport.value = calendar.timeInMillis
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.show()
        }

        binding.goButton.setOnClickListener {
            if (viewModel.isValidLocationDetails()){
                findNavController().navigate(
                    LocationReportFilterFragmentDirections.actionLocationReportFilterFragmentToLocationReportFragment(
                    viewModel.selectedLocationForLocationReport.value!!,
                    viewModel.startDateForLocationReport.value!!,
                    viewModel.endDateForLocationReport.value!!
                ))
            }
        }
    }

}