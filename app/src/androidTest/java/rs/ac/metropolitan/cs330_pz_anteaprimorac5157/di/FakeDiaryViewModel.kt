import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.DiaryEntry
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryViewModel

class FakeDiaryViewModel(loggedIn: Boolean) : DiaryViewModel {
    private val _uiState = MutableLiveData<DiaryUiState>(
        if (loggedIn) DiaryUiState.Loading else DiaryUiState.LoggedOut
    )
    override val uiState: LiveData<DiaryUiState> = _uiState

    private var lastOpenedText: String? = null

    init {
        if (!loggedIn) {
            _uiState.postValue(DiaryUiState.LoggedOut)
        }
    }

    fun loadEmptyDiaryEntryList() {
        _uiState.postValue(DiaryUiState.Success(emptyList(), lastOpenedText))
    }

    override fun loadDiaryEntries() {
        _uiState.postValue(DiaryUiState.Success(
            listOf(
                DiaryEntry(1, "First Entry", "Content 1", "2023-07-01", emptyList(), emptyList()),
                DiaryEntry(2, "Second Entry", "Content 2", "2023-07-02", emptyList(), emptyList())
            ),
            lastOpenedText
        ))
    }

    fun setLastOpenedText(text: String?) {
        lastOpenedText = text
    }

    override fun createDiaryEntry(title: String, content: String) {
        TODO("Not yet implemented")
    }

    override fun forceRefresh() {
       loadDiaryEntries()
    }

}

