package com.bunbeauty.fakelivestream

import android.Manifest.permission.CAMERA
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.fakelivestream.common.navigation.NavigationDestinations.PREPARATION
import com.bunbeauty.fakelivestream.common.navigation.NavigationDestinations.STREAM
import com.bunbeauty.fakelivestream.features.preparation.presentation.Preparation
import com.bunbeauty.fakelivestream.features.preparation.presentation.PreparationViewModel
import com.bunbeauty.fakelivestream.features.preparation.ui.PreparationScreen
import com.bunbeauty.fakelivestream.features.stream.presentation.StreamViewModel
import com.bunbeauty.fakelivestream.features.stream.ui.StreamScreen
import com.bunbeauty.fakelivestream.ui.theme.FakeLiveStreamTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            AlertDialog.Builder(this)
                .setMessage(resources.getString(R.string.need_camera_permisseon))
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestCameraPermission()

        setContent {
            FakeLiveStreamTheme {
                AppContent()
            }
        }
    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_GRANTED -> {}
            ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA) -> {}
            else -> requestCameraPermissionLauncher.launch(CAMERA)
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
                val state by viewModel.state.collectAsState()
                val onAction = remember {
                    { action: Preparation.Action ->
                        viewModel.onAction(action)
                    }
                }
                PreparationScreen(
                    state = state,
                    navController = navController,
                    onAction = onAction
                )
            }
            composable(route = STREAM) {
                val viewModel: StreamViewModel = hiltViewModel()
                val state by viewModel.state.collectAsState()
                StreamScreen(state = state, navController = navController)
            }
        }
    }
}