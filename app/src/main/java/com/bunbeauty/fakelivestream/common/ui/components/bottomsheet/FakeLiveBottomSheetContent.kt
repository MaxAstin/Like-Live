package com.bunbeauty.fakelivestream.common.ui.components.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bunbeauty.fakelivestream.common.ui.theme.FakeLiveStreamTheme

@Composable
fun ColumnScope.FakeLiveBottomSheetContent(
    title: String,
    titleColor: Color = FakeLiveStreamTheme.colors.onSurface,
    dividerColor: Color = FakeLiveStreamTheme.colors.border,
    content: @Composable () -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp,
                bottom = 12.dp
            ),
        text = title,
        color = titleColor,
        style = FakeLiveStreamTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 0.5.dp,
        color = dividerColor
    )
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        content()
    }
}