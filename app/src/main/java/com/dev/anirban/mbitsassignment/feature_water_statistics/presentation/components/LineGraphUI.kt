package com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.components

import android.content.res.Configuration
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.anirban.mbitsassignment.ui.theme.MBitsAssignmentTheme
import com.dev.anirban.mbitsassignment.ui.theme.pieChartBlue
import com.dev.anirban.mbitsassignment.ui.theme.pieChartGreen


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
}

/**
 * This function draws the Line Graph in the UI
 *
 * @param modifier To be passed by the Parent Layout
 * @param coordinates This is the list of coordinates which need to be mapped in the Graph (Always
 * provide the coordinates in ascending order of the Graph from left to right)
 * @param height This is the Minimum height of the Graph
 * @param numOfXMarkers This is the Number of X markers which will be there in the Graph
 * @param numOfYMarkers This is the number of Y markers which will be there in the Graph
 */
@Composable
fun LineGraphUIX(
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

    // Y Axis Marker bounds are held by these variables
    var yLowerBounds = 0
    var yUpperBounds = 0

    // This function calculates the Bounds of the Y Axis and also sets the above two variable values
    calculateLineBoundsX(
        coordinates = coordinates,
        yMarkerCount = numOfYMarkers,
        setYBounds = { lower, upper ->
            yLowerBounds = lower
            yUpperBounds = upper
        }
    )

    // This canvas draws the complex Line Chart UI
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {

        // Decreasing the Size of Component than the Canvas Size to make the UI look better
        val componentSize = size / 1.20f

        // X and Y Coordinates Padding Values from the Top and start/left
        val xPadding = (size.width - componentSize.width) / 2f
        val yPadding = (size.height - componentSize.height) / 2f

        // These variables keeps the offset/Scale ratio
        var xOffsetRatio = 0f
        var yOffsetRatio = 0f

        // This variables contains the lowest Marker in the Graph in Y - Axis
        var lowestMarker = 0

        // This function draws the Axis and the Markers
        drawGraphAxisAndMarkersX(
            yUpperBounds = yUpperBounds,
            yLowerBounds = yLowerBounds,
            xMarkerCount = numOfXMarkers,
            yMarkerCount = numOfYMarkers,
            xPadding = xPadding,
            yPadding = yPadding,
            xAxisMarkers = xAxisData,
            setLowestMarker = {
                lowestMarker = it
            }
        ) { yRatio, xRatio ->
            yOffsetRatio = yRatio
            xOffsetRatio = xRatio
        }

        // This function plots the Graph and joins the Line
        plotGraphPoints(
            coordinates = coordinates,
            yOffsetRatio = yOffsetRatio,
            xOffsetRatio = xOffsetRatio,
            yUpperBound = yUpperBounds,
            yLowerBound = yLowerBounds,
            yMarkerCount = numOfYMarkers,
            lowestMarker = lowestMarker
        )
    }
}


/**
 * This function calculates the Minimum and Maximum Markers of Y Axis and returns them to the parent
 * using a lambda Function
 *
 * @param coordinates This is the coordinates of the X,Y Axis Readings
 * @param yMarkerCount This is the Number of Markers to be set on Y Axis
 * @param setYBounds This is the lambda function which sends the minimum and maximum value to the
 * parent function
 */
private fun calculateLineBoundsX(
    coordinates: List<Pair<Float, String>>,
    yMarkerCount: Int,
    setYBounds: (yLowerBound: Int, yUpperBound: Int) -> Unit,
) {

    // These are the Minimum and Maximum readings that needs to be shown
    var yMaxReading = coordinates[0].first
    var yMinReading = coordinates[0].first

    // Searching for the Minimum and maximum from the list
    coordinates.forEach {
        if (yMaxReading < it.first)
            yMaxReading = it.first

        if (yMinReading > it.first)
            yMinReading = it.first
    }

    // Finding the Min and Maximum of the Given Readings
    val yUpperBound = ((yMaxReading / yMarkerCount).toInt() + 1) * yMarkerCount
    val yLowerBounds = ((yMinReading / yMarkerCount).toInt() - 1) * yMarkerCount

    // Setting the reading Bounds back to the Parent for further processing
    setYBounds(
        if (yLowerBounds >= 0) yLowerBounds else 0,
        yUpperBound
    )
}


/**
 * This function draws the Axes and the Markers of the Graph
 *
 * @param yUpperBounds This is the upper Bounds for Y-Axis
 * @param yLowerBounds This is the Lower Bounds for Y- Axis
 * @param xMarkerCount this is the count of how many markers should be there on X - Axis
 * @param yMarkerCount This is the count of how many markers should be there on Y - Axis
 * @param xPadding This is the padding from the Left of the Canvas
 * @param yPadding This is the padding from the top of the Canvas
 * @param xAxisMarkers This is the List of Strings which needs to be there on X - Axis
 * @param setLowestMarker This function is used to set the Lowest Marker in the Graph Y - Axis
 * @param setOffsetOfAxis This function sets the Offset for both the Axis
 */
