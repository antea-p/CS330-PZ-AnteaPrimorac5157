package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.AuthenticationService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.AccountViewModel
import javax.inject.Inject

sealed class AccountUiState {
    object Loading : AccountUiState()
    data class LoggedIn(val username: String) : AccountUiState()
    object LoggedOut : AccountUiState()
    data class Error(val message: String) : AccountUiState()
}

@HiltViewModel
class AccountViewModelImpl @Inject constructor(
    private val authService: AuthenticationService
) : ViewModel(), AccountViewModel {

    private val _uiState = MutableLiveData<AccountUiState>(AccountUiState.Loading)
    override val uiState: LiveData<AccountUiState> = _uiState

    init {
        checkAuthenticationStatus()
    }

    override fun checkAuthenticationStatus() {
        viewModelScope.launch {
            authService.checkAuthentication()
            authService.getUsername().collect { username ->
                _uiState.value = if (username != null) {
                    AccountUiState.LoggedIn(username)
                } else {
                    AccountUiState.LoggedOut
                }
            }
        }
    }


    override fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AccountUiState.Loading
            try {
                authService.login(username, password)
            } catch (e: Exception) {
                _uiState.value = AccountUiState.Error("Login failed: ${e.message}")
            }
        }
    }

    override fun logout() {
        viewModelScope.launch {
            authService.logout()
        }
    }

}