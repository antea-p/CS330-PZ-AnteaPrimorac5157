package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.DreamDiaryRepository
import javax.inject.Inject

@HiltViewModel
    class DiaryEntryDetailsViewModelImpl @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository
) : ViewModel(), DiaryEntryDetailsViewModel {

    override val _uiState = MutableLiveData<DiaryEntryDetailsUiState>(DiaryEntryDetailsUiState.Loading)
    override val uiState: LiveData<DiaryEntryDetailsUiState> = _uiState

    override fun loadDiaryEntry(id: Int) {
        viewModelScope.launch {
            _uiState.value = DiaryEntryDetailsUiState.Loading
            try {
                val entry = dreamDiaryRepository.getDiaryEntryById(id)
                _uiState.value = DiaryEntryDetailsUiState.Success(entry)
            } catch (e: NoSuchElementException) {
                _uiState.value = DiaryEntryDetailsUiState.Error("Entry not found")
            } catch (e: Exception) {
                _uiState.value = DiaryEntryDetailsUiState.Error("Failed to load entry: ${e.message}")
            }
        }
    }

    override fun deleteDiaryEntry(id: Int) {
        viewModelScope.launch {
            _uiState.value = DiaryEntryDetailsUiState.Loading
            try {
                dreamDiaryRepository.deleteDiaryEntry(id)
                _uiState.value = DiaryEntryDetailsUiState.Deleted
            } catch (e: NoSuchElementException) {
                _uiState.value = DiaryEntryDetailsUiState.Error("Entry not found")
            } catch (e: Exception) {
                _uiState.value = DiaryEntryDetailsUiState.Error("Failed to delete entry: ${e.message}")
            }
        }
    }
}