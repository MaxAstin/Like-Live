package com.bunbeauty.fakelivestream.common.ui.components.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fakelivestream.common.ui.theme.FakeLiveStreamTheme

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        onClick = onClick,
    ) {
        Text(
            text = text,
            color = FakeLiveStreamTheme.colors.onSurfaceVariant,
            style = FakeLiveStreamTheme.typography.titleSmall,
        )
    }
}

@Preview
@Composable
private fun SecondaryButtonPreview() {
    FakeLiveStreamTheme {
        SecondaryButton(
            text = "Button",
            onClick = {},
        )
    }
}