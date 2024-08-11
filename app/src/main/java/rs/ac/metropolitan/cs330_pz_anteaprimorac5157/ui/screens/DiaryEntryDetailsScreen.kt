package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.DiaryEntry
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.theme.LightBlue
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.theme.LightPurple
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryEntryDetailsUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryEntryDetailsViewModel
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.impl.DiaryEntryDetailsViewModelImpl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryEntryDetailsScreen(
    entryId: Int,
    viewModel: DiaryEntryDetailsViewModel = hiltViewModel<DiaryEntryDetailsViewModelImpl>(),
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val uiState by viewModel.uiState.observeAsState(DiaryEntryDetailsUiState.Loading)
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    LaunchedEffect(entryId) {
        viewModel.loadDiaryEntry(entryId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diary Entry Details") },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onNavigateToEdit(entryId) },
                        modifier = Modifier.testTag("edit_button")
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                    }
                    IconButton(
                        onClick = { showDeleteConfirmation = true },
                        modifier = Modifier.testTag("delete_button")
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val state = uiState) {
                is DiaryEntryDetailsUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is DiaryEntryDetailsUiState.Success -> {
                    DiaryEntryContent(entry = state.entry)
                    if (showDeleteConfirmation) {
                        DeleteConfirmationDialog(
                            onConfirm = {
                                viewModel.deleteDiaryEntry(state.entry.id)
                                showDeleteConfirmation = false
                            },
                            onDismiss = { showDeleteConfirmation = false }
                        )
                    }
                }
                is DiaryEntryDetailsUiState.Error -> {
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        Text("Error: ${state.message}")
                        Button(onClick = { viewModel.loadDiaryEntry(entryId) }) {
                            Text("Retry")
                        }
                    }
                }
                is DiaryEntryDetailsUiState.Deleted -> {
                    LaunchedEffect(Unit) {
                        onNavigateBack()
                    }
                }
                DiaryEntryDetailsUiState.LoggedOut -> LoggedOutScreen()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DiaryEntryContent(entry: DiaryEntry) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = entry.title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.testTag("entry_title")
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Created on: ${entry.createdDate}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.testTag("entry_date")
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = entry.content,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.testTag("entry_content")
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (entry.emotions.isNotEmpty()) {
            Text("Emotions:", style = MaterialTheme.typography.labelMedium)
            FlowRow(
                modifier = Modifier.testTag("emotions_row"),
                maxItemsInEachRow = 3
            ) {
                entry.emotions.forEach { emotion ->
                    ChipItem(
                        text = emotion,
                        containerColor = LightPurple
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (entry.tags.isNotEmpty()) {
            Text("Tags:", style = MaterialTheme.typography.labelMedium)
            FlowRow(
                modifier = Modifier.testTag("tags_row"),
                maxItemsInEachRow = 3
            ) {
                entry.tags.forEach { tag ->
                    ChipItem(
                        text = tag,
                        containerColor = LightBlue
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Entry") },
        text = { Text("Are you sure you want to delete this entry? This action cannot be undone.") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                modifier = Modifier.testTag("confirm_delete_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.testTag("dismiss_delete_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Cancel")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun ChipItem(
    text: String,
    containerColor: Color
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}