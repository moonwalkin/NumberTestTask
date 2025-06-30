package com.moonwalkin.numbertesttask.presentation.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moonwalkin.numbertesttask.R

@Composable
fun HomeScreen(
    isOffline: Boolean,
    openDetails: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreenContent(
        isOffline = isOffline,
        modifier = modifier.padding(horizontal = 16.dp),
        onGetFactClick = viewModel::loadNumberInfo,
        onRandomFactClick = viewModel::getRandomNumberInfo,
        state = state,
        openDetails = openDetails
    )
}

@Composable
private fun HomeScreenContent(
    state: HomeViewModel.HomeScreenState,
    isOffline: Boolean,
    onGetFactClick: (Long) -> Unit,
    onRandomFactClick: () -> Unit,
    openDetails: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var text by rememberSaveable {
        Log.d("TAG", "called")
        mutableStateOf("")
    }
    val listState = rememberLazyListState()

    LaunchedEffect(state.error) {
        Log.d("TAG", "launched")
        if (state.error != null) {
            Toast.makeText(
                context,
                state.error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(state.history) {
        if (state.history.isNotEmpty()) {
            listState.scrollToItem(0)
        }
    }

    Box(modifier = modifier) {
        Column {
            Text(text = state.numberInfo)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                onValueChange = { text = it },
                label = { Text(stringResource(R.string.enter_number)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { onGetFactClick(text.toLongOrNull() ?: 0) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isOffline
            ) {
                Text(text = stringResource(R.string.get_fact))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onRandomFactClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isOffline
            ) {
                Text(text = stringResource(R.string.get_fact_about_random_number))
            }
            HistoryItems(listState, state, openDetails)
        }
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun HistoryItems(
    lazyListState: LazyListState,
    state: HomeViewModel.HomeScreenState,
    openDetails: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, state = lazyListState, contentPadding = PaddingValues(8.dp)) {
        items(items = state.history, key = { it.id }) { numberInfo ->
            Text(
                text = "${numberInfo.number} — ${numberInfo.text}",
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable { openDetails(numberInfo.number, numberInfo.text) },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}