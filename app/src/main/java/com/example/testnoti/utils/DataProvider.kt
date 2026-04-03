package com.example.testnoti.utils

import com.example.testnoti.model.NotificationItem

object DataProvider {
    fun getSampleData(): List<NotificationItem> {
        val sampleList: List<NotificationItem> = listOf(

            // Group 1 (3 notifications)
            NotificationItem.Group(
                id = 1,
                appName = "APP Test 1",
                notifications = listOf(
                    NotificationItem.Notification(
                        id = 101,
                        groupId = 1,
                        color = 0xFFE57373.toInt(),
                        content = "noti 1"
                    ),
                    NotificationItem.Notification(
                        id = 102,
                        groupId = 1,
                        color = 0xFF64B5F6.toInt(),
                        content = "noti 2"
                    ),
                    NotificationItem.Notification(
                        id = 103,
                        groupId = 1,
                        color = 0xFF81C784.toInt(),
                        content = "noti 3"
                    )
                )
            ),

            // Notification lẻ
            NotificationItem.Notification(
                id = 201,
                groupId = -1,
                color = 0xFFFFB74D.toInt(), content = "noti 4"

            ),

            // Group 2 (2 notifications)
            NotificationItem.Group(
                id = 2,
                appName = "APP Test 2",
                notifications = listOf(
                    NotificationItem.Notification(
                        id = 301,
                        groupId = 2,
                        color = 0xFFBA68C8.toInt(),
                        content = "noti 5"
                    ),
                    NotificationItem.Notification(
                        id = 302,
                        groupId = 2,
                        color = 0xFF4DB6AC.toInt(),
                        content = "noti 6"
                    )
                )
            ),

            // Notification lẻ
            NotificationItem.Notification(
                id = 202,
                groupId = -1,
                color = 0xFFA1887F.toInt(), content = "noti 7"
            ),

            NotificationItem.Notification(
                id = 203,
                groupId = -1,
                color = 0xFFA1887F.toInt(), content = "noti 8"
            ),

            NotificationItem.Notification(
                id = 204,
                groupId = -1,
                color = 0xFFA1887F.toInt(), content = "noti 9"
            ),

            NotificationItem.Notification(
                id = 205,
                groupId = -1,
                color = 0xFFA1887F.toInt(), content = "noti 10"
            ),

            NotificationItem.Notification(
                id = 206,
                groupId = -1,
                color = 0xFFA1887F.toInt(), content = "noti 11"
            ),

            NotificationItem.Notification(
                id = 207,
                groupId = -1,
                color = 0xFFA1887F.toInt(), content = "noti 12"
            ),

            NotificationItem.Notification(
                id = 208,
                groupId = -1,
                color = 0xFFA1887F.toInt(), content = "noti 13"
            ),

            NotificationItem.Notification(
                id = 209,
                groupId = -1,
                color = 0xFFA1887F.toInt(), content = "noti 14"
            )
        )
        return sampleList
    }

}