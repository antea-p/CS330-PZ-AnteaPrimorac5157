import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.DiaryEntry
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryViewModel

// TODO: elegantnije rijesiti praznu listu
class FakeDiaryViewModel(loggedIn: Boolean) : DiaryViewModel {
  private val _uiState = MutableLiveData(
      if (loggedIn) DiaryUiState.Loading else DiaryUiState.LoggedOut)
  override val uiState = _uiState

    init {
        if (loggedIn) {
            loadDiaryEntries()
        } else {
            _uiState.postValue(DiaryUiState.LoggedOut)
        }
    }

    override fun loadDiaryEntries() {
        _uiState.postValue(DiaryUiState.Success(listOf(
            DiaryEntry(1, "First Entry", "Content 1", "2023-07-01", emptyList(), emptyList()),
            DiaryEntry(2, "Second Entry", "Content 2", "2023-07-02", emptyList(), emptyList())
            ))
        )
    }

    override fun createDiaryEntry(title: String, content: String) {
        TODO("Not yet implemented")
    }
    
    fun loadEmptyDiaryEntryList() {
        _uiState.postValue(DiaryUiState.Success(emptyList()))
    }
}

