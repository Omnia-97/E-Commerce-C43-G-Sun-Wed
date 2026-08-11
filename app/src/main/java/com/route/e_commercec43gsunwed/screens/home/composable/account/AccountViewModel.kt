package com.route.e_commercec43gsunwed.screens.home.composable.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.route.domain.model.Result
import com.route.domain.repository.AuthRepository
import com.route.domain.usecases.user.ChangePasswordUseCase
import com.route.domain.usecases.user.GetUserProfileUseCase
import com.route.domain.usecases.user.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val authRepository: AuthRepository
) : ViewModel(), AccountContract.ViewModel {

    private val _states = MutableStateFlow(AccountContract.States())
    override val states: StateFlow<AccountContract.States>
        get() = _states

    private val _events = MutableSharedFlow<AccountContract.Events>()
    override val events: SharedFlow<AccountContract.Events>
        get() = _events

    init {
        handleActions(AccountContract.Actions.LoadProfile)
    }

    override fun handleActions(action: AccountContract.Actions) {
        viewModelScope.launch {
            when (action) {
                AccountContract.Actions.Idle -> {}

                AccountContract.Actions.LoadProfile -> loadProfile()

                AccountContract.Actions.ClickedEditName ->
                    _states.value = _states.value.copy(
                        activeDialog = AccountContract.ActiveDialog.Name
                    )

                AccountContract.Actions.ClickedEditEmail ->
                    _states.value = _states.value.copy(
                        activeDialog = AccountContract.ActiveDialog.Email
                    )

                AccountContract.Actions.ClickedEditPhone ->
                    _states.value = _states.value.copy(
                        activeDialog = AccountContract.ActiveDialog.Phone
                    )

                AccountContract.Actions.ClickedEditPassword ->
                    _states.value = _states.value.copy(
                        activeDialog = AccountContract.ActiveDialog.Password
                    )

                AccountContract.Actions.DismissDialog ->
                    _states.value = _states.value.copy(
                        activeDialog = AccountContract.ActiveDialog.None
                    )

                is AccountContract.Actions.ConfirmUpdateProfile ->
                    updateProfile(action.name, action.email, action.phone)

                is AccountContract.Actions.ConfirmChangePassword ->
                    changePassword(
                        action.currentPassword,
                        action.newPassword,
                        action.rePassword
                    )
                is AccountContract.Actions.Logout -> logout()
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            getUserProfileUseCase.invoke().collect {
                _states.value = _states.value.copy(profile = it)
            }
        }
    }

    private fun updateProfile(name: String, email: String, phone: String) {
        if (name.isBlank() || email.isBlank()) {
            viewModelScope.launch {
                _events.emit(AccountContract.Events.ShowMessage("Name and email cannot be empty"))
            }
            return
        }
        viewModelScope.launch {
            _states.value = _states.value.copy(isUpdating = true)
            updateUserUseCase.invoke(name, email, phone).collect { result ->
                _states.value = _states.value.copy(isUpdating = false)
                when (result) {
                    is Result.Success -> {
                        loadProfile()
                        _states.value = _states.value.copy(
                            activeDialog = AccountContract.ActiveDialog.None
                        )
                        _events.emit(AccountContract.Events.ShowMessage("Profile updated successfully"))
                    }
                    is Result.Error -> {
                        _events.emit(AccountContract.Events.ShowMessage(result.failure.message))
                    }
                }
            }
        }
    }

    private fun changePassword(
        currentPassword: String,
        newPassword: String,
        rePassword: String
    ) {
        if (currentPassword.isBlank() || newPassword.isBlank() || rePassword.isBlank()) {
            viewModelScope.launch {
                _events.emit(AccountContract.Events.ShowMessage("Please fill all password fields"))
            }
            return
        }
        if (newPassword != rePassword) {
            viewModelScope.launch {
                _events.emit(AccountContract.Events.ShowMessage("Passwords do not match"))
            }
            return
        }
        viewModelScope.launch {
            _states.value = _states.value.copy(isUpdating = true)
            changePasswordUseCase.invoke(currentPassword, newPassword, rePassword)
                .collect { result ->
                    _states.value = _states.value.copy(isUpdating = false)
                    when (result) {
                        is Result.Success -> {
                            _states.value = _states.value.copy(
                                activeDialog = AccountContract.ActiveDialog.None
                            )
                            _events.emit(
                                AccountContract.Events.ShowMessage("Password changed successfully")
                            )
                        }
                        is Result.Error -> {
                            _events.emit(
                                AccountContract.Events.ShowMessage(result.failure.message)
                            )
                        }
                    }
                }
        }
    }
    private fun logout() {
        viewModelScope.launch {
            authRepository.logout().collect {
                _events.emit(AccountContract.Events.NavigateToLogin)
            }
        }
    }
}