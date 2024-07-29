package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface AccountViewModel {
    val uiState: LiveData<AccountUiState>
    fun checkAuthenticationStatus()
    fun login(username: String, password: String)
    fun logout()
}