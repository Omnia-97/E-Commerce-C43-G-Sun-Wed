package com.route.data.model.user.request

import com.google.gson.annotations.SerializedName

data class UpdateUserRequestDM(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("phone")
    val phone: String? = null
)

data class ChangePasswordRequestDM(
    @field:SerializedName("currentPassword")
    val currentPassword: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("rePassword")
    val rePassword: String? = null
)