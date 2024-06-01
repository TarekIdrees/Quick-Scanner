package com.tareq.feature.archive


import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tareq.feature.archive.comosable.EmptyArchivePlaceholder

@Composable
fun ArchiveScreen(modifier: Modifier = Modifier, viewModel: ArchiveViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isArchivedItemsEmpty by remember {
        derivedStateOf { uiState.isArchivedItemsEmpty() }
    }
    if (isArchivedItemsEmpty)
        EmptyArchivePlaceholder()
}