package com.route.e_commercec43gsunwed.screens.home.composable.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.route.domain.model.Result
import com.route.e_commercec43gsunwed.LocalNavController
import com.route.e_commercec43gsunwed.R
import com.route.e_commercec43gsunwed.destinations.AppRoutes
import com.route.e_commercec43gsunwed.utils.ErrorDialog

@Composable
fun AccountTab(modifier: Modifier = Modifier) {
    val viewModel: AccountViewModel = hiltViewModel()
    val state = viewModel.states.collectAsStateWithLifecycle()
    val colorScheme = MaterialTheme.colorScheme
    var toastMessage by remember { mutableStateOf<String?>(null) }
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                is AccountContract.Events.ShowMessage -> toastMessage = it.message
                AccountContract.Events.NavigateToLogin -> {
                    navController.navigate(AppRoutes.LoginDestination) {
                        popUpTo(AppRoutes.HomeDestination) {
                            inclusive = true
                        }
                    }
                }

                AccountContract.Events.Idle -> {}
            }
        }
    }

    val profile = (state.value.profile as? Result.Success)?.data

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.onSecondary)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(R.drawable.e_commerce_route_logo_blue),
            contentDescription = stringResource(R.string.app_logo)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome, ${profile?.name ?: ""}",
            fontSize = 18.sp,
            fontWeight = FontWeight.W700,
            color = colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = profile?.email ?: "",
            fontSize = 14.sp,
            fontWeight = FontWeight.W400,
            color = colorScheme.onBackground.copy(alpha = 0.6F)
        )

        Spacer(modifier = Modifier.height(24.dp))

        ProfileField(
            label = "Your full name",
            value = profile?.name ?: "",
            onEditClick = {
                viewModel.handleActions(AccountContract.Actions.ClickedEditName)
            }
        )
        ProfileField(
            label = "Your E-mail",
            value = profile?.email ?: "",
            onEditClick = {
                viewModel.handleActions(AccountContract.Actions.ClickedEditEmail)
            }
        )
        ProfileField(
            label = "Your password",
            value = "••••••••••••",
            onEditClick = {
                viewModel.handleActions(AccountContract.Actions.ClickedEditPassword)
            }
        )
        ProfileField(
            label = "Your mobile number",
            value = profile?.phone ?: "",
            onEditClick = {
                viewModel.handleActions(AccountContract.Actions.ClickedEditPhone)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, colorScheme.secondary, RoundedCornerShape(12.dp))
                .clickable {
                    viewModel.handleActions(AccountContract.Actions.Logout)
                }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Log out",
                fontSize = 15.sp,
                fontWeight = FontWeight.W600,
                color = colorScheme.secondary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    val activeDialog = state.value.activeDialog
    val isUpdating = state.value.isUpdating

    when (activeDialog) {
        AccountContract.ActiveDialog.Name -> {
            SingleFieldDialog(
                title = "Update Name",
                label = "Full name",
                initialValue = profile?.name ?: "",
                isUpdating = isUpdating,
                onConfirm = { newName ->
                    viewModel.handleActions(
                        AccountContract.Actions.ConfirmUpdateProfile(
                            name = newName,
                            email = profile?.email ?: "",
                            phone = profile?.phone ?: ""
                        )
                    )
                },
                onDismiss = {
                    viewModel.handleActions(AccountContract.Actions.DismissDialog)
                }
            )
        }

        AccountContract.ActiveDialog.Email -> {
            SingleFieldDialog(
                title = "Update Email",
                label = "Email address",
                initialValue = profile?.email ?: "",
                keyboardType = KeyboardType.Email,
                isUpdating = isUpdating,
                onConfirm = { newEmail ->
                    viewModel.handleActions(
                        AccountContract.Actions.ConfirmUpdateProfile(
                            name = profile?.name ?: "",
                            email = newEmail,
                            phone = profile?.phone ?: ""
                        )
                    )
                },
                onDismiss = {
                    viewModel.handleActions(AccountContract.Actions.DismissDialog)
                }
            )
        }

        AccountContract.ActiveDialog.Phone -> {
            SingleFieldDialog(
                title = "Update Mobile Number",
                label = "Mobile number",
                initialValue = profile?.phone ?: "",
                keyboardType = KeyboardType.Phone,
                isUpdating = isUpdating,
                onConfirm = { newPhone ->
                    viewModel.handleActions(
                        AccountContract.Actions.ConfirmUpdateProfile(
                            name = profile?.name ?: "",
                            email = profile?.email ?: "",
                            phone = newPhone
                        )
                    )
                },
                onDismiss = {
                    viewModel.handleActions(AccountContract.Actions.DismissDialog)
                }
            )
        }

        AccountContract.ActiveDialog.Password -> {
            ChangePasswordDialog(
                isUpdating = isUpdating,
                onConfirm = { current, new, re ->
                    viewModel.handleActions(
                        AccountContract.Actions.ConfirmChangePassword(current, new, re)
                    )
                },
                onDismiss = {
                    viewModel.handleActions(AccountContract.Actions.DismissDialog)
                }
            )
        }

        AccountContract.ActiveDialog.None -> {}
    }

    if (toastMessage != null) {
        ErrorDialog(errorState = toastMessage) {
            toastMessage = null
        }
    }
}

