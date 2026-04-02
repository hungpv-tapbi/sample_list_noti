package com.example.testnoti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testnoti.model.NotificationItem
import com.example.testnoti.ui.theme.Black
import com.example.testnoti.ui.theme.TestNotiTheme
import com.example.testnoti.ui.theme.White

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestNotiTheme {
                Scaffold(modifier = Modifier
                    .background(color = White)
                    .fillMaxSize()) { innerPadding ->
                    AppContent()
                }
            }
        }
    }
}

@Composable
fun AppContent(
    modifier: Modifier = Modifier
) {
    val data by remember { mutableStateOf(getSampleData()) }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(color = White)
    ) {
        items(
            items = data,
            key = { k -> if (k is NotificationItem.Notification) k.id else (k as NotificationItem.GroupNotification).id }) {
            when (it) {
                is NotificationItem.GroupNotification -> {
                    GroupNotificationLayout(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth(),
                        it
                    )
                }

                is NotificationItem.Notification -> {
                    NotificationLayout(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth(),
                        it
                    )
                }
            }
        }

    }
}

@Composable
fun NotificationLayout(
    modifier: Modifier = Modifier,
    notification: NotificationItem.Notification
) {
    Box(
        modifier = modifier
            .padding(12.dp)
            .background(color = Color(notification.color))
    ) {

    }
}

@Composable
fun GroupNotificationLayout(
    modifier: Modifier = Modifier,
    group: NotificationItem.GroupNotification
) {
    Box(
        modifier = modifier
            .padding(12.dp)
            .background(color = Black)
    ) {

    }
}


fun getSampleData(): List<NotificationItem> {
    val sampleList: List<NotificationItem> = listOf(

        NotificationItem.GroupNotification(
            id = 1,
            notifications = listOf(
                NotificationItem.Notification(id = 101, groupId = 1, color = 0xFFE57373.toInt()),
                NotificationItem.Notification(id = 102, groupId = 1, color = 0xFF64B5F6.toInt()),
                NotificationItem.Notification(id = 103, groupId = 1, color = 0xFF81C784.toInt())
            )
        ),

        NotificationItem.Notification(
            id = 201,
            groupId = -1,
            color = 0xFFFFB74D.toInt()
        ),

        NotificationItem.GroupNotification(
            id = 2,
            notifications = listOf(
                NotificationItem.Notification(id = 301, groupId = 2, color = 0xFFBA68C8.toInt()),
                NotificationItem.Notification(id = 302, groupId = 2, color = 0xFF4DB6AC.toInt())
            )
        ),

        NotificationItem.Notification(
            id = 202,
            groupId = -1,
            color = 0xFFA1887F.toInt()
        )
    )
    return sampleList
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestNotiTheme {
        AppContent()
    }
}