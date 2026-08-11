package com.route.data.repository.user

import com.route.domain.model.Result
import com.route.domain.model.user.UserProfile
import com.route.domain.repository.AuthRepository
import com.route.domain.repository.UserRemoteDataSource
import com.route.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val authRepository: AuthRepository
) : UserRepository {

    override suspend fun updateUserData(
        name: String,
        email: String,
        phone: String
    ): Flow<Result<UserProfile>> =
        remoteDataSource.updateUserData(name, email, phone).onEach { result ->
            if (result is Result.Success) {
                authRepository.saveUserProfile(
                    name = result.data?.name ?: name,
                    email = result.data?.email ?: email,
                    phone = result.data?.phone ?: phone
                ).collect {}
            }
        }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        rePassword: String
    ): Flow<Result<Unit>> =
        remoteDataSource.changePassword(currentPassword, newPassword, rePassword)
}