private fun DrawScope.drawGraphAxisAndMarkersX(
    yUpperBounds: Int,
    yLowerBounds: Int,
    xMarkerCount: Int,
    yMarkerCount: Int,
    xPadding: Float,
    yPadding: Float,
    xAxisMarkers: List<String>,
    setLowestMarker: (Int) -> Unit,
    setOffsetOfAxis: (yOffsetRatio: Float, xOffsetRatio: Float) -> Unit
) {

    // Total Size of the Canvas which can be used for the Graph
    val xTotalSize = size.width - 2 * xPadding
    val yTotalSize = size.height - 2 * yPadding

    // This is the Floating Value to Canvas Px Ratio
    val yOffsetRatio = yTotalSize / (yMarkerCount + 0.25f)
    val xOffsetRatio = xTotalSize / xMarkerCount

    // Sending the offset back to the parent function
    setOffsetOfAxis(yOffsetRatio, xOffsetRatio)

    // Drawing the Graph X Axis Parallel Lines
    for (i in 1..yMarkerCount) {

        // This is the value of the current Y Axis Marker
        val currentYMarker =
            yUpperBounds - (((yUpperBounds - yLowerBounds) / yMarkerCount) * (i - 1))

        // This draws the String Marker
        drawContext.canvas.nativeCanvas.drawText(
            currentYMarker.toString(),
            xPadding - 24f,
            (yOffsetRatio * i) + 12f,
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
                x = xPadding + 24f,
                y = yOffsetRatio * i
            ),
            color = Color.Gray,
            end = Offset(
                x = size.width - xPadding,
                y = yOffsetRatio * i
            ),
            strokeWidth = 2f
        )

//        lowestMarker = currentYMarker
    }

    // This variable contains the Lowest Marker in the Y - Axis
    val lowestMarker =
        yUpperBounds - (((yUpperBounds - yLowerBounds) / yMarkerCount) * (yMarkerCount - 1))

    // This function sets the lowest Marker of the Graph in the Parent Function
    setLowestMarker(lowestMarker)

    // This Draws the Y Markers below the Graph
    xAxisMarkers.forEachIndexed { index, currentMarker ->

        // This draws the String Marker
        drawContext.canvas.nativeCanvas.drawText(
            currentMarker,
            xOffsetRatio * (index + 1),
            yOffsetRatio * (yMarkerCount + 1),
            Paint().apply {
                color = Color.Black.toArgb()
                textSize = 12.sp.toPx()
                textAlign = Paint.Align.LEFT
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            }
        )
    }
}

/**
 * This function plots the Graph Points and joins them using a Curved Line
 *
 * @param coordinates This contains all the Coordinates of the Points to be plotted in the Graph
 * @param yOffsetRatio This is the Offset from the Y Axis or TOP
 * @param xOffsetRatio This is the Offset from the X Axis or the Left
 * @param yUpperBound This is the Upper Bounds of the Graph Marker
 * @param yLowerBound This is the Lower Bounds of the Graph marker
 * @param lowestMarker This is the Lowest Marker point of the Y Axis
 */
private fun DrawScope.plotGraphPoints(
    coordinates: List<Pair<Float, String>>,
    yOffsetRatio: Float,
    xOffsetRatio: Float,
    yUpperBound: Int,
    yLowerBound: Int,
    yMarkerCount: Int,
    lowestMarker: Int
) {

    // This is the Size of the Graph Y - Axis
    val ySizeOfGraph = yOffsetRatio * yMarkerCount

    // Difference between Maximum and the Minimum Markers
    val differenceBetweenMaxAndMin = yUpperBound - yLowerBound

    // Scale of Y Change in Px according to change in Floating Point
    val yRatio = ySizeOfGraph / differenceBetweenMaxAndMin

    // This variable contains all the Offset of all the graph coordinates
    val graphCoordinates: MutableList<Offset> = mutableListOf()

    // Adding the Offsets to the Variable
    coordinates.forEachIndexed { index, coordinate ->

        graphCoordinates.add(
            Offset(
                x = 24f + (index + 1) * xOffsetRatio,
                y = ySizeOfGraph - (yRatio * (coordinate.first - lowestMarker))
            )
        )
    }
//        drawCircle(
//            color = pieChartGreen,
//            radius = 5f,
//            center = Offset(
//                x = 24f + (index + 1) * xOffsetRatio,
//                y = sizeOfGraph - (yRatio * (coordinate.first - lowestMarker))
//            )
//        )
//    }

    var previousOffset = graphCoordinates[0]

    // drawing the Circle points and the Curved Line
    graphCoordinates.forEachIndexed { index, offset ->

        // This function draws the Circle points 
        drawCircle(
            color = pieChartGreen,
            radius = 10f,
            center = offset
        )

        if (index != 0) {

            // This function draws the curved Lines
            drawLine(
                color = pieChartBlue,
                start = previousOffset,
                end = offset,
                strokeWidth = 3f
            )
        }
        previousOffset = offset
    }

}