@Composable
fun ProfileField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onEditClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(modifier = modifier.padding(bottom = 16.dp)) {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.W500,
            color = colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    colorScheme.onBackground.copy(alpha = 0.2F),
                    RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value.ifEmpty { "—" },
                modifier = Modifier.weight(1F),
                fontSize = 15.sp,
                fontWeight = FontWeight.W400,
                color = colorScheme.onBackground.copy(alpha = if (value.isEmpty()) 0.4F else 1F)
            )
            Image(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = "Edit $label",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onEditClick() }
            )
        }
    }
}

@Composable
fun SingleFieldDialog(
    title: String,
    label: String,
    initialValue: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isUpdating: Boolean,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    var value by remember { mutableStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = { if (!isUpdating) onDismiss() },
        containerColor = colorScheme.onSecondary,
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.W600,
                color = colorScheme.onBackground
            )
        },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                label = { Text(label) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorScheme.secondary,
                    focusedLabelColor = colorScheme.secondary,
                    cursorColor = colorScheme.secondary
                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            if (isUpdating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = colorScheme.secondary
                )
            } else {
                TextButton(onClick = { onConfirm(value.trim()) }) {
                    Text(
                        "Save",
                        color = colorScheme.secondary,
                        fontWeight = FontWeight.W600
                    )
                }
            }
        },
        dismissButton = {
            if (!isUpdating) {
                TextButton(onClick = onDismiss) {
                    Text(
                        "Cancel",
                        color = colorScheme.onBackground.copy(alpha = 0.6F)
                    )
                }
            }
        }
    )
}

@Composable
fun ChangePasswordDialog(
    isUpdating: Boolean,
    onConfirm: (currentPassword: String, newPassword: String, rePassword: String) -> Unit,
    onDismiss: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var rePassword by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { if (!isUpdating) onDismiss() },
        containerColor = colorScheme.onSecondary,
        title = {
            Text(
                "Change Password",
                fontSize = 18.sp,
                fontWeight = FontWeight.W600,
                color = colorScheme.onBackground
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PasswordField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = "Current password"
                )
                PasswordField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "New password"
                )
                PasswordField(
                    value = rePassword,
                    onValueChange = { rePassword = it },
                    label = "Confirm new password"
                )
            }
        },
        confirmButton = {
            if (isUpdating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = colorScheme.secondary
                )
            } else {
                TextButton(
                    onClick = { onConfirm(currentPassword, newPassword, rePassword) }
                ) {
                    Text(
                        "Save",
                        color = colorScheme.secondary,
                        fontWeight = FontWeight.W600
                    )
                }
            }
        },
        dismissButton = {
            if (!isUpdating) {
                TextButton(onClick = onDismiss) {
                    Text(
                        "Cancel",
                        color = colorScheme.onBackground.copy(alpha = 0.6F)
                    )
                }
            }
        }
    )
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    val colorScheme = MaterialTheme.colorScheme
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None
        else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            Text(
                text = if (visible) "Hide" else "Show",
                modifier = Modifier
                    .clickable { visible = !visible }
                    .padding(end = 8.dp),
                fontSize = 12.sp,
                color = colorScheme.secondary
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorScheme.secondary,
            focusedLabelColor = colorScheme.secondary,
            cursorColor = colorScheme.secondary
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
