package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel

import androidx.lifecycle.LiveData
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.DiaryEntry

sealed class DiaryUiState {
    object Loading : DiaryUiState()
    data class Success(val entries: List<DiaryEntry>, val lastOpenedText: String?) : DiaryUiState()
    object LoggedOut : DiaryUiState()
    data class Error(val message: String) : DiaryUiState()
}

interface DiaryViewModel {
    val uiState: LiveData<DiaryUiState>
    fun loadDiaryEntries()
    fun createDiaryEntry(title: String, content: String)
    fun forceRefresh()
}