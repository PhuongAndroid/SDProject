package com.example.sdproject.view

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.scale
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.sdproject.common.BIRTHDAY_FORMAT
import com.example.sdproject.common.convertToTime
import com.example.sdproject.common.createQuestionDialog
import com.example.sdproject.common.loadImage
import com.example.sdproject.common.showToast
import com.example.sdproject.databinding.FragmentNewBinding
import com.example.sdproject.model.Gender
import com.example.sdproject.model.NumOffice
import com.example.sdproject.viewModel.NewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewFragment : Fragment() {

    private lateinit var binding: FragmentNewBinding
    private val viewModel: NewViewModel by viewModels()

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uriFile: Uri? ->
            uriFile?.let { uri ->
                binding.imgAvatar.loadImage(uri)
                val bitmap: Bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                bitmap.scale(256, 256)
                viewModel.bitmap = bitmap.toString()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerEvents()
        handleObservers()
    }

    private fun registerEvents() {
        binding.btnNew.setOnClickListener {
            val re = Regex("[^A-Za-z0-9 ]")
            val birthDay = re.replace(binding.txtBirthday.text.toString(), "")
            if (viewModel.canSaveBirthDay(birthDay))
                canNewDialog()
            else
                onNew()
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
        binding.imgAvatar.setOnClickListener {
            resultLauncher.launch("image/*")
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

        viewModel.isNewSuccess.observe(viewLifecycleOwner) {
            if (it)
                showToast("Tạo thành công")
        }
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

    private fun canNewDialog() {
        createQuestionDialog(requireContext()) {
            onNew()
        }.show()
    }

    private fun onNew() {
        val birthDay = binding.txtBirthday.text.toString()
        val fullName = binding.edtFullName.text.toString()
        val email = binding.edtEmail.text.toString()
        val phone = binding.edtPhone.text.toString()
        val salary = binding.edtSalary.text.toString()
        val profile = binding.edtProfile.text.toString()
        val gender = viewModel.gender
        val numOffice = viewModel.numOffice
        viewModel.newEmploy(
            fullName,
            birthDay,
            gender.value.express,
            email,
            phone,
            salary.toDouble(),
            numOffice.value.number,
            profile,
            viewModel.bitmap
        )
    }

}