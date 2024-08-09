package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.DreamDiaryRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.AuthenticationService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryEntryDetailsUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryEntryDetailsViewModel
import javax.inject.Inject

@HiltViewModel
    class DiaryEntryDetailsViewModelImpl @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
    private val authService: AuthenticationService
) : ViewModel(), DiaryEntryDetailsViewModel {

    override val _uiState = MutableLiveData<DiaryEntryDetailsUiState>(DiaryEntryDetailsUiState.Loading)
    override val uiState: LiveData<DiaryEntryDetailsUiState> = _uiState

    init {
        viewModelScope.launch {
            checkAuthentication()
        }
    }

    private suspend fun checkAuthentication() {
        authService.checkAuthentication()
        authService.getUsername().collect { username ->
            if (username == null) {
                _uiState.value = DiaryEntryDetailsUiState.LoggedOut
            }
        }
    }

    override fun loadDiaryEntry(id: Int) {
        viewModelScope.launch {
            _uiState.value = DiaryEntryDetailsUiState.Loading
            try {
                val token = authService.getToken().first()
                if (token != null) {
                    val entry = dreamDiaryRepository.getDiaryEntryById(token, id)
                    _uiState.value = DiaryEntryDetailsUiState.Success(entry)
                } else {
                    _uiState.value = DiaryEntryDetailsUiState.LoggedOut
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    401 -> _uiState.value = DiaryEntryDetailsUiState.LoggedOut
                    404 -> _uiState.value = DiaryEntryDetailsUiState.Error("Entry not found")
                    else -> _uiState.value =
                        DiaryEntryDetailsUiState.Error("Failed to load entry: ${e.message()}")
                }
            } catch (e: Exception) {
                _uiState.value =
                    DiaryEntryDetailsUiState.Error("Failed to load entry: ${e.message}")
            }
        }
    }

    override fun deleteDiaryEntry(id: Int) {
        viewModelScope.launch {
            _uiState.value = DiaryEntryDetailsUiState.Loading
            try {
                val token = authService.getToken().first()
                if (token != null) {
                    dreamDiaryRepository.deleteDiaryEntry(token, id)
                    _uiState.value = DiaryEntryDetailsUiState.Deleted
                } else {
                    _uiState.value = DiaryEntryDetailsUiState.LoggedOut
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    401 -> _uiState.value = DiaryEntryDetailsUiState.LoggedOut
                    404 -> _uiState.value = DiaryEntryDetailsUiState.Error("Entry not found")
                    else -> _uiState.value =
                        DiaryEntryDetailsUiState.Error("Failed to delete entry: ${e.message()}")
                }
            } catch (e: Exception) {
                _uiState.value =
                    DiaryEntryDetailsUiState.Error("Failed to delete entry: ${e.message}")
            }
        }
    }
}
