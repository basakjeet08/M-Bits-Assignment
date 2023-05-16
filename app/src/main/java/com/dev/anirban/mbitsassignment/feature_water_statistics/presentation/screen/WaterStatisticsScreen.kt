package com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.components.*
import com.dev.anirban.mbitsassignment.ui.theme.MBitsAssignmentTheme
import com.dev.anirban.mbitsassignment.ui.theme.pieChartBlue
import com.dev.anirban.mbitsassignment.ui.theme.pieChartGreen
import com.dev.anirban.mbitsassignment.ui.theme.pieChartRed

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

        // This Column contains the body of the screen
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            // This function draws the Line chart with one Single Line
            CardViewUI(cardHeading = "Monthly Progress") {
                LineGraphUIX(
                    coordinates = listOf(
                        Pair(6f, "Jan"),
                        Pair(5f, "Mar"),
                        Pair(4f, "May"),
                        Pair(6f, "Jul"),
                        Pair(7.5f, "Sep"),
                        Pair(7f, "Nov"),
                        Pair(6f, "Dec")
                    )
                )
            }

            // This cardView makes a card with its contents given in its composable function
            CardViewUI(cardHeading = "Ratio") {

                // This function draws a Pie Chart using Canvas
                PieChartUI(
                    itemsList = listOf(
                        Pair("Water", 1500f),
                        Pair("Juice", 300f),
                        Pair("Soft Drink", 500f)
                    ),
                    colorList = listOf(
                        pieChartBlue,
                        pieChartGreen,
                        pieChartRed
                    ),
                    unit = "mL"
                )
            }
        }
    }
}