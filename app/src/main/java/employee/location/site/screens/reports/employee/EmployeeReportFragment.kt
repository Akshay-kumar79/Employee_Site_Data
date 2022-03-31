package employee.location.site.screens.reports.employee

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import employee.location.site.adapters.EmployeeReportAdapter
import employee.location.site.databinding.FragmentEmployeeReportBinding
import employee.location.site.viewModelFactories.EmployeeReportViewModelFactory
import employee.location.site.viewmodels.EmployeeReportViewModel
import employee.location.site.viewmodels.ReportViewModel

class EmployeeReportFragment : Fragment() {

    private val args: EmployeeReportFragmentArgs by navArgs()

    private lateinit var binding: FragmentEmployeeReportBinding
    private lateinit var viewModel: EmployeeReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmployeeReportBinding.inflate(inflater, container, false)

        val application: Application = requireNotNull(this.activity).application
        val viewModelFactory = EmployeeReportViewModelFactory(args.employeeName, args.startDate, args.endDate, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[EmployeeReportViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setRecyclerView()

        return binding.root
    }

    private fun setRecyclerView() {
        val adapter = EmployeeReportAdapter()
        binding.employeeReportRv.adapter = adapter
    }

}