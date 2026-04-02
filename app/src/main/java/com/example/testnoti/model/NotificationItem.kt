package com.example.testnoti.model

sealed class NotificationItem {

    data class GroupNotification(
        val id: Int,
        val notifications: List<Notification>
    ) : NotificationItem()

    data class Notification(
        val id: Int,
        val groupId: Int,
        val color: Int
    ): NotificationItem()
}