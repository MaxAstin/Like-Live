package com.bunbeauty.fakelivestream.common.ui.components.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fakelivestream.common.ui.theme.FakeLiveStreamTheme
import com.bunbeauty.fakelivestream.common.ui.theme.FakeLiveTheme
import com.bunbeauty.fakelivestream.common.ui.util.rememberMultipleEventsCutter

@Composable
fun FakeLivePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val multipleEventsCutter = rememberMultipleEventsCutter()

    Button(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(containerColor = FakeLiveStreamTheme.colors.interactive),
        onClick = {
            multipleEventsCutter.processEvent(onClick)
        },
    ) {
        Text(
            text = text,
            color = FakeLiveStreamTheme.colors.onSurface,
            style = FakeLiveStreamTheme.typography.titleSmall,
        )
    }
}

@Preview
@Composable
private fun FakeLivePrimaryButtonPreview() {
    FakeLiveTheme {
        FakeLivePrimaryButton(
            text = "Button",
            onClick = {}
        )
    }
}