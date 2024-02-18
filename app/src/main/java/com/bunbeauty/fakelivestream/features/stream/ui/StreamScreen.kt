package com.bunbeauty.fakelivestream.features.stream.ui

import androidx.annotation.DrawableRes
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.features.stream.presentation.CommentUi
import com.bunbeauty.fakelivestream.features.stream.presentation.StreamViewState
import com.bunbeauty.fakelivestream.features.stream.presentation.ViewersCount
import com.bunbeauty.fakelivestream.ui.blurTop
import com.bunbeauty.fakelivestream.ui.clickableWithoutIndication
import com.bunbeauty.fakelivestream.ui.components.CachedImage
import com.bunbeauty.fakelivestream.ui.components.ImageSource
import com.bunbeauty.fakelivestream.ui.theme.FakeLiveStreamTheme
import com.bunbeauty.fakelivestream.utils.getCameraProvider
import androidx.camera.core.Preview as CameraPreview

@Composable
fun StreamScreen(
    state: StreamViewState,
    navController: NavHostController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FakeLiveStreamTheme.colors.surface)
            .padding(vertical = 24.dp)
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            var isFrontCamera by remember {
                mutableStateOf(true)
            }

            Camera(isFrontCamera = isFrontCamera)
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp, bottom = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.TopEnd),
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AvatarImage(image = state.image)
                            UsernameRow(
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .weight(1f),
                                username = state.username
                            )

                            LiveCard(modifier = Modifier.padding(start = 12.dp))
                            ViewersCard(
                                modifier = Modifier.padding(start = 8.dp),
                                viewersCount = state.viewersCount
                            )
                            ActionIcon(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .clickableWithoutIndication(
                                        onClick = {
                                            navController.popBackStack()
                                        }
                                    ),
                                iconResId = R.drawable.ic_close,
                                contentDescription = "Close",
                            )
                        }
                        Actions(
                            modifier = Modifier.padding(top = 16.dp),
                            onSwitchClick = {
                                isFrontCamera = !isFrontCamera
                            }
                        )
                    }
                    Comments(
                        modifier = Modifier.align(Alignment.BottomStart),
                        comments = state.comments,
                    )
                }
            }

            repeat(state.reactionCount) { i ->
                AnimatedReaction(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp),
                    order = i
                )
            }
        }
        BottomPanel()
    }
}

@Composable
private fun Camera(
    isFrontCamera: Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val previewView = remember { PreviewView(context) }

    LaunchedEffect(isFrontCamera) {
        val cameraProvider = context.getCameraProvider()
        val lensFacing = if (isFrontCamera) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
        val preview = CameraPreview.Builder().build()
        val imageCapture = ImageCapture.Builder().build()

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    AndroidView(
        factory = { previewView },
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(8.dp))
    )
}

@Composable
private fun AvatarImage(
    image: ImageSource<*>,
    modifier: Modifier = Modifier,
) {
    CachedImage(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape),
        imageSource = image,
        cacheKey = image.data.toString(),
        contentDescription = "Avatar"
    )
}

@Composable
private fun UsernameRow(
    username: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f, false),
            text = username,
            color = FakeLiveStreamTheme.colors.onSurface,
            style = FakeLiveStreamTheme.typography.title,
            overflow = Ellipsis,
            maxLines = 1,
        )
        Icon(
            modifier = Modifier
                .padding(start = 4.dp)
                .size(16.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_drop_down),
            contentDescription = "Dropdown",
            tint = FakeLiveStreamTheme.colors.icon,
        )
    }
}

@Composable
private fun LiveCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(FakeLiveStreamTheme.colors.instagram.accent)
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(R.string.stream_live),
            style = FakeLiveStreamTheme.typography.bodySmall,
            color = FakeLiveStreamTheme.colors.onSurface
        )
    }
}

@Composable
private fun ViewersCard(
    viewersCount: ViewersCount,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(FakeLiveStreamTheme.colors.surface.copy(alpha = 0.5f))
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(12.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_eye),
                contentDescription = "Viewers",
                tint = FakeLiveStreamTheme.colors.icon,
            )
            val viewersCountText = when (viewersCount) {
                is ViewersCount.UpToThousand -> viewersCount.count
                is ViewersCount.Thousands -> stringResource(
                    R.string.stream_thousands_viewers_count,
                    viewersCount.thousands,
                    viewersCount.hundreds
                )
            }
            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = viewersCountText,
                style = FakeLiveStreamTheme.typography.bodySmall,
                color = FakeLiveStreamTheme.colors.onSurface
            )
        }
    }
}

