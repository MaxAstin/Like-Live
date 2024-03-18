package com.bunbeauty.fakelivestream.features.main.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.common.ui.LocalePreview
import com.bunbeauty.fakelivestream.common.ui.theme.FakeLiveStreamTheme
import com.bunbeauty.fakelivestream.features.preparation.presentation.Preparation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackDialog(
    onAction: (Preparation.Action) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onAction(Preparation.Action.CloseFeedbackDialogClick)
        }
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(FakeLiveStreamTheme.colors.background)
                .padding(24.dp)
        ) {
            Image(
                modifier = Modifier
                    .height(80.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.shy_emoji),
                contentDescription = "feedback emoji"
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(R.string.feedback),
                color = FakeLiveStreamTheme.colors.onBackground,
                style = FakeLiveStreamTheme.typography.titleMedium,
            )
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                text = stringResource(
                    R.string.help_up_improve,
                    stringResource(R.string.app_name)
                ),
                color = FakeLiveStreamTheme.colors.onBackground,
                style = FakeLiveStreamTheme.typography.bodyMedium,
            )
            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.End),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FakeLiveStreamTheme.colors.interactive),
                onClick = {
                    onAction(Preparation.Action.GiveFeedbackClick)
                },
            ) {
                Text(
                    text = stringResource(R.string.give_feedback),
                    color = FakeLiveStreamTheme.colors.onSurface,
                    style = FakeLiveStreamTheme.typography.titleSmall,
                )
            }
        }
    }
}

@LocalePreview
@Composable
private fun FeedbackDialogPreview() {
    FeedbackDialog(onAction = {})
}