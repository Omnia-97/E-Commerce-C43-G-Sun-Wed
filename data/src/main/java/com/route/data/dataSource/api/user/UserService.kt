package com.route.data.dataSource.api.user

import com.route.data.model.user.UserMutationResponseDM
import com.route.data.model.user.request.ChangePasswordRequestDM
import com.route.data.model.user.request.UpdateUserRequestDM
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

interface UserService {
    @PUT("users/updateMe/")
    suspend fun updateUserData(
        @Body request: UpdateUserRequestDM
    ): Response<UserMutationResponseDM>

    @PUT("users/changeMyPassword")
    suspend fun changeMyPassword(
        @Body request: ChangePasswordRequestDM
    ): Response<UserMutationResponseDM>
}