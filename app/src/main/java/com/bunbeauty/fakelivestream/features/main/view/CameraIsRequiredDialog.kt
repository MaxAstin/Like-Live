package com.bunbeauty.fakelivestream.features.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.common.ui.LocalePreview
import com.bunbeauty.fakelivestream.common.ui.components.button.SecondaryButton
import com.bunbeauty.fakelivestream.common.ui.theme.FakeLiveStreamTheme
import com.bunbeauty.fakelivestream.features.main.presentation.Main

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraIsRequiredDialog(
    onAction: (Main.Action) -> Unit,
    onSettingsClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            onAction(Main.Action.CloseCameraRequiredDialogClick)
        }
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(FakeLiveStreamTheme.colors.background)
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.required_camera_permisseon_title,
                    stringResource(R.string.app_name)
                ),
                color = FakeLiveStreamTheme.colors.onBackground,
                style = FakeLiveStreamTheme.typography.titleMedium,
            )
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.required_camera_permisseon_body),
                color = FakeLiveStreamTheme.colors.onBackground,
                style = FakeLiveStreamTheme.typography.bodyMedium,
            )
            SecondaryButton(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.End),
                text = stringResource(R.string.settings),
                onClick = onSettingsClick,
            )
        }
    }
}

@LocalePreview
@Composable
private fun CameraIsRequiredDialogPreview() {
    FakeLiveStreamTheme {
        CameraIsRequiredDialog(
            onAction = {},
            onSettingsClick = {}
        )
    }
}