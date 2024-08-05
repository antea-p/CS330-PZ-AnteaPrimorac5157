package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.EmotionEnum

sealed class CreateEntryUiState {
    //object Initial : CreateEntryUiState()
    object LoggedOut : CreateEntryUiState()
    data class Form(
        val title: String,
        val content: String,
        val emotions: List<EmotionEnum>,
        val tags: List<String>
    ) : CreateEntryUiState()
    object Loading : CreateEntryUiState()
    object Success : CreateEntryUiState()
    data class Error(val message: String) : CreateEntryUiState()
}

interface CreateEntryViewModel {
    val _uiState: MutableLiveData<CreateEntryUiState>
    val uiState: LiveData<CreateEntryUiState>
    fun onTitleChanged(title: String)
    fun onContentChanged(content: String)
    fun onEmotionChanged(emotion: EmotionEnum)
    fun onTagAdded(tag: String)
    fun onTagRemoved(tag: String)
    fun onSubmit()
    fun updateFormState(update: (CreateEntryUiState.Form) -> CreateEntryUiState.Form)
}