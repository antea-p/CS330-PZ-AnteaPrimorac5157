package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.DiaryEntry


sealed class DiaryEntryDetailsUiState {
    object Loading : DiaryEntryDetailsUiState()
    data class Success(val entry: DiaryEntry) : DiaryEntryDetailsUiState()
    data class Error(val message: String) : DiaryEntryDetailsUiState()
    object Deleted : DiaryEntryDetailsUiState()
    object LoggedOut : DiaryEntryDetailsUiState()
}

interface DiaryEntryDetailsViewModel {
    val _uiState: MutableLiveData<DiaryEntryDetailsUiState>
    val uiState: LiveData<DiaryEntryDetailsUiState>
    fun loadDiaryEntry(id: Int)
    fun deleteDiaryEntry(id: Int)
}