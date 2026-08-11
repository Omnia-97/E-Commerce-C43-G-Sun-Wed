package com.route.domain.usecases.user

import com.route.domain.repository.UserRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend fun invoke(currentPassword: String, newPassword: String, rePassword: String) =
        repository.changePassword(currentPassword, newPassword, rePassword)
}