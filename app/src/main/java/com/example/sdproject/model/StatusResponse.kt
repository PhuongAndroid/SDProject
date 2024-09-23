package com.example.sdproject.model

import com.google.gson.annotations.SerializedName

data class StatusResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("description") val status: String
)