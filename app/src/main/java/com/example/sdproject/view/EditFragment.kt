package com.example.sdproject.view

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.sdproject.MainViewModel
import com.example.sdproject.common.BIRTHDAY_FORMAT
import com.example.sdproject.common.convertToTime
import com.example.sdproject.common.showToast
import com.example.sdproject.databinding.FragmentEditBinding
import com.example.sdproject.model.Employ
import com.example.sdproject.model.Gender
import com.example.sdproject.model.NumOffice
import com.example.sdproject.viewModel.EditViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: EditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.receiverEmploy = arguments?.getParcelable(editEmploy)
            viewModel.editEmploy = viewModel.receiverEmploy?.copy()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.editEmploy?.let {
            initView(it)
        }
        registerEvents()
        handleObservers()
    }

    private fun initView(employ: Employ) {
        binding.rgGender.isVisible = false
        binding.rgOffice.isVisible = false
        binding.edtFullName.setText(employ.fullName)
        binding.txtBirthday.text = employ.brithDay
        binding.txtGender.text = employ.gender.nameGender
        binding.edtEmail.setText(employ.email)
        binding.edtPhone.setText(employ.phone)
        binding.edtSalary.setText(employ.salary.toString())
        binding.txtNumOffice.text = employ.numberOffice.nameOffice
        binding.edtProfile.setText(employ.profile)
    }

    private fun registerEvents() {
        binding.btnEdit.setOnClickListener {
            getEmployEdit()
            viewModel.postEdit()
        }

        binding.txtGender.setOnClickListener {
            binding.rgGender.isVisible = true
        }
        binding.txtNumOffice.setOnClickListener {
            binding.rgOffice.isVisible = true
        }
        binding.rdoNam.setOnClickListener {
            viewModel.gender.value = Gender.NAM
        }
        binding.rdoNu.setOnClickListener {
            viewModel.gender.value = Gender.NU
        }
        binding.rdo1.setOnClickListener {
            viewModel.numOffice.value = NumOffice.BanGiamDoc
        }
        binding.rdo2.setOnClickListener {
            viewModel.numOffice.value = NumOffice.PhongKeToan
        }
        binding.rdo3.setOnClickListener {
            viewModel.numOffice.value = NumOffice.PhongKinhDoanh
        }
        binding.rdo4.setOnClickListener {
            viewModel.numOffice.value = NumOffice.PhongThietKe
        }
        binding.txtBirthday.setOnClickListener {
            showDatePicker()
        }
    }

    private fun handleObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gender.collect {
                    binding.rgGender.isVisible = false
                    binding.txtGender.text = it.nameGender
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.numOffice.collect {
                    binding.rgOffice.isVisible = false
                    binding.txtNumOffice.text = it.nameOffice
                }
            }
        }
        viewModel.isUpdateSuccess.observe(viewLifecycleOwner) { isUpdated ->
            if (isUpdated) {
                showToast("Cập nhật thành công")
            }
        }
    }

    private fun getEmployEdit() {
        val fullName = binding.edtFullName.text.toString()
        val birthDay = binding.txtBirthday.text.toString()
        val email = binding.edtEmail.text.toString()
        val phone = binding.edtPhone.text.toString()
        val salary = binding.edtSalary.text.toString()
        val profile = binding.edtProfile.text.toString()
        val gender = viewModel.gender
        val numOffice = viewModel.numOffice
        viewModel.editEmploy = viewModel.editEmploy?.copy(
            fullName = fullName,
            brithDay = birthDay,
            email = email,
            phone = phone,
            gender = gender.value,
            numberOffice = numOffice.value
        )
        viewModel.editEmploy?.salary = salary.toDouble()
        viewModel.editEmploy?.profile = profile
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireActivity(), { view, year, monthOfYear, dayOfMonth ->
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, monthOfYear)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                viewModel.birthDay = c.timeInMillis
                binding.txtBirthday.text = c.timeInMillis.convertToTime(BIRTHDAY_FORMAT)
            }, year, month, day)
        datePickerDialog.show()
    }

    companion object {
        private const val editEmploy = "edit"
        fun newInstance(employ: Employ): EditFragment {
            val args = Bundle()
            args.putParcelable(editEmploy, employ)
            val fragment = EditFragment()
            fragment.arguments = args
            return fragment
        }
    }
}