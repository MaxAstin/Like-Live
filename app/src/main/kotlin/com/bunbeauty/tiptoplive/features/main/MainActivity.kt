package com.bunbeauty.tiptoplive.features.main

import android.Manifest.permission.CAMERA
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.navigation.NavigationDestinations.CROP_IMAGE
import com.bunbeauty.tiptoplive.common.navigation.NavigationDestinations.DONATION
import com.bunbeauty.tiptoplive.common.navigation.NavigationDestinations.INTRO
import com.bunbeauty.tiptoplive.common.navigation.NavigationDestinations.PREPARATION
import com.bunbeauty.tiptoplive.common.navigation.NavigationDestinations.STREAM
import com.bunbeauty.tiptoplive.common.navigation.NavigationParameters.CROPPED_IMAGE_URI
import com.bunbeauty.tiptoplive.common.navigation.NavigationParameters.URI
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.util.keepScreenOn
import com.bunbeauty.tiptoplive.common.util.launchInAppReview
import com.bunbeauty.tiptoplive.common.util.openSettings
import com.bunbeauty.tiptoplive.common.util.openSharing
import com.bunbeauty.tiptoplive.features.billing.BillingService
import com.bunbeauty.tiptoplive.features.cropimage.CropImageScreen
import com.bunbeauty.tiptoplive.features.donation.view.DonationScreen
import com.bunbeauty.tiptoplive.features.intro.view.IntroScreen
import com.bunbeauty.tiptoplive.features.main.presentation.Main
import com.bunbeauty.tiptoplive.features.main.presentation.MainViewModel
import com.bunbeauty.tiptoplive.features.main.view.CameraIsRequiredDialog
import com.bunbeauty.tiptoplive.features.preparation.view.PreparationScreen
import com.bunbeauty.tiptoplive.features.stream.view.DURATION_NAV_PARAM
import com.bunbeauty.tiptoplive.features.stream.view.StreamScreen
import dagger.Lazy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val GOOGLE_PLAY_LINK = "https://play.google.com/store/apps/details?id=com.bunbeauty.fakelivestream"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var billingService: Lazy<BillingService>

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            mainViewModel.onAction(Main.Action.CameraPermissionAccept)
        } else {
            mainViewModel.onAction(Main.Action.CameraPermissionDeny)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            FakeLiveTheme {
                val state by mainViewModel.state.collectAsStateWithLifecycle()

                AppContent()

                val onAction = remember {
                    { action: Main.Action ->
                        mainViewModel.onAction(action)
                    }
                }
                if (state.showNoCameraPermission) {
                    CameraIsRequiredDialog(
                        onAction = onAction,
                        onSettingsClick = {
                            openSettings()
                        }
                    )
                }
            }
        }
    }

    private fun requestCameraPermission() {
        val isCameraPermissionDenied = ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA)
        if (isCameraPermissionDenied) {
            mainViewModel.onAction(Main.Action.CameraPermissionDeny)
        } else {
            requestCameraPermissionLauncher.launch(CAMERA)
        }
    }

    @Composable
    private fun AppContent() {
        Scaffold(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
        ) { padding ->
            val navController = rememberNavController()
            LaunchedEffect(Unit) {
                mainViewModel.event.onEach { event ->
                    when (event) {
                        Main.Event.OpenStream -> {
                            navController.navigate(STREAM)
                        }
                    }
                }.launchIn(this)
            }

            LaunchedEffect(Unit) {
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    window.keepScreenOn = (destination.route == STREAM)
                }
            }

            MainNavigation(
                navController = navController,
                modifier = Modifier.padding(padding)
            )
        }
    }

    @Composable
    fun MainNavigation(
        navController: NavHostController,
        modifier: Modifier = Modifier,
    ) {
        NavHost(
            navController = navController,
            startDestination = INTRO,
            modifier = modifier,
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                ExitTransition.None
            },
        ) {
            composable(route = INTRO) {
                IntroScreen(navController = navController)
            }
            composable(route = PREPARATION) { entry ->
                val streamDurationInSeconds = entry.savedStateHandle.get<Int>(DURATION_NAV_PARAM)
                val croppedImageUri = entry.savedStateHandle.get<Uri>(CROPPED_IMAGE_URI)
                PreparationScreen(
                    navController = navController,
                    streamDurationInSeconds = streamDurationInSeconds,
                    croppedImageUri = croppedImageUri,
                    onStartStreamClick = {
                        requestCameraPermission()
                    },
                    onPositiveFeedbackClick = {
                        launchInAppReview()
                    },
                    onShareClick = {
                        val isSuccessful = openSharing(
                            text = getString(
                                R.string.sharing_text,
                                getString(R.string.app_name),
                                GOOGLE_PLAY_LINK
                            ),
                        )
                        if (!isSuccessful) {
                            // TODO show error
                        }
                    }
                )
            }
            composable(route = DONATION) {
                val scope = rememberCoroutineScope()
                DonationScreen(
                    navController = navController,
                    onDonateClick = { productId ->
                        scope.launch {
                            val isSuccessful = billingService.get().launchOneTypeProductFlow(
                                activity = this@MainActivity,
                                id = productId,
                            )
                            if (!isSuccessful) {
                                // TODO show error
                            }
                        }
                    }
                )
            }
            composable(route = STREAM) {
                StreamScreen(navController = navController)
            }
            composable(route = CROP_IMAGE) { navBackStackEntry ->
                val uri = navBackStackEntry.arguments?.getString(URI)?.toUri()
                CropImageScreen(
                    navController = navController,
                    uri = uri,
                    onMockClick = {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.common_under_development),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}