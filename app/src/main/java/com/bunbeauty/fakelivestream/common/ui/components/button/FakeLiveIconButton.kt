package com.bunbeauty.fakelivestream.common.ui.components.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.common.ui.theme.FakeLiveStreamTheme
import com.bunbeauty.fakelivestream.common.ui.util.rememberMultipleEventsCutter

@Composable
fun FakeLiveIconButton(
    @DrawableRes iconId: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hasMarker: Boolean = false,
) {
    val multipleEventsCutter = rememberMultipleEventsCutter()

    IconButton(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .border(
                width = 1.dp,
                color = FakeLiveStreamTheme.colors.interactive,
                shape = RoundedCornerShape(6.dp)
            )
            .size(48.dp),
        onClick = {
            multipleEventsCutter.processEvent(onClick)
        }
    ) {
        Box {
            Icon(
                modifier = Modifier
                    .padding(2.dp)
                    .size(24.dp),
                painter = painterResource(iconId),
                tint = FakeLiveStreamTheme.colors.interactive,
                contentDescription = contentDescription
            )
            if (hasMarker) {
                Badge(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .border(
                            width = 1.dp,
                            color = FakeLiveStreamTheme.colors.background,
                            shape = CircleShape
                        )
                        .padding(1.dp),
                    containerColor = FakeLiveStreamTheme.colors.important,
                )
            }
        }
    }
}

@Preview
@Composable
private fun FakeLiveIconButtonPreview() {
    FakeLiveIconButton(
        iconId = R.drawable.ic_share,
        contentDescription = "",
        onClick = {},
        hasMarker = true,
    )
}