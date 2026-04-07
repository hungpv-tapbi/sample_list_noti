package com.example.testnoti.utils

import com.example.testnoti.model.NotificationItem

object DataProvider {
    fun getSampleData(): List<NotificationItem> {
        val list = List(
            12
        ) { index ->
            NotificationItem.Notification(
                id = 500 + index,
                groupId = 1,
                title = "Cỏ dại và hoa dành dành",
                content = "noti ${500 + index}",
                isCanDelete = true
            )
        }

        val moreNoti = List(1000) { index ->
            NotificationItem.Notification(
                id = 1000 + index,
                groupId = -1,
                title = "Cỏ dại và hoa dành dành",
                content = "noti ${1000 + index}",
                isCanDelete = true

            )
        }
        val sampleList: List<NotificationItem> = listOf(
            NotificationItem.Notification(
                id = 202,
                groupId = -1,
                title = "You co len",
                content = "noti 202",
                isCanDelete = false
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

                        content = "noti 1",
                        isCanDelete = true
                    ),
                    NotificationItem.Notification(
                        id = 102,
                        groupId = 1,
                        title = "Cỏ dại và hoa dành dành",

                        content = "noti 2",
                        isCanDelete = true
                    ),
                    NotificationItem.Notification(
                        id = 103,
                        groupId = 1,
                        title = "Cỏ dại và hoa dành dành",

                        content = "noti 3",
                        isCanDelete = true
                    )
                ) + list
            ),

            // Notification lẻ
            NotificationItem.Notification(
                id = 201,
                groupId = -1,
                title = "You co len",
                content = "noti 4",
                isCanDelete = false

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

                        content = "noti 5",
                        isCanDelete = true
                    ),
                    NotificationItem.Notification(
                        id = 302,
                        groupId = 2,
                        title = "Test notification",

                        content = "noti 6",
                        isCanDelete = true
                    )
                )
            ),
        ) + moreNoti
        return sampleList
    }

}