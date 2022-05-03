package employee.location.site.screens

import android.R
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import employee.location.site.MainActivity
import employee.location.site.database.WorkDao
import employee.location.site.database.WorkDatabase
import employee.location.site.databinding.FragmentMainBinding
import employee.location.site.utils.Constants
import employee.location.site.utils.PreferenceUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


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

        val languageItem = menu.add("Language")
        languageItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title == "Clear All Data") {
            AlertDialog.Builder(context)
                .setTitle("Clear All Data")
                .setMessage("Are you sure you want to clear all data?")
                .setPositiveButton(R.string.ok) { dialog, which ->
                   clearData()
                }
                .setNegativeButton(R.string.cancel, null)
                .setIcon(R.drawable.ic_dialog_alert)
                .show()

        } else if (item.title == "Language") {
            val alt_bld: AlertDialog.Builder = AlertDialog.Builder(requireContext())

            val grpname = arrayOf("English", "Arabic", "Urdu")

            alt_bld.setTitle("Select language")
            alt_bld.setSingleChoiceItems(grpname, preferenceUtils.get(Constants.LANGUAGE, Constants.LANGUAGE_ENGLISH) as Int) { dialog, dialogItem ->
                preferenceUtils.save(Constants.LANGUAGE, dialogItem)
                when (dialogItem) {
                    Constants.LANGUAGE_ENGLISH -> {
                        val language = "en"
                        setLocale(language)
                    }
                    Constants.LANGUAGE_ARABIC -> {
                        val language = "ar"
                        setLocale(language)
                    }
                    Constants.LANGUAGE_URDU -> {
                        val language = "ur"
                        setLocale(language)
                    }
                }
                dialog.dismiss()
            }
            val alert: AlertDialog = alt_bld.create()
            alert.show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun clearData() {
        lifecycleScope.launch(Dispatchers.IO) {
            database.deleteAll()
        }
        preferenceUtils.remove(Constants.ACTIVITY_LIST)
        preferenceUtils.remove(Constants.LOCATION_LIST)
        preferenceUtils.remove(Constants.EMPLOYEE_LIST)

        Toast.makeText(requireContext(), "all data is cleared", Toast.LENGTH_SHORT).show()
    }

    private fun setLocale(language: String) {

        val config = resources.configuration
        val locale = Locale(language)
        Locale.setDefault(locale)
        config.setLocale(locale)
        requireContext().applicationContext.createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)

        val refresh = Intent(requireContext(), MainActivity::class.java)
        requireActivity().finish()
        startActivity(refresh)

    }
}