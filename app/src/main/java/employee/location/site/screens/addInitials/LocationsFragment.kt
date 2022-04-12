package employee.location.site.screens.addInitials

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import employee.location.site.R
import employee.location.site.adapters.EmptyDataObserver
import employee.location.site.adapters.NameAdapter
import employee.location.site.adapters.NameClickListener
import employee.location.site.databinding.FragmentLocationsBinding
import employee.location.site.utils.Constants
import employee.location.site.viewmodels.AddInitialsViewModel
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.IOException
import java.io.InputStream

class LocationsFragment : Fragment() {

    private var fileType = ""
    private val extensionXLS = "XLS"
    private val extensionXLXS = "XLXS"

    private lateinit var binding: FragmentLocationsBinding
    private lateinit var viewModel: AddInitialsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentLocationsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AddInitialsViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setUpRecyclerView()
        setClickListeners()

        return binding.root
    }

    private fun setUpRecyclerView() {
        val locationAdapter = NameAdapter(NameClickListener { pos, view ->
            val popupMenu = PopupMenu(requireContext(), view)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        val dialog = AlertDialog.Builder(requireContext())
                        dialog.setTitle("Delete")
                        dialog.setMessage("are you sure you want to delete this location?")
                        dialog.setPositiveButton("ok") { _, _ ->
                            viewModel.removeLocationFromSp(pos)
                        }
                        dialog.setNegativeButton("cancel", null)
                        dialog.setIcon(R.drawable.ic_round_delete_forever_24)
                        dialog.show()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }

            popupMenu.inflate(R.menu.name_list_click_menu_item)
            popupMenu.show()
        })
        binding.locationRv.adapter = locationAdapter
        val emptyDataObserver = EmptyDataObserver(binding.locationRv, binding.emptyRv)
        locationAdapter.registerAdapterDataObserver(emptyDataObserver)
    }

    private fun setClickListeners() {
        binding.addLocation.setOnClickListener {
            val taskEditText = EditText(requireContext())
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Add Location")
                .setMessage("Enter the Location name")
                .setView(taskEditText)
                .setPositiveButton("Add") { dialog, which ->
                    viewModel.addLocationToSp(taskEditText.text.toString().trim())
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }
    }

    private fun checkPermission(): Boolean {
        val per1 = ContextCompat.checkSelfPermission(requireActivity().applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val per2 = ContextCompat.checkSelfPermission(requireActivity().applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        return per1 == PackageManager.PERMISSION_GRANTED && per2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ), 201
        )
    }

    private val filePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null && result.data!!.data != null) {

                val uri = result.data!!.data!!

                try {
                    val inputStream: InputStream?
                    var wb: Workbook? = null
                    try {
                        inputStream = requireContext().contentResolver.openInputStream(uri)

                        if (fileType == extensionXLS)
                            wb = HSSFWorkbook(inputStream);
                        else
                            wb = XSSFWorkbook(inputStream);


                        inputStream?.close()
                    } catch (e: IOException) {
                        Toast.makeText(requireActivity().applicationContext, "First ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.d("TAG", e.message ?: "")
                    }

                    val sheet = wb!!.getSheetAt(0)
                    val names = Constants.getDataFromExcel(sheet)
                    for (name in names) {
                        viewModel.addLocationToSp(name)
                    }
                } catch (ex: Exception) {
                    Log.d("ReadExcelFile Error:", ex.message ?: "")
                }


            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.import_name_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_import_xls) {
            fileType = extensionXLS
            OpenFilePicker()
            return true
        } else if (item.itemId == R.id.action_import_xlxs) {
            fileType = extensionXLXS
            OpenFilePicker()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun OpenFilePicker() {
        try {
            if (checkPermission()) {
                ChooseFile()
            }else{
                requestPermission()
            }
        } catch (e: ActivityNotFoundException) {
            Log.d("TAG","No activity can handle picking a file. Showing alternatives.")
        }
    }

    fun ChooseFile() {
        try {
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            fileIntent.addCategory(Intent.CATEGORY_OPENABLE)
            if (fileType === extensionXLS) fileIntent.type = "application/vnd.ms-excel" else fileIntent.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            filePicker.launch(fileIntent)
        } catch (ex: java.lang.Exception) {
            Log.d("TAG","ChooseFile error: " + ex.message.toString())
        }
    }

}