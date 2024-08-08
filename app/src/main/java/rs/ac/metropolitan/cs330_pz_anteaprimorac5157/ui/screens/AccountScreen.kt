package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.impl.AccountUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.AccountViewModel
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.impl.AccountViewModelImpl

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel<AccountViewModelImpl>()
) {
    val uiState by viewModel.uiState.observeAsState(AccountUiState.Loading)

    Box(modifier = Modifier.testTag("account_screen")) {
        when (val state = uiState) {
            is AccountUiState.Loading -> LoadingScreen()
            is AccountUiState.LoggedOut -> LoginScreen(onLogin = viewModel::login)
            is AccountUiState.LoggedIn -> LoggedInScreen(
                username = state.username,
                onLogout = viewModel::logout
            )

            is AccountUiState.Error -> ErrorScreen(message = state.message)
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("loading_screen"),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun LoginScreen(onLogin: (String, String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("login_screen"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("username_input")
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("password_input")
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onLogin(username, password) },
            enabled = (username.isNotBlank() && password.isNotBlank()),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_button")
        ) {
            Text("Login")
        }
    }
}

@Composable
fun LoggedInScreen(username: String, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("logged_in_screen"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Welcome, $username!",
            Modifier.testTag("welcome_username")
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("logout_button")
        ) {
            Text("Logout")
        }
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("error_screen"),
        contentAlignment = Alignment.Center
    ) {
        Text(message, color = MaterialTheme.colorScheme.error)
    }
}

