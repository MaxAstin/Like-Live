package com.bunbeauty.fakelivestream.common.ui.components.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bunbeauty.fakelivestream.common.ui.theme.FakeLiveStreamTheme

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(containerColor = FakeLiveStreamTheme.colors.interactive),
        onClick = onClick,
    ) {
        Text(
            text = text,
            color = FakeLiveStreamTheme.colors.onSurface,
            style = FakeLiveStreamTheme.typography.titleSmall,
        )
    }
}