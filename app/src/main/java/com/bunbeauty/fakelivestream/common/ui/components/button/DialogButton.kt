package com.bunbeauty.fakelivestream.common.ui.components.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bunbeauty.fakelivestream.common.ui.theme.FakeLiveStreamTheme

@Composable
fun DialogButton(
    text: String,
    background: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconId: Int? = null,
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(containerColor = background),
        onClick = onClick,
    ) {
        Text(
            text = text,
            color = FakeLiveStreamTheme.colors.onSurface,
            style = FakeLiveStreamTheme.typography.titleSmall,
        )
        if (iconId != null) {
            Image(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(16.dp),
                painter = painterResource(iconId),
                contentDescription = "icon"
            )
        }
    }
}