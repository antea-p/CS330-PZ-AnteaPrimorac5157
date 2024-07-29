package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.AccountUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.AccountViewModel

class FakeAccountViewModel: AccountViewModel {
    private val _uiState = MutableLiveData<AccountUiState>(AccountUiState.Loading)
    override val uiState: LiveData<AccountUiState> = _uiState

    val methodCalls = mutableListOf<String>()

    init {
        checkAuthenticationStatus()
    }

    override fun checkAuthenticationStatus() {
        methodCalls.add("checkAuthenticationStatus")
        _uiState.postValue(AccountUiState.LoggedOut)
    }

    override fun login(username: String, password: String) {
        methodCalls.add("login")
        _uiState.postValue(AccountUiState.LoggedIn(username))
    }

    override fun logout() {
        methodCalls.add("logout")
        _uiState.postValue(AccountUiState.LoggedOut)
    }

    fun setUiState(state: AccountUiState) {
        _uiState.value = state
    }
}