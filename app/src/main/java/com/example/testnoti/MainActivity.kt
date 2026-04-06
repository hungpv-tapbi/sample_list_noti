package com.example.testnoti

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.testnoti.model.DragAnchors
import com.example.testnoti.model.NotificationItem
import com.example.testnoti.model.UiItem
import com.example.testnoti.ui.theme.TestNotiTheme
import com.example.testnoti.ui.theme.White
import com.example.testnoti.utils.DataProvider
import com.example.testnoti.utils.extensions.toUiList
import com.example.testnoti.utils.extensions.updateGroup
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.lens

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestNotiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppContent(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun AppContent(
    modifier: Modifier = Modifier
) {
    var data by remember { mutableStateOf(DataProvider.getSampleData()) }
    val backdrop = rememberLayerBackdrop()
    val uiList = remember(data) {
        data.toUiList()
    }
    val lazyListState = rememberLazyListState()


    Box(modifier = modifier) {

        Image(
            painter = painterResource(R.drawable.img),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .layerBackdrop(backdrop),
            contentScale = ContentScale.Crop
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(), state = lazyListState
        ) {
            itemsIndexed(
                items = uiList, key = { _, k ->
                    when (k) {
                        is UiItem.GroupHeader -> "group_${k.group.id}"
                        is UiItem.Child -> "notif_${k.notification.id}"
                        is UiItem.Single -> "notif_${k.notification.id}"
                    }
                }) { _, it ->
                when (it) {
                    is UiItem.GroupHeader -> {
                        GroupLayout(
                            modifier = Modifier,
                            group = it.group,
                            backdrop = backdrop,
                            onExpanded = { id -> data = data.updateGroup(id, true) },
                            onCollapse = { id -> data = data.updateGroup(id, false) })
                    }

                    is UiItem.Child -> {
                        Log.d("pvhung", "child")
                        NotificationLayout(
                            notification = it.notification,
                            isVisible = it.notification.isVisible,
                            backdrop = backdrop
                        )
                    }

                    is UiItem.Single -> {
                        NotificationLayout(
                            notification = it.notification, isVisible = true, backdrop = backdrop
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GroupLayout(
    modifier: Modifier = Modifier,
    group: NotificationItem.Group,
    backdrop: LayerBackdrop,
    onExpanded: (Int) -> Unit,
    onCollapse: (Int) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        AnimatedVisibility(
            visible = group.isExpanded, enter = expandVertically(
                expandFrom = Alignment.Top
            ) + fadeIn(initialAlpha = 0.2f, animationSpec = tween(200)), exit = shrinkVertically(
                shrinkTowards = Alignment.Bottom, animationSpec = tween(200)
            ) + fadeOut(
                targetAlpha = 0f, animationSpec = tween(200)
            )
        ) {
            Row(
                modifier = Modifier.animateContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = group.appName, color = White, modifier = Modifier.weight(1f))

                Button(onClick = {
                    Log.d("pvhung", "onCollapse")
                    onCollapse(group.id)
                }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        tint = White
                    )

                    Text(text = "Show less", color = White)
                }

                Icon(
                    imageVector = Icons.Default.Close, contentDescription = null, tint = White
                )


            }
        }


        if (!group.isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)


            ) {
                group.notifications.take(3).forEachIndexed { index, item ->
                    NotificationLayout(
                        notification = item,
                        modifier = Modifier
                            .offset(y = (index * 6).dp)
                            .scale(1f - index * 0.05f)
                            .zIndex((10 - index).toFloat()),
                        isVisible = true,
                        backdrop = backdrop
                    )
                }

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .zIndex(100f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            Log.d("pvhung", "before onExpanded")
                            if (!group.isExpanded) {
                                Log.d("pvhung", "onExpanded")
                                onExpanded(group.id)
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun NotificationLayout(
    modifier: Modifier = Modifier,
    notification: NotificationItem.Notification,
    isVisible: Boolean,
    backdrop: LayerBackdrop
) {
    val enterTransition = expandVertically() + scaleIn() + fadeIn()
    val exit = slideOutVertically() + shrinkVertically() + scaleOut()

    if (notification.index != 0) {
        AnimatedVisibility(
            visible = isVisible, exit = exit, enter = enterTransition, modifier = modifier

        ) {
            NotificationContent(notification = notification, backdrop = backdrop)
        }
    } else if (isVisible) {
        NotificationContent(notification = notification, backdrop = backdrop)

    }


//    if (notification.index < 15) {

//

//    } else {
//        if (isVisible) {
//            NotificationContent(notification = notification, backdrop = backdrop)
//        }
//    }
}


@Composable
fun NotificationContent(
    notification: NotificationItem.Notification,
    backdrop: LayerBackdrop
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current
    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.weight(1f))

            Text(
                text = "View",
                color = White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { RoundedCornerShape(16.dp) },
                        effects = { lens(16f.dp.toPx(), 32f.dp.toPx()) }
                    )
                    .wrapContentHeight(Alignment.CenterVertically)
                    .padding(horizontal = 12.dp)
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = "Delete",
                color = White,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxHeight()
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { RoundedCornerShape(16.dp) },
                        effects = { lens(16f.dp.toPx(), 32f.dp.toPx()) }
                    )
                    .wrapContentHeight(Alignment.CenterVertically) // Căn giữa chữ trong Text
                    .padding(horizontal = 12.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { RoundedCornerShape(16.dp) },
                    effects = { lens(16f.dp.toPx(), 32f.dp.toPx()) }
                )
                .clickable { /* Handle click */ }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_youtube),
                    contentDescription = null,
                )

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notification.title,
                        color = White,
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                    Text(
                        text = notification.content,
                        color = White,
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }

                Text(
                    text = "1 hour ago",
                    color = White,
                    fontSize = 12.sp,
                    modifier = Modifier.alpha(0.7f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestNotiTheme {
        AppContent(

        )
    }
}