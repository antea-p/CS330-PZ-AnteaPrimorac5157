package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel

import androidx.lifecycle.LiveData
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.impl.AccountUiState

interface AccountViewModel {
    val uiState: LiveData<AccountUiState>
    fun checkAuthenticationStatus()
    fun login(username: String, password: String)
    fun logout()
}