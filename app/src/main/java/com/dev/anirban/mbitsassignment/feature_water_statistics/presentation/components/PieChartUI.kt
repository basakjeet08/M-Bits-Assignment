package com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.anirban.mbitsassignment.ui.theme.MBitsAssignmentTheme
import com.dev.anirban.mbitsassignment.ui.theme.customBlueForCharts
import com.dev.anirban.mbitsassignment.ui.theme.customGreenForCharts
import com.dev.anirban.mbitsassignment.ui.theme.customRedForCharts

// Preview Function
@Preview("Light")
@Preview(
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun DefaultPreview() {
    MBitsAssignmentTheme {

        PieChartUI(
            itemsList = listOf(
                Pair("Water", 1500.0f),
                Pair("Juice", 300.0f),
                Pair("Soft Drink", 500.0f)
            ),
            colorList = listOf(
                customBlueForCharts,
                customGreenForCharts,
                customRedForCharts
            ),
            unit = "mL"
        )
    }
}

/**
 * This function draws the Pie Chart and writes all the items according to
 * their respective colors
 *
 * @param modifier This is the default Modifier to be passed down by the
 * Parent
 * @param itemsList This is the list of Items with Names which needs to be displayed
 * @param colorList This is the list of Colors in order with the Items
 * @param minHeight This is the minimum height of the Row layout Range(150 dp to 200 dp)
 */
@Composable
fun PieChartUI(
    modifier: Modifier = Modifier,
    itemsList: List<Pair<String, Float>>,
    colorList: List<Color>,
    minHeight: Dp = 180.dp,
    unit: String
) {

    // It separates the Chart and its reading Items and color coding
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(minHeight)
            .defaultMinSize(minWidth = Dp.Infinity, minHeight = minHeight)
    ) {

        // This contains the Pie Chart Graph
        Box(
            modifier = Modifier
                .width(minHeight)
                .height(minHeight)
                .drawBehind {

                    // Size of the chart (We are taking a bit less than the Canvas to make it look good)
                    val chartSize = size / 2f

                    // List of all the Floating Numbers which will be used to display Chart
                    val valueList: MutableList<Float> = mutableListOf()

                    // Initializing the List
                    itemsList.forEachIndexed { _, pair ->
                        valueList.add(pair.second)
                    }

                    // This draws the Pie Chart Graph
                    drawPieChart(
                        componentSize = chartSize,
                        floatingDataList = valueList,
                        colorList = colorList
                    )
                }
        )

        // This UI contains all the Items given and their values and color codes
        Column(
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minHeight = minHeight, minWidth = Dp.Infinity),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Drawing the Color Code and writing the Text of the UI
            itemsList.forEachIndexed { it, pair ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Drawing the small circles(color codes)
                    Canvas(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(20.dp)
                    ) {

                        // This function draws the Color codes circles
                        drawCircle(
                            colorList[it],
                            radius = 20f,
                            center = size.center
                        )
                    }

                    // Item Name
                    Text(
                        text = "${pair.first} - ",
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 4.dp, start = 4.dp),

                        // Text Features
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W300
                    )

                    // Checking if the data should be displayed in SI unit or Cgs
                    val value =
                        if (pair.second >= 1000) pair.second / 1000.0f else pair.second.toInt()
                    val reconsideredUnit = if (pair.second >= 1000) "L" else unit

                    // Item Value
                    Text(
                        text = "$value$reconsideredUnit",

                        // Text Features
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    )
                }
            }
        }
    }
}

/**
 * This function draws the Pie chart Graph shown in UI
 *
 * @param componentSize This is the size of this pie chart graph boundaries
 * @param floatingDataList This is the list of the Floating Data according to which graph will form
 * @param colorList This is the List of colors which should be used
 */
fun DrawScope.drawPieChart(
    componentSize: Size,
    floatingDataList: List<Float>,
    colorList: List<Color>
) {

    // Total of the Data
    var total = 0f

    // This list will contains the amount of sweep Angle each floating data should get
    val sweepAngles: MutableList<Float> = mutableListOf()

    // Calculating Total
    floatingDataList.forEach {
        total += it
    }

    // Calculating the SweepAngles
    floatingDataList.forEach { fl ->

        val percentage = (fl / total)

        /**
         * some value is subtracted because according to the UI there shall be some free space
         * between each graph.
         *
         * Free Space = Some Angles shall be subtracted so that
         *
         * We are taking a 4f minus between each and every Floating Datas
         */
        val angle = percentage * (360f - (floatingDataList.size * 4f))
        sweepAngles.add(angle)
    }

    // This is used to define the sweep angle of each and every Floating Data
    var currentSweepAngle = 270f

    // Drawing all the arcs
    sweepAngles.forEachIndexed { index, fl ->

        //This function draws the Arc
        drawArc(
            color = colorList[index],
            startAngle = currentSweepAngle,
            sweepAngle = fl,
            useCenter = false,
            size = componentSize,
            style = Stroke(
                width = 45f
            ),
            topLeft = Offset(
                x = (size.width - componentSize.width) / 2f,
                y = (size.height - componentSize.height) / 2f
            )
        )

        // Marking the sweep angle for the next Floating Item
        currentSweepAngle += fl + 4f
    }
}