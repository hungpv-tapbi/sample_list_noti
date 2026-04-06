package com.example.testnoti.utils

import com.example.testnoti.model.NotificationItem

object DataProvider {
    fun getSampleData(): List<NotificationItem> {
        val list = List(
            50
        ) { index ->
            NotificationItem.Notification(
                id = 500 + index,
                groupId = 1,
                title = "Cỏ dại và hoa dành dành",
                content = "noti ${500 + index}"
            )
        }

        val moreNoti = List(1000) { index ->
            NotificationItem.Notification(
                id = 1000 + index,
                groupId = -1,
                title = "Cỏ dại và hoa dành dành",
                content = "noti ${1000 + index}"
            )
        }
        val sampleList: List<NotificationItem> = listOf(
            NotificationItem.Notification(
                id = 202,
                groupId = -1,
                title = "You co len",
                content = "noti 202"

            ),

            // Group 1 (3 notifications)
            NotificationItem.Group(
                id = 1,
                appName = "APP Test 1",
                notifications = listOf(
                    NotificationItem.Notification(
                        id = 101,
                        groupId = 1,
                        title = "Cỏ dại và hoa dành dành",

                        content = "noti 1"
                    ),
                    NotificationItem.Notification(
                        id = 102,
                        groupId = 1,
                        title = "Cỏ dại và hoa dành dành",

                        content = "noti 2"
                    ),
                    NotificationItem.Notification(
                        id = 103,
                        groupId = 1,
                        title = "Cỏ dại và hoa dành dành",

                        content = "noti 3"
                    )
                ) + list
            ),

            // Notification lẻ
            NotificationItem.Notification(
                id = 201,
                groupId = -1,
                title = "You co len",
                content = "noti 4"

            ),

            // Group 2 (2 notifications)
            NotificationItem.Group(
                id = 2,
                appName = "APP Test 2",
                notifications = listOf(
                    NotificationItem.Notification(
                        id = 301,
                        groupId = 2,
                        title = "Test notification",

                        content = "noti 5"
                    ),
                    NotificationItem.Notification(
                        id = 302,
                        groupId = 2,
                        title = "Test notification",

                        content = "noti 6"
                    )
                )
            ),
        ) + moreNoti
        return sampleList
    }

}