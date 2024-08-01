package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.DiaryEntry
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryEntryDetailsUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryEntryDetailsViewModel

class FakeDiaryEntryDetailsViewModel : DiaryEntryDetailsViewModel {
    override val _uiState = MutableLiveData<DiaryEntryDetailsUiState>(DiaryEntryDetailsUiState.Loading)
    override val uiState: LiveData<DiaryEntryDetailsUiState> = _uiState

    private var diaryEntry: DiaryEntry? = null

    fun setDiaryEntry(entry: DiaryEntry) {
        diaryEntry = entry
        _uiState.postValue(DiaryEntryDetailsUiState.Success(entry))
    }

    override fun loadDiaryEntry(id: Int) {
        if (diaryEntry != null && diaryEntry?.id == id) {
            _uiState.postValue(DiaryEntryDetailsUiState.Success(diaryEntry!!))
        } else {
            _uiState.postValue(DiaryEntryDetailsUiState.Error("Entry not found"))
        }
    }

    override fun deleteDiaryEntry(id: Int) {
        if (diaryEntry != null && diaryEntry?.id == id) {
            diaryEntry = null
            _uiState.postValue(DiaryEntryDetailsUiState.Deleted)
        } else {
            _uiState.postValue(DiaryEntryDetailsUiState.Error("Failed to delete entry"))
        }
    }
}