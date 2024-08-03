package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.CreateEntryUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.CreateEntryViewModel
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.CreateEntryViewModelImpl

@Composable
fun CreateEntryScreen(
    viewModel: CreateEntryViewModel = hiltViewModel<CreateEntryViewModelImpl>(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.observeAsState()

    when (val state = uiState) {
        is CreateEntryUiState.Initial -> CircularProgressIndicator()
        is CreateEntryUiState.NotLoggedIn -> NotLoggedInMessage()
        is CreateEntryUiState.Form -> CreateEntryForm(
            state = state,
            onTitleChanged = viewModel::onTitleChanged,
            onContentChanged = viewModel::onContentChanged,
            onEmotionChanged = viewModel::onEmotionChanged,
            onTagAdded = viewModel::onTagAdded,
            onTagRemoved = viewModel::onTagRemoved,
            onSubmit = viewModel::onSubmit
        )
        is CreateEntryUiState.Loading -> CircularProgressIndicator()
        is CreateEntryUiState.Success -> {
            LaunchedEffect(Unit) {
                onNavigateBack()
            }
        }
        is CreateEntryUiState.Error -> ErrorMessage(state.message)
        null -> {} // TODO
    }
}

@Composable
fun CreateEntryForm(
    state: CreateEntryUiState.Form,
    onTitleChanged: (String) -> Unit,
    onContentChanged: (String) -> Unit,
    onEmotionChanged: (String) -> Unit,
    onTagAdded: (String) -> Unit,
    onTagRemoved: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = state.title,
            onValueChange = onTitleChanged,
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.content,
            onValueChange = onContentChanged,
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth().height(200.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        EmotionSelector(
            selectedEmotions = state.emotions,
            onEmotionChanged = onEmotionChanged
        )
        Spacer(modifier = Modifier.height(8.dp))
        TagInput(
            tags = state.tags,
            onTagAdded = onTagAdded,
            onTagRemoved = onTagRemoved
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onSubmit,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Submit")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionSelector(
    selectedEmotions: List<String>,
    onEmotionChanged: (String) -> Unit
) {
    val emotions = listOf("Nostalgia", "Wonder", "Joy", "Fear", "Curiosity", "Shock", "Determination", "Loneliness", "Hope")

    Column {
        Text("Emotions", style = MaterialTheme.typography.titleMedium)
        LazyRow {
            items(emotions) { emotion ->
                FilterChip(
                    selected = selectedEmotions.contains(emotion),
                    onClick = { onEmotionChanged(emotion) },
                    label = { Text(emotion) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}

@Composable
fun TagInput(
    tags: List<String>,
    onTagAdded: (String) -> Unit,
    onTagRemoved: (String) -> Unit
) {
    var newTag by remember { mutableStateOf("") }

    Column {
        Text("Tags", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newTag,
                onValueChange = { newTag = it },
                label = { Text("Add Tag") },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                if (newTag.isNotBlank()) {
                    onTagAdded(newTag)
                    newTag = ""
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Tag")
            }
        }
        LazyRow {
            items(tags) { tag ->
                ChipItem(
                    text = tag,
                    onClick = { onTagRemoved(tag) },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}

@Composable
fun NotLoggedInMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("You need to be logged in to create a new entry. Please log in and try again.")
    }
}

@Composable
fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )
    }
}