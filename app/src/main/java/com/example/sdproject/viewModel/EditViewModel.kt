package com.example.sdproject.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sdproject.model.Employ
import com.example.sdproject.model.Gender
import com.example.sdproject.model.NumOffice
import com.example.sdproject.repository.EmployRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val repository: EmployRepository
) : ViewModel() {

    var receiverEmploy: Employ? = null
    var editEmploy: Employ? = null
    var gender: MutableStateFlow<Gender> = MutableStateFlow(Gender.NAM)
    var numOffice: MutableStateFlow<NumOffice> = MutableStateFlow(NumOffice.BanGiamDoc)
    var birthDay: Long = -1
    var isUpdateSuccess: MutableLiveData<Boolean> = MutableLiveData()

    fun postEdit() {
        viewModelScope.launch(Dispatchers.IO) {
            val employRemote = editEmploy!!.convertToEmployRemote()
            val response = repository.updateEmployee(employRemote)
            if (response.isSuccessful) {
                receiverEmploy?.clone(editEmploy!!)
                isUpdateSuccess.postValue(true)
            }
        }
    }
}