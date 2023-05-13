package com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.components.TabOptionListUI
import com.dev.anirban.mbitsassignment.ui.theme.MBitsAssignmentTheme

// Preview Composable Function
@Preview("Light")
@Preview(
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun DefaultPreview() {
    MBitsAssignmentTheme {
        WaterStatisticsScreen()
    }
}

/**
 * This is the water Statistics Screen which shows various Charts and Graphs
 */
@Composable
fun WaterStatisticsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // This is the Item which is selected in the Tab Option Layout
        val selectedItem = remember { mutableStateOf(0) }

        // This is used to leave some space between the App Title and the Body UI
        Spacer(modifier = Modifier.height(16.dp))

        // This Function makes the Tab Layout UI
        TabOptionListUI(
            tabList = listOf(
                "DAY",
                "WEEK",
                "MONTH",
                "ALL"
            ),
            selectedItem = selectedItem.value
        ) {

            // Changing the Current Selected Item according to the User Interactions
            selectedItem.value = it
        }
    }
}