package com.bunbeauty.fakelivestream.features.stream.view.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.common.ui.theme.FakeLiveStreamTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun AnimatedReaction(
    modifier: Modifier = Modifier,
    order: Int
) {
    val minXAmplitude = with(LocalDensity.current) { 4.dp.toPx().roundToInt() }
    val maxXAmplitude = with(LocalDensity.current) { 16.dp.toPx().roundToInt() }
    val xOffsetAnimation = remember { Animatable(0f) }
    val yOffsetAnimation = remember { Animatable(0f) }
    val alphaAnimation = remember { Animatable(1f) }
    var emojiImageId by remember { mutableStateOf(R.drawable.heart) }

    LaunchedEffect(Unit) {
        val delayMillis = Random.nextLong(300, 500) * order
        delay(delayMillis)

        while (true) {
            val animationSpec = tween<Float>(
                durationMillis = Random.nextInt(2_000, 3_000),
                delayMillis = Random.nextInt(300, 1_200),
                easing = LinearEasing
            )

            emojiImageId = when (Random.nextInt(100)) {
                in 0 until 90 ->  R.drawable.heart
                in 90 until 96 ->  R.drawable.fire
                96 ->  R.drawable.thumbs_up
                97 ->  R.drawable.lol
                98 ->  R.drawable.hundred
                99 ->  R.drawable.smile
                else -> R.drawable.heart
            }
            val xOffsetAnimationJob = launch {
                val amplitude = Random.nextInt(minXAmplitude, maxXAmplitude)
                val startXOffset = Random.nextInt(0, amplitude).toFloat()
                xOffsetAnimation.snapTo(startXOffset)
                xOffsetAnimation.animateTo(
                    targetValue = -amplitude.toFloat(),
                    animationSpec = animationSpec,
                )
                yOffsetAnimation.snapTo(0f)
            }
            val yOffsetAnimationJob = launch {
                yOffsetAnimation.animateTo(
                    targetValue = -500f,
                    animationSpec = animationSpec,
                )
                yOffsetAnimation.snapTo(0f)
            }
            val alphaAnimationJob = launch {
                alphaAnimation.animateTo(
                    targetValue = 0f,
                    animationSpec = animationSpec,
                )
                alphaAnimation.snapTo(1f)
            }
            xOffsetAnimationJob.join()
            yOffsetAnimationJob.join()
            alphaAnimationJob.join()
        }
    }
    if (yOffsetAnimation.value != 0f) {
        Image(
            modifier = modifier
                .size(24.dp)
                .offset {
                    IntOffset(
                        x = -abs(xOffsetAnimation.value.roundToInt()),
                        y = yOffsetAnimation.value.roundToInt()
                    )
                }
                .alpha(alphaAnimation.value),
            painter = painterResource(emojiImageId),
            contentDescription = "reaction"
        )
    }
}

@Preview
@Composable
fun AnimatedReactionPreview() {
    FakeLiveStreamTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            repeat(6) { i ->
                AnimatedReaction(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    order = i
                )
            }
        }
    }
}