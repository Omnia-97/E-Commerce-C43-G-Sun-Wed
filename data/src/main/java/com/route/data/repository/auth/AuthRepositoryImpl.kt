package com.route.data.repository.auth

import com.route.domain.model.Result
import com.route.domain.model.auth.AuthResponse
import com.route.domain.model.auth.request.LoginRequestParams
import com.route.domain.model.auth.request.RegistrationRequestParams
import com.route.domain.model.user.UserProfile
import com.route.domain.repository.AuthLocalDataSource
import com.route.domain.repository.AuthRemoteDataSource
import com.route.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource,
) : AuthRepository {
    override suspend fun login(params: LoginRequestParams): Flow<Result<AuthResponse>> {
        val result = remoteDataSource.login(params)
        result.collect {
            if (it is Result.Success) {
                saveToken(it.data?.token ?: "").collect {}
                saveUserProfile(
                    name = it.data?.authUser?.name ?: "",
                    email = it.data?.authUser?.email ?: "",
                    phone = ""
                ).collect {}
            }
        }
        return result
    }

    override suspend fun register(params: RegistrationRequestParams): Flow<Result<AuthResponse>> {
        val result = remoteDataSource.register(params)
        result.collect {
            if (it is Result.Success) {
                saveToken(it.data?.token ?: "").collect {}
                saveUserProfile(
                    name = it.data?.authUser?.name ?: "",
                    email = it.data?.authUser?.email ?: "",
                    phone = params.phone ?: ""
                ).collect {}
            }
        }
        return result
    }

    override suspend fun saveToken(params: String): Flow<Result<Unit>> {
        return localDataSource.saveToken(params)
    }

    override suspend fun getToken(): Flow<Result<String>> {
        return localDataSource.getToken()
    }

    override suspend fun saveUserProfile(
        name: String,
        email: String,
        phone: String
    ): Flow<Result<Unit>> = localDataSource.saveUserProfile(name, email, phone)

    override suspend fun getUserProfile(): Flow<Result<UserProfile>> =
        localDataSource.getUserProfile()
    override suspend fun logout(): Flow<Result<Unit>> =
        localDataSource.clearAllUserData()

}