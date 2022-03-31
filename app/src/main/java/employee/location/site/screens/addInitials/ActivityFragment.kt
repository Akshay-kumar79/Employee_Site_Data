package employee.location.site.screens.addInitials

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import employee.location.site.R
import employee.location.site.adapters.*
import employee.location.site.databinding.FragmentActivityBinding
import employee.location.site.models.Activity
import employee.location.site.viewmodels.AddInitialsViewModel


class ActivityFragment : Fragment() {

    private lateinit var binding: FragmentActivityBinding
    private lateinit var viewModel: AddInitialsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActivityBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AddInitialsViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setUpRecyclerView()
        setClickListeners()

        return binding.root
    }

    private fun setUpRecyclerView() {
        val activityAdapter = ActivityAdapter(ActivityClickListener{ pos, view ->
            val popupMenu = PopupMenu(requireContext(), view)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        val dialog = AlertDialog.Builder(requireContext())
                        dialog.setTitle("Delete")
                        dialog.setMessage("are you sure you want to delete this activity?")
                        dialog.setPositiveButton("ok") { _, _ ->
                            viewModel.removeActivityFromSp(pos)
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
        binding.activityRv.adapter = activityAdapter
        val emptyDataObserver = EmptyDataObserver(binding.activityRv, binding.emptyRv)
        activityAdapter.registerAdapterDataObserver(emptyDataObserver)
    }

    private fun setClickListeners() {
        binding.addActivity.setOnClickListener {
            val parent = LinearLayout(requireContext())
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(8, 8, 8, 8)
            parent.layoutParams = layoutParams
            parent.orientation = LinearLayout.VERTICAL

            val editLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            val nameEditText = EditText(requireContext())
            nameEditText.layoutParams = editLayoutParams
            nameEditText.hint = "activity name"

            val costEditText = EditText(requireContext())
            costEditText.layoutParams = editLayoutParams
            costEditText.hint = "activity cost"
            costEditText.inputType = InputType.TYPE_CLASS_NUMBER

            parent.addView(nameEditText)
            parent.addView(costEditText)

            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Add Activity")
                .setMessage("Enter the activity name and cost")
                .setView(parent)
                .setPositiveButton("Add") { dialog, which ->
                    viewModel.addActivityToSp(
                        Activity(nameEditText.text.toString().trim(), costEditText.text.toString().trim().toInt())
                    )
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }
    }
}