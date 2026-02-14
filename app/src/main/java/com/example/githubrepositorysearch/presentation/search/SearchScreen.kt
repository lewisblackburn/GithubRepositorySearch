package com.example.githubrepositorysearch.presentation.search

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.githubrepositorysearch.domain.model.Repository

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val query = viewModel.searchQuery

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Search Bar
        SearchBar(
            query = query,
            onQueryChange = viewModel::onQueryChange,
            onSearch = viewModel::onSearch
        )

        // Content
        when (uiState) {
            is SearchUiState.Idle -> EmptyState()
            is SearchUiState.Loading -> LoadingState()
            is SearchUiState.Error -> ErrorState(message = uiState.message)
            is SearchUiState.Success -> RepositoryList(repositories = uiState.repositories)
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Search GitHub repositories...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
        singleLine = true
    )
}

@Composable
private fun RepositoryList(repositories: List<Repository>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = repositories, key = { it.id }) { repo ->
            RepositoryCard(repository = repo)
        }
    }
}

@Composable
private fun RepositoryCard(repository: Repository) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repository.htmlUrl))
            context.startActivity(intent)
        }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = repository.ownerAvatarUrl,
                contentDescription = "${repository.ownerLogin} avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = repository.fullName, style = MaterialTheme.typography.titleMedium)
                repository.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    repository.language?.let { lang ->
                        StatChip(icon = "●", label = lang)
                    }
                    StatChip(icon = "★", label = repository.stars.formatCount())
                    StatChip(icon = "⑂", label = repository.forks.formatCount())
                }
            }
        }
    }
}

@Composable
private fun StatChip(icon: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = icon, style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Search for GitHub repositories", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun ErrorState(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
    }
}

// Helper extension
fun Int.formatCount(): String = when {
    this >= 1_000 -> "${this / 1_000}k"
    else -> toString()
}
