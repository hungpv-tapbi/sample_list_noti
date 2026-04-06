package com.example.testnoti.model

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class NotificationGroupState(
    initialItems: List<NotificationItem.Notification> = emptyList()
) {
    var items by mutableStateOf(initialItems)
        private set
    var isExpanded by mutableStateOf(false)
        private set

    fun toggle() {
        isExpanded = !isExpanded
    }

    fun add(item: NotificationItem.Notification) {
        items = listOf(item) + items
    }

    fun dismiss(id: Int) {
        items = items.filterNot { it.id == id }
    }
}