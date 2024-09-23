package com.example.sdproject.repository

import com.example.sdproject.model.EmployRemote
import com.example.sdproject.model.ResponseRemote
import com.example.sdproject.model.StatusResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MyApiService {

    @GET("/api/cms/Employee/GetList")
    suspend fun getListEmployee(
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int
    ): Response<ResponseRemote>

    @FormUrlEncoded
    @POST("/api/cms/Employee/Create")
    suspend fun newEmployee(
        @Field("fullName") fullName: String,
        @Field("birthday") birthDay: String,
        @Field("gender") gender: Boolean,
        @Field("email") email: String,
        @Field("phoneNumber") phone: String,
        @Field("salary") salary: Double,
        @Field("department") numOffice: Int,
        @Field("biography") biography: String,
        @Field("image") avatar: String
    ): Response<ResponseRemote>

    @PUT("/api/cms/Employee/Update")
    suspend fun updateEmployee(
        @Body employ: EmployRemote
    ): Response<StatusResponse>

    @DELETE("/api/cms/Employee/Delete/{id}")
    suspend fun delete(
        @Path("id") id: Int
    ): Response<StatusResponse>

    @FormUrlEncoded
    @POST("/api/admin/Employee/ChangeAvatar")
    suspend fun changeAvatar(
        @Field("id") id: Int,
        @Field("image") image: String
    ): Response<StatusResponse>

}