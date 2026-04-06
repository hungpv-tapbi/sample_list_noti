package com.example.testnoti.model

sealed class UiItem {
    data class GroupHeader(val group: NotificationItem.Group) : UiItem()
    data class Child(val notification: NotificationItem.Notification) : UiItem()
    data class Single(val notification: NotificationItem.Notification) : UiItem()
}