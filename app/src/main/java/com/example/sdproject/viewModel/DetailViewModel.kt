package com.example.sdproject.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sdproject.model.Employ
import com.example.sdproject.repository.EmployRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    val repository: EmployRepository
) : ViewModel() {

    var employ: Employ? = null
    var bitmap: String? = null
    val isUpdateAvatarSuccess: MutableLiveData<Boolean> = MutableLiveData()

    fun updateAvatar() {
        if (employ == null || bitmap == null)
            return
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.changeAvatar(employ?.id!!, bitmap!!)
            if (response.isSuccessful)
                isUpdateAvatarSuccess.postValue(true)
            else
                isUpdateAvatarSuccess.postValue(false)
        }
    }
}