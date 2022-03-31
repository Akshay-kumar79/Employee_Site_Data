package employee.location.site.screens.addInitials

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import employee.location.site.databinding.FragmentEmployeeLocationActivityBinding
import employee.location.site.viewmodels.AddInitialsViewModel

class EmployeeLocationActivityFragment : Fragment() {

    private lateinit var binding: FragmentEmployeeLocationActivityBinding
    private lateinit var viewModel: AddInitialsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmployeeLocationActivityBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(AddInitialsViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setClickListeners()

        return binding.root
    }

    private fun setClickListeners() {
        binding.employees.setOnClickListener {
            findNavController().navigate(EmployeeLocationActivityFragmentDirections.actionEmployeeLocationActivityFragmentToEmployeesFragment())
        }

        binding.locations.setOnClickListener {
            findNavController().navigate(EmployeeLocationActivityFragmentDirections.actionEmployeeLocationActivityFragmentToLocationsFragment())
        }

        binding.activities.setOnClickListener {
            findNavController().navigate(EmployeeLocationActivityFragmentDirections.actionEmployeeLocationActivityFragmentToActivityFragment())
        }
    }

}