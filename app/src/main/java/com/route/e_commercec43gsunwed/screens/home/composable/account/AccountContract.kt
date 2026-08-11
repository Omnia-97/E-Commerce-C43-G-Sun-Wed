package com.route.e_commercec43gsunwed.screens.home.composable.account

import com.route.domain.model.user.UserProfile
import com.route.domain.model.Result
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AccountContract {
    interface ViewModel {
        fun handleActions(action: Actions)
        val states: StateFlow<States>
        val events: SharedFlow<Events>
    }

    sealed interface Actions {
        data object Idle : Actions
        data object LoadProfile : Actions

        data object ClickedEditName : Actions
        data object ClickedEditEmail : Actions
        data object ClickedEditPhone : Actions
        data object ClickedEditPassword : Actions
        data object DismissDialog : Actions

        data class ConfirmUpdateProfile(
            val name: String,
            val email: String,
            val phone: String
        ) : Actions
        data class ConfirmChangePassword(
            val currentPassword: String,
            val newPassword: String,
            val rePassword: String
        ) : Actions
        data object Logout : Actions
    }

    sealed interface Events {
        data object Idle : Events
        data class ShowMessage(val message: String?) : Events
        data object NavigateToLogin : Events
    }

    data class States(
        val profile: Result<UserProfile>? = null,
        val activeDialog: ActiveDialog = ActiveDialog.None,
        val isUpdating: Boolean = false
    )

    sealed interface ActiveDialog {
        data object None : ActiveDialog
        data object Name : ActiveDialog
        data object Email : ActiveDialog
        data object Phone : ActiveDialog
        data object Password : ActiveDialog
    }
}