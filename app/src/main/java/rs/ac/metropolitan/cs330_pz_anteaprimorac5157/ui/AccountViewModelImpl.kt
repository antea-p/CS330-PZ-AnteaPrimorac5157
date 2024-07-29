package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.AuthenticationRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.Authentication
import javax.inject.Inject

sealed class AccountUiState {
    object Loading : AccountUiState()
    data class LoggedIn(val username: String) : AccountUiState()
    object LoggedOut : AccountUiState()
    data class Error(val message: String) : AccountUiState()
}

@HiltViewModel
class AccountViewModelImpl @Inject constructor(
    private val authRepository: AuthenticationRepository
) : ViewModel(), AccountViewModel {

    private val _uiState = MutableLiveData<AccountUiState>(AccountUiState.Loading)
    override val uiState: LiveData<AccountUiState> = _uiState

    init {
        checkAuthenticationStatus()
    }

    override fun checkAuthenticationStatus() {
        viewModelScope.launch {
            authRepository.find().collect { authentication ->
                if (authentication != null) {
                    val isValid = authRepository.checkTokenValidity(authentication.token)
                    if (isValid) {
                        _uiState.value = AccountUiState.LoggedIn(authentication.username)
                    } else {
                        _uiState.value = AccountUiState.LoggedOut
                    }
                } else {
                    _uiState.value = AccountUiState.LoggedOut
                }
            }
        }
    }

    override fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AccountUiState.Loading
            try {
                val response = authRepository.login(username, password)
                authRepository.save(Authentication(token = response.token, username = username))
                _uiState.value = AccountUiState.LoggedIn(username)
            } catch (e: Exception) {
                _uiState.value = AccountUiState.Error("Login failed: ${e.message}")
            }
        }
    }

    override fun logout() {
        viewModelScope.launch {
            authRepository.delete()
            _uiState.value = AccountUiState.LoggedOut
        }
    }


//    private suspend fun checkTokenValidity(token: String): Boolean {
//        return try {
//            val response = dreamDiaryApi.checkAuthentication("Bearer $token")
//            response.authenticated
//        } catch (e: Exception) {
//            false
//        }
//    }
}