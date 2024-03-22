package com.bunbeauty.fakelivestream.features.main.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.common.ui.LocalePreview
import com.bunbeauty.fakelivestream.common.ui.components.button.PrimaryButton
import com.bunbeauty.fakelivestream.common.ui.components.button.SecondaryButton
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
            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var initialChecked by remember {
                    mutableStateOf(false)
                }
                Checkbox(
                    checked = initialChecked,
                    colors = CheckboxDefaults.colors(
                        checkedColor = FakeLiveStreamTheme.colors.interactive
                    ),
                    onCheckedChange = { checked ->
                        initialChecked = checked
                        onAction(
                            Preparation.Action.NotShowFeedbackChecked(
                                checked = checked
                            )
                        )
                    }
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.do_not_ask),
                    color = FakeLiveStreamTheme.colors.onBackground,
                    style = FakeLiveStreamTheme.typography.bodyMedium,
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.End)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.not_now),
                    onClick = {
                        onAction(Preparation.Action.CloseFeedbackDialogClick)
                    },
                )
                PrimaryButton(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(R.string.give_feedback),
                    onClick = {
                        onAction(Preparation.Action.GiveFeedbackClick)
                    },
                )
            }
        }
    }
}

@LocalePreview
@Composable
private fun FeedbackDialogPreview() {
    FeedbackDialog(
        onAction = {}
    )
}