package com.route.domain.repository

import com.route.domain.model.Result
import com.route.domain.model.auth.AuthResponse
import com.route.domain.model.auth.request.LoginRequestParams
import com.route.domain.model.auth.request.RegistrationRequestParams
import com.route.domain.model.user.UserProfile
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(params: LoginRequestParams): Flow<Result<AuthResponse>>
    suspend fun register(params: RegistrationRequestParams): Flow<Result<AuthResponse>>
    suspend fun saveToken(params: String): Flow<Result<Unit>>
    suspend fun getToken(): Flow<Result<String>>
    suspend fun saveUserProfile(name: String, email: String, phone: String): Flow<Result<Unit>>
    suspend fun getUserProfile(): Flow<Result<UserProfile>>
    suspend fun logout(): Flow<Result<Unit>>
}

interface AuthRemoteDataSource {
    suspend fun login(params: LoginRequestParams): Flow<Result<AuthResponse>>
    suspend fun register(params: RegistrationRequestParams): Flow<Result<AuthResponse>>
}

interface AuthLocalDataSource {
    suspend fun saveToken(params: String): Flow<Result<Unit>>
    suspend fun getToken(): Flow<Result<String>>
    suspend fun saveUserProfile(name: String, email: String, phone: String): Flow<Result<Unit>>
    suspend fun getUserProfile(): Flow<Result<UserProfile>>
    suspend fun clearAllUserData(): Flow<Result<Unit>>
}
