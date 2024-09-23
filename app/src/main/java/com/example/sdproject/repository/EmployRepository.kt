package com.example.sdproject.repository

import com.example.sdproject.model.Employ
import com.example.sdproject.model.EmployRemote
import retrofit2.http.Path
import javax.inject.Inject

class EmployRepository @Inject constructor(
    private val apiService: MyApiService
) {
    suspend fun fetchEmploys(pageIndex: Int, pageSize: Int) =
        apiService.getListEmployee(pageIndex, pageSize)

    suspend fun newEmployee(
        fullName: String,
        birthDay: String,
        gender: Boolean,
        email: String,
        phone: String,
        salary: Double,
        numOffice: Int,
        biography: String,
        avatar: String
    ) = apiService.newEmployee(fullName, birthDay, gender, email, phone, salary, numOffice, biography, avatar)

    suspend fun updateEmployee(employ: EmployRemote) = apiService.updateEmployee(employ)

    suspend fun deleteEmployee(id:Int) = apiService.delete(id)

    suspend fun changeAvatar(id: Int, image: String) = apiService.changeAvatar(id, image)
}