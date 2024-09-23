package com.example.sdproject.model

import android.os.Parcelable
import com.example.sdproject.common.convertToTime
import kotlinx.parcelize.Parcelize

@Parcelize
data class Employ(
    val id: Int,
    var fullName: String?,
    var gender: Gender,
    var brithDay: String,
    var phone: String?,
    var numberOffice: NumOffice,
    var email: String?
) : Parcelable {
    var salary: Number = -1
    var avatar: String? = ""
    var profile: String? = ""
    var timeCreated: Long = -1
    var timeModify: Long = -1

    constructor(
        id: Int,
        fullName: String?,
        gender: Gender,
        brithDay: String,
        phone: String?,
        numberOffice: NumOffice,
        email: String?,
        salary: Number,
        avatar: String?,
        profile: String?,
        timeCreated: Long,
        timeModify: Long
    ) : this(id, fullName, gender, brithDay, phone, numberOffice, email) {
        this.salary = salary
        this.avatar = avatar
        this.profile = profile
        this.timeCreated = timeCreated
        this.timeModify = timeModify
    }

    fun convertToEmployRemote() : EmployRemote = EmployRemote(
        id = this.id,
        fullName = this.fullName,
        gender = this.gender.express,
        brithDay = this.brithDay,
        phone = this.phone,
        numberOffice = this.numberOffice.number,
        email = this.email,
        salary = this.salary,
        avatar = this.avatar,
        profile = this.profile,
        timeCreated = this.timeCreated.convertToTime(),
        timeModify = this.timeModify.convertToTime()
    )

    fun clone(target: Employ) {
        this.fullName = target.fullName
        this.gender = target.gender
        this.brithDay = target.brithDay
        this.phone = target.phone
        this.numberOffice = target.numberOffice
        this.email = target.email
        this.salary = target.salary
        this.avatar = target.avatar
        this.profile = target.profile
    }
}