package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.DreamDiaryRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.AuthenticationService
import javax.inject.Inject

@HiltViewModel
class DiaryViewModelImpl @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
    private val authService: AuthenticationService
) : ViewModel(), DiaryViewModel {

    private val _uiState = MutableLiveData<DiaryUiState>(DiaryUiState.Loading)
    override val uiState: LiveData<DiaryUiState> = _uiState

    init {
        viewModelScope.launch {
            authService.getUsername().collect { username ->
                if (username != null) {
                    loadDiaryEntries()
                } else {
                    _uiState.value = DiaryUiState.LoggedOut
                }
            }
        }
    }

    override fun loadDiaryEntries() {
        viewModelScope.launch {
            _uiState.value = DiaryUiState.Loading
            try {
                dreamDiaryRepository.getDiaryEntries().collect { entries ->
                    _uiState.value = DiaryUiState.Success(entries)
                }
            } catch (e: Exception) {
                _uiState.value = DiaryUiState.Error("Failed to load diary entries: ${e.message}")
            }
        }
    }

    override fun createDiaryEntry(title: String, content: String) {
        viewModelScope.launch {
            try {
                dreamDiaryRepository.createDiaryEntry(title, content)
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