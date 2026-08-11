package com.route.domain.usecases.user

import com.route.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend fun invoke(name: String, email: String, phone: String) =
        repository.updateUserData(name, email, phone)
}