package com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.screen

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.components.*
import com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.stateholder.MyViewModel
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
 *
 * @param modifier This is the modification passed down from the parent Layout
 */
@Composable
fun WaterStatisticsScreen(
    modifier: Modifier = Modifier
) {

    val myViewModel: MyViewModel = viewModel()

    Column(
        modifier = modifier
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                // Outlined Text field which takes input from the User
                OutlinedTextField(
                    value = myViewModel.inputData,
                    onValueChange = {
                        myViewModel.onInputChange(it)
                    }
                )

                // This Button Updates the List
                Button(
                    onClick = { myViewModel.updateList() },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                {
                    Text(
                        text = "Submit",
                        modifier = Modifier
                            .padding(4.dp)
                    )
                }
            }

            // This function draws the Line chart with one Single Line
            CardViewUI(cardHeading = "Monthly Progress") {
                LineGraphUI(
                    yAxisReadings = myViewModel.yAxisReadingsData,
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

                // Defining the UI in the body of this Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    // This creates the line graph with 2 lines
                    LineGraphUI(
                        yAxisReadings = listOf(
                            listOf(3.8f, 3f, 2f, 3.9f, 4.9f, 4.2f, 3.8f),
                            listOf(3.5f, 2.2f, 3f, 3.4f, 3f, 4.4f, 3f)
                        ),
                        xAxisReadings = listOf(
                            "6-7",
                            "8-9",
                            "10-11",
                            "12-1",
                            "2-3",
                            "4-5",
                            "6-7"
                        ),
                        lineColor = listOf(customGreenForCharts, customBlueForCharts),
                        dotColor = listOf(customRedForCharts, customYellowForCharts),
                        numOfXMarkers = 7,
                        numOfYMarkers = 5,
                        height = 200.dp,
                        textColor = MaterialTheme.colorScheme.onSurface.toArgb()
                    )

                    // This function contains the Color Codes details
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 8.dp)
                    ) {

                        // List of the color code which needs to be shown in UI
                        listOf(
                            Pair(customBlueForCharts, "Water"),
                            Pair(customGreenForCharts, "Juice")
                        ).forEach {

                            // This row is the Color Code UI
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .weight(1f)
                            ) {

                                // Making the Rectangle
                                Canvas(
                                    modifier = Modifier.size(16.dp)
                                ) {
                                    drawRect(
                                        color = it.first
                                    )
                                }

                                // Spacer of 12 dp
                                Spacer(modifier = Modifier.width(12.dp))

                                // This is the text of the color code
                                Text(
                                    text = it.second,

                                    // Text and Font Properties
                                    fontFamily = InterFontFamily,
                                    fontWeight = FontWeight.W100,
                                    color = CustomGrey,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            // Spacer of 16 dp
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}