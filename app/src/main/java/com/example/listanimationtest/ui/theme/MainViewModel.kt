package com.example.listanimationtest.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.lifecycle.ViewModel
import java.util.UUID

class MainViewModel : ViewModel() {

    @Stable
    data class Item(
        val uuid: String = UUID.randomUUID().toString(),
        val description: String = LoremIpsum(10).values.first().replace("\n", " "),
    )

    data class UIState(
        val data: List<Item>
    ) {
        companion object {
            val default = UIState(
                data = listOf(Item(),Item())
            )
        }
    }

    var uiState by mutableStateOf(UIState.default)

    private fun updateUiState(uiState: UIState) {
        this.uiState = uiState
    }

    fun addItem() {
        updateUiState(
            uiState.copy(
                data = (uiState.data.toMutableList().apply {
                    add(0,Item())
                })
            )
        )
    }

    fun deleteItem(uuid: String) {
        updateUiState(
            uiState.copy(
                data = (uiState.data.filter { item ->
                    item.uuid != uuid
                })
            )
        )
    }
}