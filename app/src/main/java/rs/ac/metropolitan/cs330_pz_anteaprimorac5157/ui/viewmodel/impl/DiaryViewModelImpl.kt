package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.DreamDiaryRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.ActivityLogService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.AuthenticationService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryViewModel
import javax.inject.Inject

@HiltViewModel
class DiaryViewModelImpl @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
    private val authService: AuthenticationService,
    private val activityLogService: ActivityLogService
) : ViewModel(), DiaryViewModel {

    private val _uiState = MutableLiveData<DiaryUiState>(DiaryUiState.Loading)
    override val uiState: LiveData<DiaryUiState> = _uiState

    init {
        viewModelScope.launch {
            authService.checkAuthentication()
            authService.getUsername().collect { username ->
                if (username != null) {
                    //logAppOpen()
                    loadDiaryEntries()
                } else {
                    _uiState.value = DiaryUiState.LoggedOut
                }
            }
        }
    }

    private suspend fun logAppOpen() {
        try {
            activityLogService.logCurrentDate()
        } catch (e: Exception) {
            println("Failed to log app open: ${e.message}")
        }
    }

    override fun loadDiaryEntries() {
        viewModelScope.launch {
            _uiState.value = DiaryUiState.Loading
            try {
                // !! jer znamo da je korisnik ulogiran
                val entriesDeferred = async { dreamDiaryRepository.getDiaryEntries(authService.getToken().first()!!).first() }
                val lastOpenedDeferred = async { activityLogService.getLastLoggedDate().first() }

                val entries = entriesDeferred.await()
                val lastOpened = lastOpenedDeferred.await()

                logAppOpen()
                _uiState.value = DiaryUiState.Success(entries, lastOpened)
            } catch (e: Exception) {
                _uiState.value = DiaryUiState.Error("Failed to load diary entries: ${e.message}")
            }
        }
    }

    override fun createDiaryEntry(title: String, content: String) {
        viewModelScope.launch {
            try {
                dreamDiaryRepository.createDiaryEntry(authService.getToken().first()!!, "", "", emptyList(), emptyList())
                loadDiaryEntries()
            } catch (e: Exception) {
                _uiState.value = DiaryUiState.Error("Failed to create diary entry: ${e.message}")
            }
        }
    }

    override fun forceRefresh() {
        loadDiaryEntries()
    }
}