package com.bunbeauty.tiptoplive.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.clickableWithoutIndication(
    onClick: () -> Unit,
) = composed {
    Modifier.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}

fun Modifier.blurTop() = drawWithContent {
    val colors = listOf(
        Color.Transparent,
        Color.Black,
        Color.Black,
    )
    drawContent()
    drawRect(
        brush = Brush.verticalGradient(colors),
        blendMode = BlendMode.DstIn
    )
}

fun Modifier.rippleClickable(
    onClick: () -> Unit
) = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(),
        onClick = onClick
    )
}

fun Modifier.noEffectClickable(
    onClick: () -> Unit
) = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}