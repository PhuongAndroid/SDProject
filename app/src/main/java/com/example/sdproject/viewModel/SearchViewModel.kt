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
class SearchViewModel @Inject constructor(
    val repository: EmployRepository
): ViewModel() {

    lateinit var allData: MutableList<Employ?>
    val lstSearch: MutableStateFlow<MutableList<Employ?>> = MutableStateFlow(mutableListOf())
    private var searchJob: Job? = null
    val isDeleteSuccess: MutableLiveData<Int> = MutableLiveData()

    private val search: (String) -> List<Employ> = { key ->
        val data = allData.filter {
            it?.fullName?.contains(key) == true
        }.toMutableList()
        data.filterNotNull()
    }
    fun debounceFullName(keySearch: String){
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            lstSearch.value = search.invoke(keySearch).toMutableList()
        }
    }

    fun delete(item: Employ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.deleteEmployee(item.id)
            if (response.isSuccessful) {
                val pos = lstSearch.value.indexOf(item)
                allData.remove(item)
                lstSearch.value.removeAt(pos)
                isDeleteSuccess.postValue(pos)
            }
        }
    }
}