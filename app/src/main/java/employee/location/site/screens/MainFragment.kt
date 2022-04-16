package employee.location.site.screens

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import employee.location.site.R
import employee.location.site.database.WorkDao
import employee.location.site.database.WorkDatabase
import employee.location.site.databinding.FragmentMainBinding
import employee.location.site.utils.Constants
import employee.location.site.utils.PreferenceUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var database: WorkDao
    private lateinit var preferenceUtils: PreferenceUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentMainBinding.inflate(inflater, container, false)
        database = WorkDatabase.getInstance(requireContext().applicationContext).workDao
        preferenceUtils = PreferenceUtils(requireContext().applicationContext)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val item = menu.add("Clear All Data")
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title == "Clear All Data"){
            lifecycleScope.launch(Dispatchers.IO) {
                database.deleteAll()
            }
            preferenceUtils.remove(Constants.ACTIVITY_LIST)
            preferenceUtils.remove(Constants.LOCATION_LIST)
            preferenceUtils.remove(Constants.EMPLOYEE_LIST)

            Toast.makeText(requireContext(), "all data is cleared", Toast.LENGTH_SHORT).show()

        }
        return super.onOptionsItemSelected(item)
    }

}