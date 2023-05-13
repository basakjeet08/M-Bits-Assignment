package com.dev.anirban.mbitsassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.components.TopBarUI
import com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.screen.WaterStatisticsScreen
import com.dev.anirban.mbitsassignment.ui.theme.MBitsAssignmentTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MBitsAssignmentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    // Scaffold
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize(),
                        topBar = {
                            TopBarUI()
                        }
                    ) {

                        // Main App UI in the Body of the Scaffold
                        Surface(
                            modifier = Modifier
                                .padding(it)
                                .fillMaxSize(),
                        ) {

                            // Calling the Water Statistics Screen
                            WaterStatisticsScreen()
                        }
                    }
                }
            }
        }
    }
}