package com.example.sdproject.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sdproject.model.Employ
import com.example.sdproject.repository.EmployRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: EmployRepository
) : ViewModel() {

    val arrEmploy: MutableStateFlow<MutableList<Employ>> = MutableStateFlow(mutableListOf())
    var allData: MutableList<Employ?> = mutableListOf()
    var pageIndex: Int = 1
    val pageSize = 10
    var isLoading = false

    val isDeleteSuccess: MutableLiveData<Int> = MutableLiveData()

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            val response = repository.fetchEmploys(pageIndex, pageSize)
            if (response.isSuccessful) {
                isLoading = false
                pageIndex += 1
                val responseData = response.body()?.contentRemote?.arrEmployee ?: mutableListOf()
                if (responseData.isEmpty())
                    return@launch
                val data =
                    responseData.map { it.convertToEmploy() }.sortedByDescending { it.timeModify }
                allData.addAll(data)
                arrEmploy.value = data.toMutableList()
            }
        }
    }

    fun delete(item: Employ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.deleteEmployee(item.id)
            if (response.isSuccessful) {
                val pos = allData.indexOf(item)
                allData.remove(item)
                isDeleteSuccess.postValue(pos)
            }
        }
    }
}