@Composable
private fun Actions(
    modifier: Modifier = Modifier,
    onSwitchClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = spacedBy(16.dp)
    ) {
        var isMicMuted by remember {
            mutableStateOf(false)
        }

        ActionIcon(
            modifier = Modifier.clickableWithoutIndication(
                onClick = {
                    isMicMuted = !isMicMuted
                }
            ),
            iconResId = if (isMicMuted) {
                R.drawable.ic_mic_crossed_out
            } else {
                R.drawable.ic_mic
            },
            contentDescription = "Mic",
        )
        ActionIcon(
            iconResId = R.drawable.ic_camera,
            contentDescription = "Camera",
        )
        ActionIcon(
            modifier = Modifier.clickableWithoutIndication(
                onClick = onSwitchClick
            ),
            iconResId = R.drawable.ic_switch,
            contentDescription = "Switch camera",
        )
        ActionIcon(
            iconResId = R.drawable.ic_effect,
            contentDescription = "Effect",
        )
    }
}

@Composable
private fun Comments(
    modifier: Modifier = Modifier,
    comments: List<CommentUi>,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .height(224.dp)
            .blurTop(),
        verticalArrangement = spacedBy(16.dp, Alignment.Bottom),
        reverseLayout = true
    ) {
        items(comments) { comment ->
            CommentItem(comment)
        }
    }
}

@Composable
private fun CommentItem(
    comment: CommentUi,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CachedImage(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            imageSource = comment.picture,
            cacheKey = comment.username,
            contentDescription = "Comment avatar",
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = comment.username,
                color = FakeLiveStreamTheme.colors.onSurface,
                style = FakeLiveStreamTheme.typography.title,
            )
            Text(
                text = comment.text,
                color = FakeLiveStreamTheme.colors.onSurface,
                style = FakeLiveStreamTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun BottomPanel(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(FakeLiveStreamTheme.colors.surface)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = FakeLiveStreamTheme.colors.border,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.stream_add_comment),
                style = FakeLiveStreamTheme.typography.bodyMedium,
                color = FakeLiveStreamTheme.colors.onSurface,
                overflow = Ellipsis,
                maxLines = 1,
            )
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_options),
                contentDescription = "Options",
                tint = FakeLiveStreamTheme.colors.icon,
            )
        }
        ActionIcon(
            iconResId = R.drawable.ic_camera_plus,
            contentDescription = "Add camera",
        )
        ActionIcon(
            iconResId = R.drawable.ic_invite,
            contentDescription = "Invite",
        )
        ActionIcon(
            iconResId = R.drawable.ic_question,
            contentDescription = "Question",
        )
        ActionIcon(
            iconResId = R.drawable.ic_direct,
            contentDescription = "Direct",
        )
    }

}

@Composable
private fun ActionIcon(
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int,
    contentDescription: String,
) {
    Icon(
        modifier = modifier.size(28.dp),
        imageVector = ImageVector.vectorResource(iconResId),
        contentDescription = contentDescription,
        tint = FakeLiveStreamTheme.colors.icon,
    )
}

@Preview(locale = "en")
@Preview(locale = "ru")
@Composable
private fun StreamScreenPreview() {
    FakeLiveStreamTheme {
        StreamScreen(
            state = StreamViewState(
                image = ImageSource.Res(R.drawable.img_default_avatar),
                username = "long_user_name",
                viewersCount = ViewersCount.Thousands(
                    thousands = "10",
                    hundreds = "4",
                ),
                comments = listOf(
                    CommentUi(
                        picture = ImageSource.Res(R.drawable.img_default_avatar),
                        username = "username1",
                        text = "Text 1",
                    ),
                    CommentUi(
                        picture = ImageSource.Res(R.drawable.img_default_avatar),
                        username = "username2",
                        text = "Text 2",
                    ),
                    CommentUi(
                        picture = ImageSource.Res(R.drawable.img_default_avatar),
                        username = "username3",
                        text = "Text 3",
                    ),
                ),
                reactionCount = 10
            ),
            navController = rememberNavController(),
        )
    }
}