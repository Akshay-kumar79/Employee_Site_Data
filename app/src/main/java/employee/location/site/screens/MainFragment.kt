package employee.location.site.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import employee.location.site.R
import employee.location.site.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        setClickListeners()

        return binding.root
    }

    private fun setClickListeners() {
        binding.employeeLocationActivityButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToEmployeeLocationActivityFragment())
        }

        binding.employeeProductivityButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToAddEmployeeProductivityFragment())
        }

        binding.employeeReport.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToEmployeeReportFilterFragment())
        }

        binding.locationReport.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToLocationReportFilterFragment())
        }

        binding.activityReport.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToActivityReportFilterFragment())
        }
    }

}