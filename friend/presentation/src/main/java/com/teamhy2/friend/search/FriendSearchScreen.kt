package com.teamhy2.friend.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import com.teamhy2.friend.domain.model.FriendStatus
import com.teamhy2.friend.search.component.SearchBar
import com.teamhy2.friend.search.component.SearchResultItem
import com.teamhy2.friend.search.model.SearchResultItemUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun FriendSearchRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onNavigateToFriend: () -> Unit = onBackClick,
    viewModel: FriendSearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val localSnackBar = LocalShowSnackBar.current
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is FriendSearchEffect.ShowSnackBar -> localSnackBar.showSnackBar(effect.message)
                FriendSearchEffect.NavigateToFriend -> onNavigateToFriend()
            }
        }
    }

    FriendSearchScreen(
        query = uiState.query,
        onQueryChange = viewModel::onQueryChange,
        onClearQuery = viewModel::clearQuery,
        onBackClick = viewModel::onBackClicked,
        onStatusButtonClick = viewModel::onFriendStatusButtonClick,
        results = uiState.results,
        modifier = modifier,
    )
}

@Composable
fun FriendSearchScreen(
    modifier: Modifier = Modifier,
    query: String,
    results: ImmutableList<SearchResultItemUiModel>,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onBackClick: () -> Unit,
    onStatusButtonClick: (userId: Long, userNickname: String, friendStatus: FriendStatus) -> Unit = { _, _, _ -> },
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(start = 31.dp, end = 32.dp, top = 33.dp),
    ) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onClearQuery = onClearQuery,
            onBackClick = onBackClick,
        )
        Spacer(modifier = Modifier.height(24.dp))
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            items(results) { item ->
                SearchResultItem(
                    nickname = item.friend.nickname,
                    status = item.friendStatus,
                    modifier = Modifier.fillMaxWidth(),
                    onStatusButtonClick = { status ->
                        onStatusButtonClick(
                            item.friend.userId,
                            item.friend.nickname,
                            status,
                        )
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun FriendSearchScreenEmptyPreview() {
    HY2Theme {
        FriendSearchScreen(
            query = "",
            onQueryChange = {},
            onClearQuery = {},
            onBackClick = {},
            results = persistentListOf(),
        )
    }
}

@Preview
@Composable
private fun FriendSearchScreenLoadedPreview() {
    HY2Theme {
        FriendSearchScreen(
            query = "Android",
            onQueryChange = {},
            onClearQuery = {},
            onBackClick = {},
            results = persistentListOf(),
        )
    }
}
