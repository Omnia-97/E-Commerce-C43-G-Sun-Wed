package com.route.domain.repository

import com.route.domain.model.Result
import com.route.domain.model.user.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun updateUserData(
        name: String,
        email: String,
        phone: String
    ): Flow<Result<UserProfile>>

    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        rePassword: String
    ): Flow<Result<Unit>>
}

interface UserRemoteDataSource {
    suspend fun updateUserData(
        name: String,
        email: String,
        phone: String
    ): Flow<Result<UserProfile>>

    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        rePassword: String
    ): Flow<Result<Unit>>

}