package com.bunbeauty.fakelivestream.common.ui.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bunbeauty.fakelivestream.common.ui.theme.FakeLiveStreamTheme

object FakeLiveBottomSheetDefaults {

    val shape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomEnd = 0.dp,
        bottomStart = 0.dp
    )

    @Composable
    fun DragHandle() {
        Spacer(
            modifier = Modifier
                .padding(12.dp)
                .width(36.dp)
                .height(4.dp)
                .background(
                    color = FakeLiveStreamTheme.colors.border,
                    shape = RoundedCornerShape(size = 2.5.dp)
                )
        )
    }

}