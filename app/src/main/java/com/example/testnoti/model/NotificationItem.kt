package com.example.testnoti.model

sealed class NotificationItem {
    data class Group(
        val id: Int,
        val appName: String,
        val notifications: List<Notification>,
        val isExpanded: Boolean = false
    ) : NotificationItem()

    data class Notification(
        val id: Int, val groupId: Int, val color: Int, val content: String = ""
    ) : NotificationItem()
}
