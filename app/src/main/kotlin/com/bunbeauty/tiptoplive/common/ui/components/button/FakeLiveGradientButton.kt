package com.bunbeauty.tiptoplive.common.ui.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.util.rememberMultipleEventsCutter

@Composable
fun FakeLiveGradientButton(
    brush: Brush,
    shape: Shape,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val multipleEventsCutter = rememberMultipleEventsCutter()

    Button(
        modifier = modifier,
        shape = shape,
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        onClick = {
            multipleEventsCutter.processEvent(onClick)
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(brush)
                .then(modifier)
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun FakeLiveGradientButtonPreview() {
    FakeLiveTheme {
        FakeLiveGradientButton(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.Blue,
                    Color.Cyan,
                )
            ),
            shape = RectangleShape,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
        ) {
            Text(text = "Text")
        }
    }
}