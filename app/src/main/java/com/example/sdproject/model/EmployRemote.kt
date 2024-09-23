package com.example.sdproject.model

import com.example.sdproject.common.convertToTime
import com.google.gson.annotations.SerializedName

data class EmployRemote(
    @SerializedName("id") val id: Int,
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("gender") val gender: Boolean,
    @SerializedName("birthday") val brithDay: String,
    @SerializedName("phoneNumber") val phone: String?,
    @SerializedName("department") val numberOffice: Int,
    @SerializedName("email") val email: String?,
    @SerializedName("salary") var salary: Number,
    @SerializedName("image") var avatar: String?,
    @SerializedName("biography") var profile: String?,
    @SerializedName("created_date") var timeCreated: String,
    @SerializedName("modified_date") var timeModify: String
) {
    fun convertToEmploy() = Employ(
        id = id,
        fullName = fullName,
        gender = Gender.findGenderByValue(gender),
        brithDay = brithDay,
        phone = phone,
        numberOffice =  NumOffice.findOfficeByID(numberOffice),
        email = email,
        salary = salary,
        avatar = avatar,
        profile = profile,
        timeCreated = timeCreated.convertToTime(),
        timeModify = timeModify.convertToTime()
    )
}