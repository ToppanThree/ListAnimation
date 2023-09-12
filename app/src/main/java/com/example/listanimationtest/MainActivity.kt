package com.example.listanimationtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.example.listanimationtest.ui.theme.ListAnimationTestTheme
import com.example.listanimationtest.ui.theme.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListAnimationTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Example(duration = 300)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationListItem(
    viewModel: MainViewModel,
    item: MainViewModel.Item,
    duration: Int
) {
    val itemVisibility = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var initialLoad by rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(key1 = item.uuid) {
        if (initialLoad) {
            itemVisibility.animateTo(targetValue = 1f, animationSpec = tween(duration))
            initialLoad = false
        } else {
            itemVisibility.snapTo(1f)
        }
    }
    Card(
        modifier = Modifier.alpha(itemVisibility.value),
        onClick = {
            scope.launch {
                itemVisibility.animateTo(targetValue = 0f, animationSpec = tween(duration))
                viewModel.deleteItem(item.uuid)
            }
        }
    ) {
        Text(
            text = item.description,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Example(
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    duration: Int = 100
) {
    val uiState = viewModel.uiState
    val itemList = uiState.data
    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    onClick = {
                        viewModel.addItem()
                    }
                ) {
                    Text(
                        text = "add an item",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(itemList, key = { it.uuid }) { item ->
                Column(
                    modifier = Modifier.animateItemPlacement()
                ) {
                    AnimationListItem(viewModel, item, duration)
                }
            }
        }
    }
}