package com.example.testnoti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.testnoti.model.NotificationItem
import com.example.testnoti.ui.theme.TestNotiTheme
import com.example.testnoti.ui.theme.White
import com.example.testnoti.utils.DataProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestNotiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppContent(
    modifier: Modifier = Modifier,
) {
    var data by remember { mutableStateOf(DataProvider.getSampleData()) }

    LazyColumn(
        modifier =
            modifier
    ) {
        itemsIndexed(
            items = data,
            key = { _, k ->
                when (k) {
                    is NotificationItem.Notification -> "notif_${k.id}"
                    is NotificationItem.Group -> "group_${k.id}"
                }
            }
        ) { _, it ->
            when (it) {
                is NotificationItem.Group -> {
                    GroupLayout(
                        modifier = Modifier.animateItem(

                        ),
                        group = it,
                        onExpanded = {
                            val lst = data.toMutableList()
                            val currentIdx = lst.indexOfFirst { item ->
                                item is NotificationItem.Group && item.id == it.id
                            }
                            if (currentIdx == -1) return@GroupLayout
                            val current = lst[currentIdx] as NotificationItem.Group
                            if (current.isExpanded) return@GroupLayout
                            lst[currentIdx] = current.copy(isExpanded = true)
                            lst.addAll(currentIdx + 1, current.notifications)
                            data = lst
                        },
                        onCollapse = { groupId ->
                            val lst = data.toMutableList()
                            val filtered = lst.filterNot { item ->
                                item is NotificationItem.Notification && item.groupId == groupId
                            }.toMutableList()
                            val index = filtered.indexOfFirst { item ->
                                item is NotificationItem.Group && item.id == groupId
                            }
                            if (index != -1) {
                                val group = filtered[index] as NotificationItem.Group
                                filtered[index] = group.copy(isExpanded = false)
                            }
                            data = filtered
                        }
                    )
                }

                is NotificationItem.Notification -> {
                    NotificationLayout(notification = it)
                }
            }
        }
    }
}

@Composable
fun GroupLayout(
    modifier: Modifier = Modifier,
    group: NotificationItem.Group,
    onExpanded: () -> Unit,
    onCollapse: (Int) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(if(group.isExpanded) 0.dp else 100.dp)
            .clickable {
                if (group.isExpanded) onCollapse(group.id) else onExpanded()
            }
    ) {
        if (!group.isExpanded) {
            group.notifications.take(3).forEachIndexed { index, item ->
                NotificationLayout(
                    notification = item,
                    modifier = Modifier
                        .offset(y = (index * 6).dp)
                        .scale(1f - index * 0.05f)
                        .zIndex((10 - index).toFloat())
                )
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
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color(notification.color))
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = notification.content,
            color = White,
            fontSize = 16.sp
        )
    }
}

@Composable
fun NotificationItemAnimated(
    notification: NotificationItem.Notification
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        NotificationLayout(notification = notification)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestNotiTheme {
        AppContent()
    }
}