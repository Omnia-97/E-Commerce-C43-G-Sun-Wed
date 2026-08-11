
package com.route.domain.usecases.user

import com.route.domain.repository.AuthRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun invoke() = repository.getUserProfile()
}