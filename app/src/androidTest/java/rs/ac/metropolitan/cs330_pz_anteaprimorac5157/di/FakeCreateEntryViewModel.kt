package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.EmotionEnum
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.CreateEntryUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.CreateEntryViewModel

class FakeCreateEntryViewModel : CreateEntryViewModel {
    override val _uiState = MutableLiveData<CreateEntryUiState>(CreateEntryUiState.Form(id = null, title = "", content = "", emotions = emptyList(), tags = emptyList()))
    override val uiState: LiveData<CreateEntryUiState> = _uiState

    private var id: Int? = null
    private var title = ""
    private var content = ""
    private val emotions = mutableListOf<EmotionEnum>()
    private val tags = mutableListOf<String>()

    var lastSubmittedTitle: String? = null
    var lastSubmittedContent: String? = null
    var lastSubmittedEmotions: List<EmotionEnum>? = null
    var lastSubmittedTags: List<String>? = null

    override fun initialize(entryId: Int?) {
        if (entryId != null) {
            id = entryId
            title = "Existing Title"
            content = "Existing Content"
            emotions.clear()
            emotions.add(EmotionEnum.JOY)
            tags.clear()
            tags.add("ExistingTag")
        } else {
            id = null
            title = ""
            content = ""
            emotions.clear()
            tags.clear()
        }
        updateFormState()
    }

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
            lastSubmittedTitle = title
            lastSubmittedContent = content
            lastSubmittedEmotions = emotions.toList()
            lastSubmittedTags = tags.toList()
            _uiState.postValue(CreateEntryUiState.Success)
        }
    }

    override fun updateFormState(update: (CreateEntryUiState.Form) -> CreateEntryUiState.Form) {
        val currentState = _uiState.value
        if (currentState is CreateEntryUiState.Form) {
            _uiState.postValue(update(currentState))
        }
    }

    private fun updateFormState() {
        _uiState.postValue((CreateEntryUiState.Form(
            id = id,
            title = title,
            content = content,
            emotions = emotions.toList(),
            tags = tags.toList()
        )))
    }

    fun setExistingEntry(entry: CreateEntryUiState.Form) {
        id = entry.id
        title = entry.title
        content = entry.content
        emotions.clear()
        emotions.addAll(entry.emotions)
        tags.clear()
        tags.addAll(entry.tags)
        updateFormState()
    }
}