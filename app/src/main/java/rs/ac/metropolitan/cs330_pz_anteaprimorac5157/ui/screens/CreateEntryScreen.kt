package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.EmotionEnum
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.components.AnimatedButton
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.components.PulsingIconButton
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.theme.LightPurple
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.CreateEntryUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.CreateEntryViewModel
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.impl.CreateEntryViewModelImpl

@Composable
fun CreateEntryScreen(
    entryId: Int?,
    viewModel: CreateEntryViewModel = hiltViewModel<CreateEntryViewModelImpl>(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.observeAsState(CreateEntryUiState.Loading)
    val focusManager = LocalFocusManager.current

    LaunchedEffect(entryId) {
        viewModel.initialize(entryId)
    }

    when (val state = uiState) {
        is CreateEntryUiState.Loading -> CircularProgressIndicator()
        is CreateEntryUiState.LoggedOut -> LoggedOutScreen()
        is CreateEntryUiState.Form -> CreateEntryForm(
            state = state,
            onTitleChanged = viewModel::onTitleChanged,
            onContentChanged = viewModel::onContentChanged,
            onEmotionChanged = viewModel::onEmotionChanged,
            onTagAdded = viewModel::onTagAdded,
            onTagRemoved = viewModel::onTagRemoved,
            onSubmit = viewModel::onSubmit,
            focusManager = focusManager
        )
        is CreateEntryUiState.Success -> {
            LaunchedEffect(Unit) {
                onNavigateBack()
            }
        }
        is CreateEntryUiState.Error -> ErrorMessage(state.message)
    }
}

@Composable
fun CreateEntryForm(
    state: CreateEntryUiState.Form,
    onTitleChanged: (String) -> Unit,
    onContentChanged: (String) -> Unit,
    onEmotionChanged: (EmotionEnum) -> Unit,
    onTagAdded: (String) -> Unit,
    onTagRemoved: (String) -> Unit,
    onSubmit: () -> Unit,
    focusManager: FocusManager
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = if (state.id == null) "Create New Entry" else "Edit Entry",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = state.title,
            onValueChange = onTitleChanged,
            label = { Text("Title" ) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("title_input")
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.content,
            onValueChange = onContentChanged,
            label = { Text("Content") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .testTag("content_input")
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
            onTagRemoved = onTagRemoved,
            focusManager = focusManager
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedButton(
            onClick = onSubmit,
            enabled = (state.title.isNotBlank() && state.content.isNotBlank()),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .testTag("submit_button"),
        ) {
            Text(if (state.id == null) "Create" else "Update")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EmotionSelector(
    selectedEmotions: List<EmotionEnum>,
    onEmotionChanged: (EmotionEnum) -> Unit
) {
    Column {
        Text("Emotions", style = MaterialTheme.typography.titleMedium)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3
        ) {
            EmotionEnum.entries.forEach { emotion ->
                val isSelected = selectedEmotions.contains(emotion)
                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) LightPurple else MaterialTheme.colorScheme.surface,
                    animationSpec = tween(durationMillis = 300)
                )
                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface,
                    animationSpec = tween(durationMillis = 300)
                )
                FilterChip(
                    selected = isSelected,
                    onClick = { onEmotionChanged(emotion) },
                    label = { Text(emotion.name.lowercase()) },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .testTag("emotion_${emotion.name}"),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = backgroundColor,
                        selectedLabelColor = contentColor,
                        labelColor = contentColor
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagInput(
    tags: List<String>,
    onTagAdded: (String) -> Unit,
    onTagRemoved: (String) -> Unit,
    focusManager: FocusManager
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
                modifier = Modifier
                    .weight(1f)
                    .testTag("tag_input")
            )
            PulsingIconButton(
                onClick = {
                    if (newTag.isNotBlank()) {
                        onTagAdded(newTag)
                        newTag = ""
                        focusManager.clearFocus()
                    }
                },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Tag") },
                enabled = newTag.isNotBlank(),
                modifier = Modifier.testTag("add_tag_button")
            )
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3
        ) {
            tags.forEach { tag ->
                ChipItem(
                    text = tag,
                    onRemove = { onTagRemoved(tag) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ChipItem(
    text: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp, end = 4.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .size(18.dp)
                    .testTag("remove_tag_$text")
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Remove Tag",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
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