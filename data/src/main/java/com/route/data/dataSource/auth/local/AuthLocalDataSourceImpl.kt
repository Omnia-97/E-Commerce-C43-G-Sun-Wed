package com.route.data.dataSource.auth.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.route.data.dataSource.DataStoreKeys
import com.route.domain.model.Result
import com.route.domain.model.user.UserProfile
import com.route.domain.repository.AuthLocalDataSource
import com.route.domain.utils.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AuthLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AuthLocalDataSource {
    override suspend fun saveToken(params: String): Flow<Result<Unit>> {
        return flow {
            try {
                dataStore.updateData {
                    it.toMutablePreferences().also { preferences ->
                        preferences[DataStoreKeys.TOKEN] = params
                    }
                }
                Log.e("TAG", "saveToken: Success !")
                emit(Result.Success())
            } catch (e: Exception) {
                Log.e("TAG", "saveToken: Error : ${e.message} !")
                emit(Result.Error(Failure.CustomException(e.message)))
            }
        }
    }

    override suspend fun getToken(): Flow<Result<String>> {
        return dataStore.data.map { preferences ->
            try {
                val token = preferences[DataStoreKeys.TOKEN] ?: ""
                Result.Success(token)
            } catch (e: Exception) {
                Result.Error(Failure.CustomException(e.message))
            }
        }
    }

    override suspend fun saveUserProfile(
        name: String,
        email: String,
        phone: String
    ): Flow<Result<Unit>> {
        return flow {
            try {
                dataStore.updateData {
                    it.toMutablePreferences().also { prefs ->
                        prefs[DataStoreKeys.USER_NAME] = name
                        prefs[DataStoreKeys.USER_EMAIL] = email
                        prefs[DataStoreKeys.USER_PHONE] = phone
                    }
                }
                emit(Result.Success())
            } catch (e: Exception) {
                emit(Result.Error(Failure.CustomException(e.message)))
            }
        }
    }

    override suspend fun getUserProfile(): Flow<Result<UserProfile>> {
        return dataStore.data.map { preferences ->
            try {
                Result.Success(
                    UserProfile(
                        name = preferences[DataStoreKeys.USER_NAME] ?: "",
                        email = preferences[DataStoreKeys.USER_EMAIL] ?: "",
                        phone = preferences[DataStoreKeys.USER_PHONE] ?: ""
                    )
                )
            } catch (e: Exception) {
                Result.Error(Failure.CustomException(e.message))
            }
        }
    }

    override suspend fun clearAllUserData(): Flow<Result<Unit>> {
        return flow {
            try {
                dataStore.updateData {
                    it.toMutablePreferences().also { prefs ->
                        prefs.clear()
                    }
                }
                emit(Result.Success())
            } catch (e: Exception) {
                emit(Result.Error(Failure.CustomException(e.message)))
            }
        }
    }

}
