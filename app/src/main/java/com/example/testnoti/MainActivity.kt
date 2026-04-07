package com.example.testnoti

import android.os.Bundle
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
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
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
import kotlin.math.abs
import kotlin.math.roundToInt

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
                            onCollapse = { id -> data = data.updateGroup(id, false) },
                            onDeleteNotification = { noti ->
                                data = data.filterNot { item ->
                                    (item is NotificationItem.Notification && item.id == noti.id)
                                }
                            }
                        )

                    }

                    is UiItem.Child -> {
                        NotificationLayout(
                            notification = it.notification,
                            isVisible = it.notification.isVisible,
                            backdrop = backdrop,
                            onDelete = {
                                data = data.filterNot { item ->
                                    (item is NotificationItem.Notification && item.id == it.notification.id)
                                }
                            }
                        )
                    }

                    is UiItem.Single -> {
                        NotificationLayout(
                            notification = it.notification, isVisible = true, backdrop = backdrop,
                            onDelete = {
                                data = data.filterNot { item ->
                                    (item is NotificationItem.Notification && item.id == it.notification.id)
                                }
                            }
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
    onCollapse: (Int) -> Unit,
    onDeleteNotification: (NotificationItem.Notification) -> Unit
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
                        backdrop = backdrop,
                        onDelete = {
                            onDeleteNotification(item)
                        }
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
                            if (!group.isExpanded) {
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
    backdrop: LayerBackdrop,
    onDelete: () -> Unit
) {
    val enterTransition = expandVertically() + scaleIn() + fadeIn()
    val exit = slideOutVertically() + shrinkVertically() + scaleOut()

    if (notification.index != 0) {
        AnimatedVisibility(
            visible = isVisible, exit = exit, enter = enterTransition, modifier = modifier

        ) {
            NotificationContent(
                notification = notification,
                backdrop = backdrop,
                isCanDelete = notification.isCanDelete,
                onDelete = {
                    onDelete()
                })
        }
    } else if (isVisible) {
        NotificationContent(
            notification = notification,
            backdrop = backdrop,
            isCanDelete = notification.isCanDelete,
            onDelete = {
                onDelete()
            })

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
    backdrop: LayerBackdrop,
    isCanDelete: Boolean,
    onDelete: (NotificationItem.Notification) -> Unit
) {
    val density = LocalDensity.current
    val screenWidthPx = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val screenWidthDp = with(density) {
        LocalWindowInfo.current.containerSize.width.toDp()
    }
    var isDeleted by remember { mutableStateOf(false) }
    var actionsWidth by remember { mutableFloatStateOf(0f) }
    val interactionSource = remember { MutableInteractionSource() }
    var isReleased by remember { mutableStateOf(false) }
    val commitThreshold by remember {
        derivedStateOf {
            actionsWidth * 1.2f
        }
    }
    val anchors = remember(actionsWidth) {
        if (actionsWidth == 0f) return@remember DraggableAnchors {
            DragAnchors.Start at 0f
            DragAnchors.End at -500f
        }

        DraggableAnchors {
            DragAnchors.Start at 0f
            DragAnchors.Middle at -actionsWidth
            if (isCanDelete) {
                DragAnchors.End at -screenWidthPx
            }
        }
    }

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            anchors = anchors
        )
    }

    LaunchedEffect(anchors) {
        if (anchors.size > 0) {
            state.updateAnchors(anchors)
        }
    }


    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is DragInteraction.Start -> {
                    isReleased = false
                }

                is DragInteraction.Stop,
                is DragInteraction.Cancel -> {
                    isReleased = true
                }
            }
        }
    }

    LaunchedEffect(state) {
        snapshotFlow { state.targetValue to isReleased }
            .collect { (target, released) ->
                if (released && target == DragAnchors.End && !isDeleted && isCanDelete) {
                    isDeleted = true
                    onDelete(notification)
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(8.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Spacer(Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxHeight()
                    .width((screenWidthDp / if (isCanDelete) 2 else 4))
                    .graphicsLayer {
                        val offset = abs(state.requireOffset())
                        scaleX = if (offset > actionsWidth) {
                            1f + (offset - actionsWidth) / actionsWidth
                        } else 1f
                        transformOrigin = TransformOrigin(1f, 0.5f)
                    }
                    .onSizeChanged { size ->
                        actionsWidth = size.width.toFloat() + 24f
                    }
            ) {
                if (abs(state.requireOffset()) < commitThreshold || !isCanDelete) {
                    Text(
                        text = "View",
                        color = White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .drawBackdrop(
                                backdrop = backdrop,
                                shape = { RoundedCornerShape(16.dp) },
                                effects = { lens(16f.dp.toPx(), 32f.dp.toPx()) }
                            )
                            .wrapContentHeight(Alignment.CenterVertically)
                            .padding(horizontal = 12.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                }

                if (isCanDelete) {
                    Text(
                        text = "Delete",
                        color = White,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .drawBackdrop(
                                backdrop = backdrop,
                                shape = { RoundedCornerShape(16.dp) },
                                effects = { lens(16f.dp.toPx(), 32f.dp.toPx()) }
                            )
                            .clickable {
                                onDelete(notification)
                            }
                            .wrapContentHeight(Alignment.CenterVertically)
                            .padding(horizontal = 12.dp)
                    )
                }
            }

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset {
                    IntOffset(state.requireOffset().roundToInt(), 0)
                }
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Horizontal,
                    flingBehavior = AnchoredDraggableDefaults.flingBehavior(state),
                    interactionSource = interactionSource
                )
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { RoundedCornerShape(16.dp) },
                    effects = { lens(16f.dp.toPx(), 32f.dp.toPx()) }
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_youtube),
                    contentDescription = null,
                    modifier = Modifier
                        .width(32.dp)
                        .height(32.dp)
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