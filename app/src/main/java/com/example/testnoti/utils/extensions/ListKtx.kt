package com.example.testnoti.utils.extensions

import com.example.testnoti.model.NotificationItem
import com.example.testnoti.model.UiItem

fun <T> List<T>.isAtLeast(size: Int): Boolean {
    return this.size >= size
}

fun List<NotificationItem>.flatten(): List<NotificationItem.Notification> {
    return flatMap { item ->
        when (item) {
            is NotificationItem.Group -> {
                item.notifications.mapIndexed { index, noti ->
                    noti.copy(isVisible = index == 0)
                }
            }

            is NotificationItem.Notification -> {
                listOf(item.copy(isVisible = true))
            }
        }
    }
}

fun List<NotificationItem>.toUiList(): List<UiItem> {
    return buildList {

        this@toUiList.forEach { item ->

            when (item) {

                is NotificationItem.Group -> {
                    add(UiItem.GroupHeader(item))

                    item.notifications.forEachIndexed { idx, notification ->
                        add(
                            UiItem.Child(
                                notification.copy(
                                    isVisible = item.isExpanded,
                                    index = idx
                                )
                            )
                        )
                    }
                }

                is NotificationItem.Notification -> {
                    add(UiItem.Single(item.copy(isVisible = true)))
                }
            }
        }
    }
}

fun List<NotificationItem>.updateGroup(
    groupId: Int,
    expanded: Boolean
): List<NotificationItem> {
    return map {
        if (it is NotificationItem.Group && it.id == groupId) {
            it.copy(isExpanded = expanded)
        } else it
    }
}