package com.example.sdproject.view

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sdproject.common.loadImage
import com.example.sdproject.common.seeAvatar
import com.example.sdproject.common.showToast
import com.example.sdproject.databinding.FragmentDetailBinding
import com.example.sdproject.model.Employ
import com.example.sdproject.viewModel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uriFile: Uri? ->
            uriFile?.let { uri ->
                val bitmap: Bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                bitmap.scale(256, 256)
                viewModel.bitmap = bitmap.toString()
                seeAvatar(requireContext(), uri) {
                    viewModel.updateAvatar()
                }.show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.employ = arguments?.getParcelable(ITEM_EMPLOY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachDataToView()
        registerEvents()
        handleObservers()
    }

    private fun attachDataToView() {
        var employ: Employ? = arguments?.getParcelable(ITEM_EMPLOY)
        employ?.let { item ->
            binding.imgAvatar.loadImage(item.avatar)
            binding.txtFullName.text = item.fullName
            binding.txtBirthday.text = item.brithDay
            binding.txtGender.text = item.gender.nameGender
            binding.txtEmail.text = item.email
            binding.txtPhone.text = item.phone
            binding.txtSalary.text = item.salary.toString()
            binding.txtNumOffice.text = item.numberOffice.nameOffice
            binding.txtProfile.text = item.profile
        }
    }

    private fun registerEvents() {
        binding.imgAvatar.setOnClickListener {
            resultLauncher.launch("image/*")
        }
    }

    private fun handleObservers() {
        viewModel.isUpdateAvatarSuccess.observe(viewLifecycleOwner) {
            if (it) {
                showToast("Cập nhật ảnh thành công")
                binding.imgAvatar.loadImage(viewModel.bitmap)
            } else
                showToast("Cập nhật ảnh thất bai")
        }
    }

    companion object {
        private const val ITEM_EMPLOY = "employ"
        fun newInstance(employ: Employ): DetailFragment {
            val args = Bundle()
            args.putParcelable(ITEM_EMPLOY, employ)
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}