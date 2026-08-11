package com.route.data.model.user

import com.google.gson.annotations.SerializedName

data class UserMutationResponseDM(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("user")
    val user: UserDM? = null,

    @field:SerializedName("token")
    val token: String? = null
)

data class UserDM(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("role")
    val role: String? = null
)