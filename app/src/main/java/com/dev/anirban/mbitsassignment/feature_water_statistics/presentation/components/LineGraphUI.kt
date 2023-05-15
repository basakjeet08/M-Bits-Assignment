package com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.components

import android.content.res.Configuration
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.anirban.mbitsassignment.ui.theme.MBitsAssignmentTheme

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
        LineGraphUI(
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
}

/**
 * This function draws the Line Graph in the UI
 *
 * @param modifier To be passed by the Parent Layout
 * @param coordinates This is the list of coordinates which need to be mapped in the Graph
 * @param height This is the Minimum height of the Graph
 * @param numOfXMarkers This is the Number of X markers which will be there in the Graph
 * @param numOfYMarkers This is the number of Y markers which will be there in the Graph
 */
@Composable
fun LineGraphUI(
    modifier: Modifier = Modifier,
    coordinates: List<Pair<Float, String>>,
    height: Dp = 180.dp,
    numOfYMarkers: Int = 5,
    numOfXMarkers: Int = 7
) {

    // These variables will contains the Coordinate's X and Y Axis individually
    val yAxisData: MutableList<Float> = mutableListOf()
    val xAxisData: MutableList<String> = mutableListOf()

    // Fetching the X and Y coordinates accordingly
    coordinates.forEach {
        yAxisData.add(it.first)
        xAxisData.add(it.second)
    }

    // This function draws the Graph
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        contentAlignment = Alignment.Center
    ) {

        // This canvas draws the complex Line Chart UI
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        ) {

            // Decreasing the Size of Component than the Canvas Size to make the UI look better
            val componentSize = size / 1.25f

            // X and Y Coordinates Padding Values from the Top and start/left
            val xPadding = (size.width - componentSize.width) / 2f
            val yPadding = (size.height - componentSize.height) / 2f

            // Pixel to be changed for Y for each unit Floating value change in Y
            var changeInYForEachPointChange = 0f

            // This function generates the List of Y Markers and returns it to this variable
            val yMarkerList = generateYMarkerList(
                yAxisData = yAxisData,
                yMarkerCount = numOfYMarkers - 1
            ) {

                // Setting the PixelToFloating Ratio to be used during Plotting
                changeInYForEachPointChange = (componentSize.height - 2 * yPadding) / it
            }

            // This function draws the Line Chart X and Y Axis
            drawGraphAxis(
                componentSize = componentSize,
                xMarkerList = xAxisData,
                yMarkerList = yMarkerList,
                numOfYMarkers = numOfYMarkers - 1,
                numOfXMarkers = numOfXMarkers,
                xPadding = xPadding,
                yPadding = yPadding
            )
        }
    }

}

/**
 * This function creates the Markers of Y-Axis according to which the graph will be plotted
 *
 * @param yAxisData This is the list of Floating points in the Y-Axis
 * @param yMarkerCount This is the number of markers should be there
 * @param setYMarkerOffset This is the distance between individual Markers
 */
private fun generateYMarkerList(
    yAxisData: List<Float>,
    yMarkerCount: Int,
    setYMarkerOffset: (Float) -> Unit
): List<Int> {

    // Finding the Min and Maximum of the Given Readings
    val yMinReading: Int = (yAxisData.min() - yAxisData.min()).toInt() / yMarkerCount
    val yMaxReading: Int = ((yAxisData.max() / yMarkerCount).toInt() + 1) * yMarkerCount

    // This is the distance between each y Markers of the Graph
    val yMarkerOffset: Int = (yMaxReading - yMinReading) / yMarkerCount

    // This function sets the yMarkOffset
    setYMarkerOffset((yMaxReading - yMinReading).toFloat())

    // This is the actual list of Y-Axis Markers
    val yMarkerList: MutableList<Int> = mutableListOf()

    // These are the Y-Markers Item List
    var yMarkerItem = yMaxReading

    // Looping for given number of Number of Count to add them
    for (i in 1..yMarkerCount + 1) {

        yMarkerList.add(yMarkerItem)
        yMarkerItem -= yMarkerOffset
    }
    return yMarkerList
}

/**
 * This function draws the Line Chart X and Y Axis
 *
 * @param componentSize This is the size of the component/container of the Graph
 * @param xMarkerList This is the List of Markers to be plotted in X Axis
 * @param yMarkerList This is the List of Markers to be plotted in Y Axis
 * @param numOfXMarkers This is the number of X Markers that are to be drawn
 * @param numOfYMarkers This is the number of Y Markers that are to be drawn
 * @param xPadding This is the padding in the X-Axis
 * @param yPadding This is the padding in the Y-Axis
 */
fun DrawScope.drawGraphAxis(
    componentSize: Size,
    xMarkerList: List<String>,
    yMarkerList: List<Int>,
    numOfYMarkers: Int,
    numOfXMarkers: Int,
    xPadding: Float,
    yPadding: Float
) {

    // Space that can be allotted for each Reading
    val yAxisSpaceForEachReading = (componentSize.height - 2 * yPadding) / numOfYMarkers

    // This is the distance from the top for each line
    var yDistanceFromTop = yAxisSpaceForEachReading

    yMarkerList.forEach {

        // This draws the numbering of the X Axis
        drawContext.canvas.nativeCanvas.drawText(
            it.toString(),
            xPadding - 24.dp.toPx(),
            yDistanceFromTop + 4.dp.toPx(),
            Paint().apply {
                color = Color.Black.toArgb()
                textSize = 12.sp.toPx()
                textAlign = Paint.Align.LEFT
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            }
        )

        // This draws the Lines for the readings parallel to X Axis
        drawLine(
            start = Offset(
                x = xPadding,
                y = yDistanceFromTop
            ),
            color = Color.Gray,
            end = Offset(
                x = size.width - xPadding,
                y = yDistanceFromTop
            ),
            strokeWidth = 2f
        )

        // Changing the distance from top to move to the next Line
        yDistanceFromTop += yAxisSpaceForEachReading
    }

    // Distance Between Each X Axis Marker
    val xOffset = (size.width - 2 * xPadding) / numOfXMarkers

    // Current XDistance from the left which is used to define the x offset of each and every Marker
    var xDistanceFromLeft = xPadding

    // Taking Each Marker and marking or drawing it on the UI
    xMarkerList.forEach {

        // This draws the String Marker
        drawContext.canvas.nativeCanvas.drawText(
            it,
            xDistanceFromLeft,
            yDistanceFromTop,
            Paint().apply {
                color = Color.Black.toArgb()
                textSize = 12.sp.toPx()
                textAlign = Paint.Align.LEFT
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            }
        )

        // Increasing the xOffset to help with the adaptation of UI
        xDistanceFromLeft += xOffset
    }
}