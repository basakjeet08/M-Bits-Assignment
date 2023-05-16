package com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.components.*
import com.dev.anirban.mbitsassignment.ui.theme.*

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
            .background(MaterialTheme.colorScheme.secondary)
    ) {

        // This is the Item which is selected in the Tab Option Layout
        val selectedItem = remember { mutableStateOf(0) }

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
                LineGraphUI(
                    yAxisReadings = listOf(listOf(6f, 5f, 4f, 6f, 7.5f, 7f, 6f)),
                    xAxisReadings = listOf(
                        "Jan",
                        "Mar",
                        "May",
                        "Jul",
                        "Sep",
                        "Nov",
                        "Dec"
                    ),
                    lineColor = listOf(customBlueForCharts),
                    dotColor = listOf(customGreenForCharts),
                    numOfXMarkers = 7,
                    numOfYMarkers = 5,
                    height = 200.dp,
                    textColor = MaterialTheme.colorScheme.onSurface.toArgb()
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
                        customBlueForCharts,
                        customGreenForCharts,
                        customRedForCharts
                    ),
                    unit = "mL"
                )
            }

            // This function draws the Line chart with one Single Line
            CardViewUI(cardHeading = "Beverages") {
                LineGraphUI(
                    yAxisReadings = listOf(
                        listOf(3.8f, 3f, 2f, 3.9f, 4.9f, 4.2f, 3.8f),
                        listOf(3.5f, 2.2f, 3f, 3.4f, 3f, 4.4f, 3f)
                    ),
                    xAxisReadings = listOf(
                        "Jan",
                        "Mar",
                        "May",
                        "Jul",
                        "Sep",
                        "Nov",
                        "Dec"
                    ),
                    lineColor = listOf(customGreenForCharts, customBlueForCharts),
                    dotColor = listOf(customRedForCharts, customYellowForCharts),
                    numOfXMarkers = 7,
                    numOfYMarkers = 5,
                    height = 200.dp,
                    textColor = MaterialTheme.colorScheme.onSurface.toArgb()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}