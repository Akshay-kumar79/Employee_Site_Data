package employee.location.site.screens.reports.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import employee.location.site.R
import employee.location.site.adapters.EmployeeReportAdapter
import employee.location.site.adapters.EmptyDataObserver
import employee.location.site.adapters.LocationReportAdapter
import employee.location.site.databinding.FragmentActivityReportBinding
import employee.location.site.viewmodels.ActivityReportViewModel

class ActivityReportFragment : Fragment() {

    private lateinit var binding: FragmentActivityReportBinding
    private lateinit var viewModel: ActivityReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActivityReportBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ActivityReportViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setRecyclerView()

        return binding.root
    }

    private fun setRecyclerView() {
        val adapter = LocationReportAdapter()
        binding.activityReportRv.adapter = adapter

        val emptyDataObserver = EmptyDataObserver(binding.activityReportRv, binding.emptyRv)
        adapter.registerAdapterDataObserver(emptyDataObserver)
    }

}