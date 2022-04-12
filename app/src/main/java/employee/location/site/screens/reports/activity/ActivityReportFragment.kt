package employee.location.site.screens.reports.activity

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import employee.location.site.adapters.EmptyDataObserver
import employee.location.site.adapters.LocationReportAdapter
import employee.location.site.databinding.FragmentActivityReportBinding
import employee.location.site.utils.PDFUtility
import employee.location.site.utils.PDF_REPORT_TYPE_ACTIVITY
import employee.location.site.viewModelFactories.ActivityReportViewModelFactory
import employee.location.site.viewModelFactories.LocationReportViewModelFactory
import employee.location.site.viewmodels.ActivityReportViewModel
import java.io.FileOutputStream

class ActivityReportFragment : Fragment(), PDFUtility.OnDocumentClose {

    private val args: ActivityReportFragmentArgs by navArgs()

    private lateinit var binding: FragmentActivityReportBinding
    private lateinit var viewModel: ActivityReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentActivityReportBinding.inflate(inflater, container, false)

        val application: Application = requireNotNull(this.activity).application
        val viewModelFactory = ActivityReportViewModelFactory(args.locationName, args.startDate, args.endDate, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[ActivityReportViewModel::class.java]

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val pdfMenuItem = menu.add("Pdf")
        pdfMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        //super.onCreateOptionsMenu(menu, inflater)
    }

    private val createPdfLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null && result.data!!.data != null) {
                val fileOutputStream = FileOutputStream(requireActivity().contentResolver.openFileDescriptor(result.data!!.data!!, "w")?.fileDescriptor)
                PDFUtility.createPdf(
                    requireContext(),
                    this,
                    viewModel.allWorksForActivityReport.value!!,
                    PDF_REPORT_TYPE_ACTIVITY,
                    "Activity Report",
                    viewModel.totalCostText.value!! ,
                    fileOutputStream,
                    true
                );
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title == "Pdf") {
            if (checkPermission()) {
                try {
                    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.type = "application/pdf"
                    intent.putExtra(Intent.EXTRA_TITLE, "activity_report.pdf")
                    createPdfLauncher.launch(intent)
                } catch (e: Exception) {
                    e.printStackTrace();
                    Log.e("TAG", "Error Creating Pdf: " + e.message);
                    Toast.makeText(requireContext(), "Error Creating Pdf: " + e.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                requestPermission()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPDFDocumentClose() {
        Toast.makeText(requireContext(), "Pdf Created", Toast.LENGTH_SHORT).show();
    }


}