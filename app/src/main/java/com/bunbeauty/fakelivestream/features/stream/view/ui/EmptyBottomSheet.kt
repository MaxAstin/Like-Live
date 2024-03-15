package com.bunbeauty.fakelivestream.features.stream.view.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.ui.LocalePreview
import com.bunbeauty.fakelivestream.ui.components.bottomsheet.FakeLiveBottomSheet
import com.bunbeauty.fakelivestream.ui.theme.FakeLiveStreamTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmptyBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    @StringRes titleResId: Int,
    @StringRes bodyResId: Int,
    @StringRes descriptionResId: Int,
) {
    FakeLiveBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest
    ) {
        Content(
            titleResId = titleResId,
            bodyResId = bodyResId,
            descriptionResId = descriptionResId,
        )
    }
}

@Composable
private fun ColumnScope.Content(
    @StringRes titleResId: Int,
    @StringRes bodyResId: Int,
    @StringRes descriptionResId: Int,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp,
                bottom = 12.dp
            ),
        text = stringResource(titleResId),
        color = FakeLiveStreamTheme.colors.onSurface,
        style = FakeLiveStreamTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )
    Divider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 0.5.dp,
        color = FakeLiveStreamTheme.colors.border
    )
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            modifier = Modifier
                .padding(top = 100.dp)
                .fillMaxWidth(),
            text = stringResource(bodyResId),
            color = FakeLiveStreamTheme.colors.onSurface,
            style = FakeLiveStreamTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    bottom = 100.dp
                )
                .fillMaxWidth(),
            text = stringResource(descriptionResId),
            color = FakeLiveStreamTheme.colors.onSurfaceVariant,
            style = FakeLiveStreamTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@LocalePreview
@Composable
fun EmptyBottomSheetPreview() {
    FakeLiveStreamTheme {
        Column {
            Content(
                titleResId = R.string.stream_questions_title,
                bodyResId = R.string.stream_questions_body,
                descriptionResId = R.string.stream_questions_description,
            )
        }
    }
}