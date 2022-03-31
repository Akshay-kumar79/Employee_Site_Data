package employee.location.site.screens.reports.location

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import employee.location.site.R
import employee.location.site.adapters.EmployeeReportAdapter
import employee.location.site.adapters.LocationReportAdapter
import employee.location.site.databinding.FragmentEmployeeReportBinding
import employee.location.site.databinding.FragmentLocationReportBinding
import employee.location.site.databinding.FragmentLocationsBinding
import employee.location.site.screens.reports.employee.EmployeeReportFragmentArgs
import employee.location.site.viewModelFactories.EmployeeReportViewModelFactory
import employee.location.site.viewModelFactories.LocationReportViewModelFactory
import employee.location.site.viewmodels.EmployeeReportViewModel
import employee.location.site.viewmodels.LocationReportViewModel


class LocationReportFragment : Fragment() {

    private val args: LocationReportFragmentArgs by navArgs()

    private lateinit var binding: FragmentLocationReportBinding
    private lateinit var viewModel: LocationReportViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationReportBinding.inflate(inflater, container, false)

        val application: Application = requireNotNull(this.activity).application
        val viewModelFactory = LocationReportViewModelFactory(args.locationName, args.startDate, args.endDate, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[LocationReportViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setRecyclerView()

        return binding.root
    }

    private fun setRecyclerView() {
        val adapter = LocationReportAdapter()
        binding.employeeReportRv.adapter = adapter
    }

}