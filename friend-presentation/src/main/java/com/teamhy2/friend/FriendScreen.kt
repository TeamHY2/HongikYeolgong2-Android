package com.teamhy2.friend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamhy2.designsystem.common.HY2CircularLoading
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import com.teamhy2.friend.domain.model.Friend
import kotlinx.coroutines.launch

@Composable
fun FriendRoute(
    modifier: Modifier = Modifier,
    friendViewModel: FriendViewModel = hiltViewModel(),
) {
    val friendState: FriendUiState by friendViewModel.uiState.collectAsStateWithLifecycle()

    val localShowSnackBar = LocalShowSnackBar.current

    LaunchedEffect(Unit) {
        friendViewModel.sendIntent(FriendUiIntent.EnterFriendScreen)
        launch {
            friendViewModel.sideEffect.collect { sideEffect ->
                when (sideEffect) {
                    is FriendSideEffect.ShowSnackBar -> localShowSnackBar.showSnackBar(sideEffect.message)
                }
            }
        }
    }

    when (val state = friendState) {
        FriendUiState.Loading -> HY2CircularLoading()
        is FriendUiState.Loaded ->
            FriendScreen(
                friends = state.friends,
                modifier = modifier,
            )
    }
}

@Composable
fun FriendScreen(
    friends: List<Friend>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(friends) { friend ->
                Text(text = friend.name, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
