package com.bunbeauty.fakelivestream.features.preparation.view

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.features.domain.model.ViewerCount
import com.bunbeauty.fakelivestream.features.preparation.presentation.Preparation
import com.bunbeauty.fakelivestream.ui.LocalePreview
import com.bunbeauty.fakelivestream.ui.components.CachedImage
import com.bunbeauty.fakelivestream.ui.components.FakeLiveTextField
import com.bunbeauty.fakelivestream.ui.components.GradientButton
import com.bunbeauty.fakelivestream.ui.components.ImageSource
import com.bunbeauty.fakelivestream.ui.noEffectClickable
import com.bunbeauty.fakelivestream.ui.rippleClickable
import com.bunbeauty.fakelivestream.ui.theme.FakeLiveStreamTheme

@Composable
fun PreparationScreen(
    state: Preparation.State,
    onAction: (Preparation.Action) -> Unit
) {
    val contentResolver = LocalContext.current.contentResolver
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                contentResolver.takePersistableUriPermission(uri, takeFlags)

                onAction(Preparation.Action.ImageSelect(uri = uri))
            }
        }
    )

    fun launchImagePicker() {
        pickImageLauncher.launch(arrayOf("image/*"))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FakeLiveStreamTheme.colors.background)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            CachedImage(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(80.dp)
                    .clip(CircleShape)
                    .noEffectClickable {
                        launchImagePicker()
                    },
                imageSource = state.image,
                cacheKey = state.image.data.toString(),
                contentDescription = "Avatar",
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .noEffectClickable {
                        launchImagePicker()
                    }
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.preparation_edit_photo),
                    color = FakeLiveStreamTheme.colors.interactive,
                    style = FakeLiveStreamTheme.typography.titleSmall,
                )
            }

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(R.string.preparation_username),
                color = FakeLiveStreamTheme.colors.onSurfaceVariant,
                style = FakeLiveStreamTheme.typography.bodyMedium,
            )
            FakeLiveTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.username,
                hint = stringResource(R.string.preparation_username_hint),
                readOnly = false,
                onValueChange = { username ->
                    onAction(Preparation.Action.UsernameUpdate(username = username))
                },
            )

            var menuExpanded by remember {
                mutableStateOf(false)
            }
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(R.string.preparation_viewer_count),
                color = FakeLiveStreamTheme.colors.onSurfaceVariant,
                style = FakeLiveStreamTheme.typography.bodyMedium,
            )
            Box {
                FakeLiveTextField(
                    modifier = Modifier.rippleClickable {
                        menuExpanded = true
                    },
                    value = state.viewerCount.text,
                    hint = "",
                    readOnly = true,
                    onValueChange = {},
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                            contentDescription = "Arrow",
                            tint = FakeLiveStreamTheme.colors.iconVariant,
                        )
                    }
                )
                ViewersDropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = {
                        menuExpanded = false
                    },
                    onItemClick = { viewerCount ->
                        onAction(Preparation.Action.ViewerCountSelect(viewerCount = viewerCount))
                        menuExpanded = false
                    }
                )
            }
        }

        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            brush = Brush.linearGradient(
                colors = listOf(
                    FakeLiveStreamTheme.colors.instagram.logo1,
                    FakeLiveStreamTheme.colors.instagram.logo2,
                    FakeLiveStreamTheme.colors.instagram.logo3,
                ),
                start = Offset(Float.POSITIVE_INFINITY, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY),
            ),
            shape = RoundedCornerShape(6.dp),
            onClick = {
                onAction(Preparation.Action.StartStreamClick)
            }
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(12.dp),
                text = stringResource(R.string.preparation_start_live),
                color = FakeLiveStreamTheme.colors.onSurface,
                style = FakeLiveStreamTheme.typography.titleSmall,
            )
        }
    }
}

@LocalePreview
@Composable
private fun PreparationStreamPreview() {
    FakeLiveStreamTheme {
        PreparationScreen(
            state = Preparation.State(
                image = ImageSource.Res(R.drawable.img_default_avatar),
                username = "",
                viewerCount = ViewerCount.V_100_200
            ),
            onAction = {}
        )
    }
}