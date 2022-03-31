package employee.location.site.screens.enterEmployeeProductivity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import employee.location.site.R
import employee.location.site.databinding.FragmentAddEmployeeProductivityBinding
import employee.location.site.viewmodels.AddProductivityViewModel
import java.util.*
import kotlin.math.min

class AddEmployeeProductivityFragment : Fragment() {

    private lateinit var binding: FragmentAddEmployeeProductivityBinding
    private lateinit var viewModel: AddProductivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEmployeeProductivityBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AddProductivityViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setClickListeners()
        setObservers()
        setSpinners()

        return binding.root
    }

    private fun setSpinners() {
        binding.selectActivityDropdown.setOnItemClickListener { parent, view, position, id ->
            val selectedActivity = viewModel.allActivities.value!![position]
            viewModel.currentActivity = selectedActivity
        }
    }

    private fun setObservers() {
        viewModel.allEmployees.observe(viewLifecycleOwner){
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, it)
            binding.selectEmployeeNameDropdown.setAdapter(arrayAdapter)
        }

        viewModel.allLocations.observe(viewLifecycleOwner){
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, it)
            binding.selectLocationDropdown.setAdapter(arrayAdapter)
        }

        viewModel.allActivities.observe(viewLifecycleOwner){
            val list = arrayListOf<String>()
            for (activity in it){
                list.add(activity.activityName)
            }
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, list)
            binding.selectActivityDropdown.setAdapter(arrayAdapter)
        }

        viewModel.isWorkAdded.observe(viewLifecycleOwner){
            if (it){
                Toast.makeText(requireActivity().applicationContext, "Employee Productivity Added Successfully", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun setClickListeners() {
        binding.selectTimeButton.setOnClickListener {
            val c = Calendar.getInstance()
            val timePicker = TimePickerDialog(requireContext(), { view, hourOfDay, minute ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                viewModel.time.value = calendar.timeInMillis
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false)

            timePicker.show()
        }

        binding.selectDateButton.setOnClickListener {
            val c = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(), { view, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.YEAR, year)
                viewModel.date.value = calendar.timeInMillis
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))

            datePicker.show()
        }
    }

}