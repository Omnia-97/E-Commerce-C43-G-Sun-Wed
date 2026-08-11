package com.route.data.model.user

import com.route.data.dataSource.api.user.UserService
import com.route.data.dataSource.utils.safeApiCall
import com.route.data.model.user.request.ChangePasswordRequestDM
import com.route.data.model.user.request.UpdateUserRequestDM
import com.route.domain.model.Result
import com.route.domain.model.user.UserProfile
import com.route.domain.repository.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val service: UserService
) : UserRemoteDataSource {

    override suspend fun updateUserData(
        name: String,
        email: String,
        phone: String
    ): Flow<Result<UserProfile>> =
        safeApiCall(
            apiCall = {
                service.updateUserData(
                    UpdateUserRequestDM(
                        name = name,
                        email = email,
                        phone = phone
                    )
                )
            },
            mapper = { response ->
                UserProfile(
                    name = response.user?.name ?: name,
                    email = response.user?.email ?: email,
                    phone = phone
                )
            }
        )

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        rePassword: String
    ): Flow<Result<Unit>> =
        safeApiCall(
            apiCall = {
                service.changeMyPassword(
                    ChangePasswordRequestDM(
                        currentPassword = currentPassword,
                        password = newPassword,
                        rePassword = rePassword
                    )
                )
            },
            mapper = { Unit }
        )
}