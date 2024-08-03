package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.DreamDiaryRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.AuthenticationService
import javax.inject.Inject

@HiltViewModel
class CreateEntryViewModelImpl @Inject constructor(
    private val repository: DreamDiaryRepository,
    private val authService: AuthenticationService
) : ViewModel(), CreateEntryViewModel {

    override val _uiState = MutableLiveData<CreateEntryUiState>(CreateEntryUiState.Initial)
    override val uiState: LiveData<CreateEntryUiState> = _uiState

    init {
        viewModelScope.launch {
            val username = authService.getUsername().first()
            _uiState.value = if (username != null) {
                CreateEntryUiState.Form(title = "", content = "", emotions = emptyList(), tags = emptyList())
            } else {
                CreateEntryUiState.NotLoggedIn
            }
        }
    }

    override fun onTitleChanged(title: String) {
        updateFormState { it.copy(title = title) }
    }

    override fun onContentChanged(content: String) {
        updateFormState { it.copy(content = content) }
    }

    override fun onEmotionChanged(emotion: String) {
        updateFormState { it.copy(emotions = listOf(emotion)) }
    }

    override fun onTagAdded(tag: String) {
        updateFormState { it.copy(tags = it.tags + tag) }
    }

    override fun onTagRemoved(tag: String) {
        updateFormState { it.copy(tags = it.tags - tag) }
    }

    override fun onSubmit() {
        val currentState = _uiState.value
        if (currentState is CreateEntryUiState.Form) {
            viewModelScope.launch {
                _uiState.value = CreateEntryUiState.Loading
                try {
                    val token = authService.getToken().first()
                    if (token != null) {
                        val result = repository.createDiaryEntry(
                            token = token,
                            title = currentState.title,
                            content = currentState.content,
                            emotions = currentState.emotions,
                            tags = currentState.tags
                        )
                        _uiState.value = CreateEntryUiState.Success
                    } else {
                        _uiState.value = CreateEntryUiState.Error("User not authenticated")
                    }
                } catch (e: Exception) {
                    _uiState.value = CreateEntryUiState.Error(e.message ?: "Unknown error occurred")
                }
            }
        }
    }

    override fun updateFormState(update: (CreateEntryUiState.Form) -> CreateEntryUiState.Form) {
        val currentState = _uiState.value
        if (currentState is CreateEntryUiState.Form) {
            _uiState.value = update(currentState)
        }
    }
}

