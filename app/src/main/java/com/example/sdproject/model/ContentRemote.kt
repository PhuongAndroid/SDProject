package com.example.sdproject.model

import com.google.gson.annotations.SerializedName


class ContentRemote(
    @SerializedName("items") val arrEmployee: MutableList<EmployRemote>
)