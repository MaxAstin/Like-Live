package com.bunbeauty.fakelivestream.features.main

import android.Manifest.permission.CAMERA
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
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
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.fakelivestream.common.navigation.NavigationDestinations.PREPARATION
import com.bunbeauty.fakelivestream.common.navigation.NavigationDestinations.STREAM
import com.bunbeauty.fakelivestream.features.main.presentation.Main
import com.bunbeauty.fakelivestream.features.main.presentation.MainViewModel
import com.bunbeauty.fakelivestream.features.main.view.CameraIsRequiredDialog
import com.bunbeauty.fakelivestream.features.preparation.presentation.Preparation
import com.bunbeauty.fakelivestream.features.preparation.presentation.PreparationViewModel
import com.bunbeauty.fakelivestream.features.preparation.view.PreparationScreen
import com.bunbeauty.fakelivestream.features.stream.presentation.Stream
import com.bunbeauty.fakelivestream.features.stream.presentation.StreamViewModel
import com.bunbeauty.fakelivestream.features.stream.view.StreamScreen
import com.bunbeauty.fakelivestream.features.stream.view.toViewState
import com.bunbeauty.fakelivestream.ui.keepScreenOn
import com.bunbeauty.fakelivestream.ui.theme.FakeLiveStreamTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            mainViewModel.onAction(Main.Action.CameraPermissionDeny)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FakeLiveStreamTheme {
                val state by mainViewModel.state.collectAsStateWithLifecycle()

                AppContent()

                val onAction = remember {
                    { action: Main.Action ->
                        mainViewModel.onAction(action)
                    }
                }
                if (state.showNoCameraPermission) {
                    CameraIsRequiredDialog(onAction = onAction)
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

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun openSettings() {
        startActivity(
            Intent().apply {
                action = ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", packageName, null)
            }
        )
    }

    @Composable
    private fun AppContent() {
        Scaffold(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
        ) { padding ->
            val scope = rememberCoroutineScope()
            LaunchedEffect(Unit) {
                mainViewModel.event.onEach { event ->
                    when (event) {
                        Main.Event.OpenSettings -> {
                            openSettings()
                        }
                    }
                }.launchIn(scope)
            }

            val navController = rememberNavController()
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
            startDestination = PREPARATION,
            modifier = modifier,
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                ExitTransition.None
            },
        ) {
            composable(route = PREPARATION) {
                val viewModel: PreparationViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                val onAction = remember {
                    { action: Preparation.Action ->
                        viewModel.onAction(action)
                    }
                }

                val scope = rememberCoroutineScope()
                LaunchedEffect(Unit) {
                    viewModel.event.onEach { event ->
                        when (event) {
                            Preparation.Event.OpenStream -> {
                                if (isCameraPermissionGranted()) {
                                    navController.navigate(STREAM)
                                } else {
                                    requestCameraPermission()
                                }
                            }
                        }
                    }.launchIn(scope)
                }

                PreparationScreen(
                    state = state,
                    onAction = onAction
                )
            }
            composable(route = STREAM) {
                val viewModel: StreamViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                val onAction = remember {
                    { action: Stream.Action ->
                        viewModel.onAction(action)
                    }
                }

                val scope = rememberCoroutineScope()
                LaunchedEffect(Unit) {
                    viewModel.event.onEach { event ->
                        when (event) {
                            Stream.Event.GoBack -> {
                                navController.popBackStack()
                            }
                        }
                    }.launchIn(scope)
                }
                LifecycleEventEffect(Lifecycle.Event.ON_START) {
                    onAction(Stream.Action.Start)
                }
                LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
                    onAction(Stream.Action.Stop)
                }

                StreamScreen(
                    state = state.toViewState(),
                    onAction = onAction,
                )
            }
        }
    }
}