package com.example.testnoti.model

sealed class NotificationItem {
    data class Group(
        val id: Int,
        val appName: String,
        val notifications: List<Notification>,
        val isExpanded: Boolean = false
    ) : NotificationItem()

    data class Notification(
        val id: Int,
        val groupId: Int? = null,
        val title: String = "",
        val content: String = "",
        val index: Int = -1,
        val isVisible: Boolean = false
    ) : NotificationItem()
}
