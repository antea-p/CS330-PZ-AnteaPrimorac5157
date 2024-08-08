package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.DiaryEntry
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryViewModel
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.impl.DiaryViewModelImpl

@Composable
fun DiaryScreen(
    viewModel: DiaryViewModel = hiltViewModel<DiaryViewModelImpl>(),
    onCreateEntry: () -> Unit,
    onEntryClick: (Int) -> Unit,
    needsRefresh: Boolean,
    onRefreshComplete: () -> Unit
) {
    val uiState by viewModel.uiState.observeAsState(DiaryUiState.Loading)

    LaunchedEffect(needsRefresh) {
        if (needsRefresh) {
            viewModel.forceRefresh()
            onRefreshComplete()
        }
    }

    Box(modifier = Modifier.testTag("diary_screen")) {
        when (val state = uiState) {
            is DiaryUiState.Loading -> DiaryScreenLoadingIndicator()
            is DiaryUiState.LoggedOut -> LoggedOutScreen()
            is DiaryUiState.Success -> DiaryEntriesList(
                entries = state.entries,
                lastOpenedText = state.lastOpenedText,
                onEntryClick = onEntryClick,
                onCreateEntry = onCreateEntry
            )
            is DiaryUiState.Error -> DiaryScreenError(message = state.message)
        }
    }
}

@Composable
fun DiaryScreenLoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun LoggedOutScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("logged_out_screen"),
        contentAlignment = Alignment.Center
    ) {
        Text("You are not currently logged in. Go to the account tab to log in!")
    }
}

@Composable
fun DiaryEntriesList(
    entries: List<DiaryEntry>,
    lastOpenedText: String?,
    onEntryClick: (Int) -> Unit,
    onCreateEntry: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateEntry,
                modifier = Modifier.testTag("create_diary_entry_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create new entry")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            lastOpenedText?.let {
                Text(
                    text = "Last opened $it",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                        .testTag("last_opened_text")
                )
            }
            if (entries.isEmpty()) {
                EmptyDiaryEntriesList()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("diary_entries_list")
                ) {
                    items(entries) { entry ->
                        DiaryEntryItem(entry = entry, onClick = { onEntryClick(entry.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyDiaryEntriesList() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("empty_diary_entries_list"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Your diary entry list is currently empty.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun DiaryEntryItem(
    entry: DiaryEntry,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
            .testTag("diary_entry_item")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = entry.title, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = entry.content, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = entry.createdDate, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun DiaryScreenError(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(message, color = MaterialTheme.colorScheme.error)
    }
}