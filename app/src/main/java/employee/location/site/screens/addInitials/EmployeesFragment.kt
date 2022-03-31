package employee.location.site.screens.addInitials

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import employee.location.site.R
import employee.location.site.adapters.EmptyDataObserver
import employee.location.site.adapters.NameAdapter
import employee.location.site.adapters.NameClickListener
import employee.location.site.databinding.FragmentEmployeesBinding
import employee.location.site.viewmodels.AddInitialsViewModel

class EmployeesFragment : Fragment() {

    private lateinit var binding: FragmentEmployeesBinding
    private lateinit var viewModel: AddInitialsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmployeesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AddInitialsViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setUpRecyclerView()
        setClickListeners()

        return binding.root
    }

    private fun setUpRecyclerView() {
        val employeeAdapter = NameAdapter(NameClickListener { pos, view->
            val popupMenu = PopupMenu(requireContext(), view)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        val dialog = AlertDialog.Builder(requireContext())
                        dialog.setTitle("Delete")
                        dialog.setMessage("are you sure you want to delete this employee?")
                        dialog.setPositiveButton("ok") { _, _ ->
                            viewModel.removeEmployeeFromSp(pos)
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
        binding.employeeRv.adapter = employeeAdapter
        val emptyDataObserver = EmptyDataObserver(binding.employeeRv, binding.emptyRv)
        employeeAdapter.registerAdapterDataObserver(emptyDataObserver)
    }

    private fun setClickListeners() {
        binding.addEmployee.setOnClickListener {
            val taskEditText = EditText(requireContext())
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Add Employee")
                .setMessage("Enter the employee name")
                .setView(taskEditText)
                .setPositiveButton("Add") { dialog, which ->
                    viewModel.addEmployeeToSp(taskEditText.text.toString().trim())
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }
    }

}