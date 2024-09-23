package com.example.sdproject.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sdproject.model.Gender
import com.example.sdproject.model.NumOffice
import com.example.sdproject.repository.EmployRepository
import com.example.sdproject.repository.MyApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class NewViewModel @Inject constructor(
    val repository: EmployRepository
) : ViewModel() {

    var gender: MutableStateFlow<Gender> = MutableStateFlow(Gender.NAM)
    var numOffice: MutableStateFlow<NumOffice> = MutableStateFlow(NumOffice.BanGiamDoc)
    var birthDay: Long = -1
    var bitmap: String = ""
    val isNewSuccess: MutableLiveData<Boolean> = MutableLiveData()

    fun newEmploy(
        fullName: String, birthDay: String, gender: Boolean, email: String,
        phone: String, salary: Double, numOffice: Int, profile: String, avatar: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.newEmployee(
                fullName,
                birthDay,
                gender,
                email,
                phone,
                salary,
                numOffice,
                profile,
                avatar
            )
            if (response.isSuccessful)
                isNewSuccess.postValue(true)
            else
                isNewSuccess.postValue(false)
        }
    }

    fun canSaveBirthDay(birthDay: String): Boolean {
        val birthDayConvert = if (birthDay.startsWith("0"))
            birthDay.substring(1)
        else
            birthDay
        val unitIndex = birthDayConvert.length - 1
        val donVi = birthDayConvert[unitIndex].toString().toInt()
        val hangChuc = birthDayConvert[unitIndex - 1].toString().toInt()
        val hangTram = birthDayConvert[unitIndex - 2].toString().toInt()
        val hangNghin = birthDayConvert[unitIndex - 3].toString().toInt()
        val minus1 = abs(donVi - hangChuc)
        val minus2 = abs(hangTram - hangNghin)
        return minus2 % minus1 == 0
    }
}