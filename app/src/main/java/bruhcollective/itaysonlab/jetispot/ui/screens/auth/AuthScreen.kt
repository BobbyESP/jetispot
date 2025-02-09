package bruhcollective.itaysonlab.jetispot.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetispot.R
import bruhcollective.itaysonlab.jetispot.proto.AudioQuality
import bruhcollective.itaysonlab.jetispot.ui.ext.compositeSurfaceElevation
import bruhcollective.itaysonlab.jetispot.ui.navigation.LocalNavigationController
import bruhcollective.itaysonlab.jetispot.ui.screens.Dialog
import bruhcollective.itaysonlab.jetispot.ui.screens.Screen

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: AuthScreenViewModel = hiltViewModel()
) {
    val navController = LocalNavigationController.current

    val snackbarHostState = remember { SnackbarHostState() }
    val (snackbarContent, setSnackbarContent) = remember { mutableStateOf("", neverEqualPolicy()) }

    LaunchedEffect(snackbarContent) {
        if (snackbarContent.isNotEmpty()) {
            snackbarHostState.showSnackbar(snackbarContent)
        }
    }

    val autofill = LocalAutofill.current
    val focusManager = LocalFocusManager.current

    val (username, setUsername) = rememberSaveable { mutableStateOf("") }
    val (password, setPassword) = rememberSaveable { mutableStateOf("") }
    val (usernameFocusRequester, passwordFocusRequester) = remember { FocusRequester.createRefs() }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(vertical = 32.dp, horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = stringResource(R.string.auth_welcome),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = stringResource(R.string.auth_welcome_text),
                fontSize = 14.sp
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .offset(y = 80.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                .clip(MaterialTheme.shapes.medium.copy(bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp)))
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.compositeSurfaceElevation(4.dp))
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            Autofill(
                autofillTypes = listOf(AutofillType.EmailAddress, AutofillType.Username),
                onFill = setUsername
            ) { autofillNode ->
                OutlinedTextField(
                    value = username,
                    onValueChange = setUsername,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions {
                        passwordFocusRequester.requestFocus()
                    },
                    singleLine = true,
                    label = { Text(stringResource(R.string.username)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusTarget()
                        .focusRequester(usernameFocusRequester)
                        .onFocusChanged {
                            autofill?.apply {
                                if (it.isFocused) {
                                    requestAutofillForNode(autofillNode)
                                } else {
                                    cancelAutofillForNode(autofillNode)
                                }
                            }
                        }
                        .focusProperties { next = passwordFocusRequester },
                )
            }

            Autofill(
                autofillTypes = listOf(AutofillType.Password),
                onFill = setPassword
            ) { autofillNode ->
                OutlinedTextField(
                    value = password,
                    onValueChange = setPassword,
                    label = { Text(stringResource(R.string.password)) },
                    singleLine = true,
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusTarget()
                        .focusRequester(passwordFocusRequester)
                        .onFocusChanged {
                            autofill?.apply {
                                if (it.isFocused) {
                                    requestAutofillForNode(autofillNode)
                                } else {
                                    cancelAutofillForNode(autofillNode)
                                }
                            }
                        }
                        .focusProperties { previous = usernameFocusRequester },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions {
                        focusManager.clearFocus()
                        viewModel.auth(
                            username = username,
                            password = password,
                            onSuccess = { viewModel.onLoginSuccess(navController) },
                            onFailure = setSnackbarContent,
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible }
                        ) {
                            if (passwordVisible) {
                                Icon(
                                    Icons.Rounded.Visibility,
                                    stringResource(R.string.hide_password)
                                )
                            } else {
                                Icon(
                                    Icons.Rounded.VisibilityOff,
                                    stringResource(R.string.show_password)
                                )
                            }
                        }
                    }
                )
            }

            Box(
                Modifier
                    .fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = { navController.navigate(Dialog.AuthDisclaimer) },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text(stringResource(R.string.auth_disclaimer))
                }

                Button(
                    onClick = {
                        viewModel.auth(
                            username = username,
                            password = password,
                            onSuccess = { navController.navigateAndClearStack(Screen.Feed) },
                            onFailure = setSnackbarContent,
                        )
                    },
                    enabled = !viewModel.isAuthInProgress.value,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text(stringResource(R.string.auth_next))
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomStart),
            snackbar = { data ->
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.compositeSurfaceElevation(12.dp),
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    snackbarData = data
                )
            }
        )
    }
}

// TODO migrate from Composable wrapper to modifier
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Autofill(
    autofillTypes: List<AutofillType>,
    onFill: ((String) -> Unit),
    content: @Composable (AutofillNode) -> Unit
) {
    val autofillNode = AutofillNode(onFill = onFill, autofillTypes = autofillTypes)

    val autofillTree = LocalAutofillTree.current
    autofillTree += autofillNode

    Box(
        modifier = Modifier.onGloballyPositioned {
            autofillNode.boundingBox = it.boundsInWindow()
        }
    ) {
        content(autofillNode)
    }
}
