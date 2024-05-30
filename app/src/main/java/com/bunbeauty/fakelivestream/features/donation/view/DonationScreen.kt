package com.bunbeauty.fakelivestream.features.donation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.common.ui.LocalePreview
import com.bunbeauty.fakelivestream.common.ui.components.button.FakeLiveIconButton
import com.bunbeauty.fakelivestream.common.ui.components.button.FakeLivePrimaryButton
import com.bunbeauty.fakelivestream.common.ui.theme.FakeLiveStreamTheme
import com.bunbeauty.fakelivestream.common.ui.theme.bold
import com.bunbeauty.fakelivestream.features.donation.presentation.Donation
import com.bunbeauty.fakelivestream.features.donation.presentation.DonationViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun DonationScreen(
    navController: NavHostController,
    onDonateClick: (String) -> Unit,
) {
    val viewModel: DonationViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = remember {
        { action: Donation.Action ->
            viewModel.onAction(action)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.onEach { event ->
            when (event) {
                Donation.Event.GoBack -> {
                    navController.popBackStack()
                }
            }
        }.launchIn(this)
    }

    DonationContent(
        options = state.options,
        onAction = onAction,
        onDonateClick = onDonateClick,
    )
}

@Composable
private fun DonationContent(
    options: ImmutableList<Donation.Option>,
    onAction: (Donation.Action) -> Unit,
    onDonateClick: (String) -> Unit,
) {
    Column(modifier = Modifier.background(FakeLiveStreamTheme.colors.background)) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = spacedBy(16.dp)
        ) {
            FakeLiveIconButton(
                iconId = R.drawable.ic_back,
                iconTint = FakeLiveStreamTheme.colors.onBackground,
                contentDescription = "Back",
                withBorder = false,
                onClick = {
                    onAction(Donation.Action.BackClick)
                }
            )
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
                    id = R.string.donation_title,
                    stringResource(R.string.app_name)
                ),
                style = FakeLiveStreamTheme.typography.titleLarge.bold,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.donation_descriptio),
                style = FakeLiveStreamTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = spacedBy(4.dp)
        ) {
            repeat(options.size / 2) { i ->
                Row(horizontalArrangement = spacedBy(12.dp)) {
                    repeat(2) { j ->
                        options.getOrNull(i * 2 + j)?.let { option ->
                            FakeLivePrimaryButton(
                                modifier = Modifier.weight(1f),
                                text = option.text,
                                onClick = {
                                    onAction(Donation.Action.OptionClick(option = option))
                                    onDonateClick(option.name)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@LocalePreview
@Composable
fun IntroScreenPreview() {
    DonationContent(
        options = Donation.Option.entries.toImmutableList(),
        onAction = {},
        onDonateClick = {},
    )
}