package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.EmotionEnum
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.CreateEntryUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.CreateEntryViewModel

class FakeCreateEntryViewModel : CreateEntryViewModel {
    override val _uiState = MutableLiveData<CreateEntryUiState>(CreateEntryUiState.Form("", "", emptyList(), emptyList()))
    override val uiState: LiveData<CreateEntryUiState> = _uiState

    private var title = ""
    private var content = ""
    private val emotions = mutableListOf<EmotionEnum>()
        get() = field
    private val tags = mutableListOf<String>()
        get() = field

    override fun onTitleChanged(title: String) {
        this.title = title
        updateFormState()
    }

    override fun onContentChanged(content: String) {
        this.content = content
        updateFormState()
    }

    override fun onEmotionChanged(emotion: EmotionEnum) {
        if (emotions.contains(emotion)) {
            emotions.remove(emotion)
        } else {
            emotions.add(emotion)
        }
        updateFormState()
    }

    override fun onTagAdded(tag: String) {
        tags.add(tag)
        updateFormState()
    }

    override fun onTagRemoved(tag: String) {
        tags.remove(tag)
        updateFormState()
    }

    override fun onSubmit() {
        if (title.isNotBlank() && content.isNotBlank()) {
            _uiState.value = CreateEntryUiState.Success
        }
    }

    override fun updateFormState(update: (CreateEntryUiState.Form) -> CreateEntryUiState.Form) {
        TODO("Not yet implemented")
    }


    private fun updateFormState() {
        _uiState.value = CreateEntryUiState.Form(title, content, emotions, tags)
    }
}