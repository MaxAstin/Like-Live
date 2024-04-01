package com.bunbeauty.fakelivestream.features.intro.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.common.navigation.NavigationDestinations.INTRO
import com.bunbeauty.fakelivestream.common.navigation.NavigationDestinations.PREPARATION
import com.bunbeauty.fakelivestream.common.ui.LocalePreview
import com.bunbeauty.fakelivestream.common.ui.components.button.PrimaryButton
import com.bunbeauty.fakelivestream.common.ui.theme.FakeLiveStreamTheme
import com.bunbeauty.fakelivestream.common.ui.theme.bold
import com.bunbeauty.fakelivestream.features.intro.presentation.Intro
import com.bunbeauty.fakelivestream.features.intro.presentation.IntroViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun IntroScreen(navController: NavHostController) {
    val viewModel: IntroViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = remember {
        { action: Intro.Action ->
            viewModel.onAction(action)
        }
    }

    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.event.onEach { event ->
            when (event) {
                is Intro.Event.OpenPreparation -> {
                    navController.navigate(PREPARATION) {
                        popUpTo(INTRO) {
                            inclusive = true
                        }
                    }
                }
            }
        }.launchIn(scope)
    }

    IntroContent(
        isChecked = state.isChecked,
        onAction = onAction
    )
}

@Composable
private fun IntroContent(
    isChecked: Boolean,
    onAction: (Intro.Action) -> Unit
) {
    if (isChecked) {
        Column(modifier = Modifier.background(FakeLiveStreamTheme.colors.background)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        modifier = Modifier.weight(4f),
                        painter = painterResource(R.drawable.img_logo),
                        contentDescription = "Logo"
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(
                        id = R.string.intro_welcome,
                        stringResource(R.string.app_name)
                    ),
                    style = FakeLiveStreamTheme.typography.titleLarge.bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.intro_description),
                    style = FakeLiveStreamTheme.typography.bodyLarge,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = FakeLiveStreamTheme.colors.borderVariant,
            )
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = stringResource(id = R.string.intro_next),
                onClick = {
                    onAction(Intro.Action.NextClick)
                },
            )
        }
    } else {
        Box(modifier = Modifier.fillMaxSize())
    }
}

@LocalePreview
@Composable
fun IntroScreenPreview() {
    IntroContent(
        isChecked = true,
        onAction = {}
    )
}