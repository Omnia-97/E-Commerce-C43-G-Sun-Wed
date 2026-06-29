package com.route.e_commercec43gsunwed.screens.auth.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.route.domain.model.Result
import com.route.e_commercec43gsunwed.R
import com.route.e_commercec43gsunwed.utils.AuthButton
import com.route.e_commercec43gsunwed.utils.AuthTextField
import com.route.e_commercec43gsunwed.utils.ErrorDialog

@Composable
fun RegistrationScreen(modifier: Modifier = Modifier) {
    val viewModel: RegistrationViewModel = hiltViewModel()
    val colorScheme = MaterialTheme.colorScheme
    val state = viewModel.registerState.collectAsStateWithLifecycle()
    val nameErrorState = viewModel.nameErrorState.collectAsStateWithLifecycle()
    val phoneErrorState = viewModel.phoneErrorState.collectAsStateWithLifecycle()
    val emailErrorState = viewModel.emailErrorState.collectAsStateWithLifecycle()
    val passwordErrorState = viewModel.passwordErrorState.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    var nameError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    nameError = when (nameErrorState.value) {
        RegistrationValidator.Empty -> stringResource(R.string.name_required)
        RegistrationValidator.Idle -> ""
        RegistrationValidator.Invalid -> stringResource(R.string.invalid_name)
        RegistrationValidator.Short -> stringResource(R.string.short_name)
    }


    phoneError = when (phoneErrorState.value) {
        RegistrationValidator.Empty -> stringResource(R.string.phone_required)
        RegistrationValidator.Idle -> ""

        RegistrationValidator.Invalid -> stringResource(R.string.invalid_phone)

        RegistrationValidator.Short -> stringResource(R.string.short_phone)
    }


    emailError = when (emailErrorState.value) {
        RegistrationValidator.Empty -> stringResource(R.string.e_mail_address_required)
        RegistrationValidator.Idle -> ""

        RegistrationValidator.Invalid -> stringResource(R.string.e_mail_address_invalid)

        RegistrationValidator.Short -> ""
    }

    passwordError = when (passwordErrorState.value) {
        RegistrationValidator.Empty -> stringResource(R.string.password_required)
        RegistrationValidator.Idle -> ""
        RegistrationValidator.Invalid -> ""
        RegistrationValidator.Short -> stringResource(R.string.short_password)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(colorScheme.secondary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.e_commerce_logo_route),
            contentDescription = stringResource(R.string.app_logo),
            modifier = Modifier
                .padding(top = 36.dp)
                .fillMaxHeight(0.13F),
            contentScale = ContentScale.FillHeight,

            )
        AuthTextField(
            modifier = Modifier.padding(top = 40.dp),
            onTextChanged = {
                viewModel.updateName(it)
            },
            label = stringResource(R.string.full_name),
            hint = stringResource(R.string.enter_your_full_name),
            error = nameError,

            )
        AuthTextField(
            modifier = Modifier.padding(top = 32.dp),
            onTextChanged = {
                viewModel.updatePhoneNumber(it)
            },
            label = stringResource(R.string.mobile_number),
            hint = stringResource(R.string.enter_your_mobile_no),
            error = phoneError,

            )
        AuthTextField(
            modifier = Modifier.padding(top = 32.dp),
            onTextChanged = {
                viewModel.updateEmailAddress(it)
            },
            label = stringResource(R.string.e_mail_address),
            hint = stringResource(R.string.enter_your_email_address),
            error = emailError,

            )
        AuthTextField(
            modifier = Modifier.padding(top = 32.dp),
            onTextChanged = {
                viewModel.updatePassword(it)
            },
            label = stringResource(R.string.password),
            hint = stringResource(R.string.enter_your_password),
            error = passwordError,

            )
        AuthButton(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth(0.9F),
            text = stringResource(R.string.sign_up),
            isLoading = isLoading.value
        ) { viewModel.register() }
    }
    val registrationState = state.value
    when (registrationState) {
        is Result.Error -> {
            ErrorDialog(errorState = registrationState.failure.message) {
                viewModel.resetState()
            }

        }

        is Result.Success -> {

        }

        null -> {}
    }
}
