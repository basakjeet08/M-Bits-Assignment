package com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.components

import android.content.res.Configuration
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.anirban.mbitsassignment.ui.theme.MBitsAssignmentTheme
import com.dev.anirban.mbitsassignment.ui.theme.customBlueForCharts
import com.dev.anirban.mbitsassignment.ui.theme.customGreenForCharts

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
}

/**
 * This function draws the Line Graph in the UI
 *
 * @param modifier To be passed by the Parent Layout
 * @param xAxisReadings This is the list of X - coordinates which need to be mapped in the Graph
 * (Always provide the coordinates in ascending order of the Graph from left to right)
 * @param yAxisReadings This is the list of set of Y - coordinates which need to be mapped in the Graph
 * (Always provide the coordinates in ascending order of the Graph from left to right)
 * @param lineColor This is the list of color of the Line of a particular Set of the Graph Reading
 * @param dotColor This is the list of color of the Dot of a particular Set of the graph
 * @param height This is the Minimum height of the Graph
 * @param numOfXMarkers This is the Number of X markers which will be there in the Graph
 * @param numOfYMarkers This is the number of Y markers which will be there in the Graph
 */
@Composable
fun LineGraphUI(
    modifier: Modifier = Modifier,
    yAxisReadings: List<List<Float>>,
    xAxisReadings: List<String>,
    lineColor: List<Color>,
    dotColor: List<Color>,
    height: Dp = 200.dp,
    numOfYMarkers: Int,
    numOfXMarkers: Int,
    textColor: Int
) {

    // Y Axis Marker bounds are held by these variables
    var yUpper = yAxisReadings[0][0]
    var yLower = yAxisReadings[0][0]

    // Finding the upper bound and Lower Bound of Y
    yAxisReadings.forEach { readings ->
        readings.forEach {
            if (it > yUpper)
                yUpper = it

            if (it < yLower)
                yLower = it
        }
    }

    // Storing the upper Bound and Lower bound of Y Markers
    val yUpperReadingRange =
        yUpper.toInt() + ((numOfYMarkers - 1) - (yUpper.toInt() % (numOfYMarkers - 1)))
    val yLowerReadingRange = (yLower.toInt() - (yLower.toInt() % (numOfYMarkers - 1)))

    // Difference between each Y Markers
    val yDividend = (yUpperReadingRange - yLowerReadingRange) / (numOfYMarkers - 1)

    // This canvas draws the complex Line Chart UI
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {

        // Decreasing the Size of Component than the Canvas Size to make the UI look better
        val componentSize = size / 1.20f

        // X Coordinates of the Graph
        val xOrigin = (size.width - componentSize.width) / 2f
        val xMax = size.width - xOrigin

        // Y Coordinates of the Graph
        val yOrigin = (size.height - componentSize.height) / 2f
        val yMax = size.height - yOrigin

        // Total Size of each of the Coordinates of the Graph
        val xTotalSize = xMax - xOrigin
        val yTotalSize = yMax - yOrigin

        // Scale of both Axis of the Graph
        val yScale = yTotalSize / numOfYMarkers
        val xScale = xTotalSize / numOfXMarkers

        // This function draws the Axis and the Markers
        drawMargins(
            yMarkerCount = numOfYMarkers,
            xOrigin = xOrigin,
            xAxisMarkers = xAxisReadings,
            textColor = textColor,
            yUpperBound = yUpperReadingRange,
            yDividend = yDividend,
            yScale = yScale,
            xScale = xScale,
            xMax = xMax
        )

        // This function plots the Graph and joins the Line
        plotPoints(
            yAxisReadingsSet = yAxisReadings,
            xScale = xScale,
            yUpperBound = yUpperReadingRange,
            dotColor = dotColor,
            lineColor = lineColor,
            yDividend = yDividend,
            yScale = yScale
        )
    }
}

/**
 * This function draws the Margins and the Markers in the Graph
 *
 * @param yUpperBound This is the upper Marker Limit of the Graph
 * @param yDividend This is the difference between each graph Marker
 * @param xAxisMarkers This is the Markers that should be written in the X - Axis
 * @param yMarkerCount This is the Marker Count of the Y - Axis
 * @param xOrigin This is the Origin of X - Axis
 * @param yScale This is the scale of Y - Axis
 * @param textColor This is the text color of the Markers
 * @param xScale This is the scale of X - Axis
 * @param xMax This is the maximum floating coordinate where the graph ends
 */
private fun DrawScope.drawMargins(
    yUpperBound: Int,
    yDividend: Int,
    xAxisMarkers: List<String>,
    yMarkerCount: Int,
    xOrigin: Float,
    yScale: Float,
    textColor: Int,
    xScale: Float,
    xMax: Float
) {

    for (i in 1..yMarkerCount) {

        // This is the value of the current Y Axis Marker
        val currentYMarker = yUpperBound - (i - 1) * yDividend

        // This draws the String Marker
        drawContext.canvas.nativeCanvas.drawText(
            currentYMarker.toString(),
            xOrigin - 24f,
            (yScale * i) + 12f,
            Paint().apply {
                color = textColor
                textSize = 12.sp.toPx()
                textAlign = Paint.Align.LEFT
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            }
        )

        // This draws the Lines for the readings parallel to X Axis
        drawLine(
            start = Offset(
                x = xOrigin + 24f,
                y = yScale * i
            ),
            color = Color.Gray,
            end = Offset(
                x = xMax,
                y = yScale * i
            ),
            strokeWidth = 1f
        )
    }

    // This Draws the Y Markers below the Graph
    xAxisMarkers.forEachIndexed { index, currentMarker ->

        // This draws the String Marker
        drawContext.canvas.nativeCanvas.drawText(
            currentMarker,
            xScale * (index + 1),
            yScale * (yMarkerCount + 1),
            Paint().apply {
                color = textColor
                textSize = 12.sp.toPx()
                textAlign = Paint.Align.LEFT
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            }
        )
    }
}

/**
 * This is the function which plots the points in the Graph
 *
 * @param yAxisReadingsSet These contains the points of the Graph
 * @param xScale This is the xScale of the Graph
 * @param yUpperBound This is the upper bounds of the current Markers in the Graph
 * @param lineColor This is the color of the Line which will be showed in the curved lines
 * @param dotColor This is the color of the dots which will be shown in the points
 * @param yDividend This is the difference between the Markers of Y - Axis
 * @param yScale This is the scale of the Y - Axis
 */
private fun DrawScope.plotPoints(
    yAxisReadingsSet: List<List<Float>>,
    xScale: Float,
    yUpperBound: Int,
    lineColor: List<Color>,
    dotColor: List<Color>,
    yDividend: Int,
    yScale: Float
) {

    // This variable contains all the Offset of all the graph coordinates
    val graphCoordinatesList: MutableList<MutableList<Offset>> = mutableListOf()

    // Adding the Offsets to the Variable
    yAxisReadingsSet.forEachIndexed { coordinateSetIndex, coordinateSet ->

        // Calculates the coordinate of One Set of the List
        val graphCoordinates: MutableList<Offset> = mutableListOf()

        coordinateSet.forEachIndexed { index, fl ->

            val currentYCoordinate = (yScale + (yUpperBound - fl) * yScale / yDividend)

            // Adding the Coordinates of points in the same Set
            graphCoordinates.add(
                Offset(
                    x = 24f + (index + 1) * xScale,
                    y = currentYCoordinate
                )
            )
        }

        // Adding the coordinates of a whole Set in one Index of the Graph
        graphCoordinatesList.add(
            coordinateSetIndex,
            graphCoordinates
        )
    }

    // This loop makes the curved line between two points
    for (i in 0 until graphCoordinatesList.size) {

        // Path Variable
        val path = Path()

        // This is the current coordinate set
        val coordinates = graphCoordinatesList[i]

        // Moving to the start path of the the coordinate set to start making the Curved lines
        path.moveTo(
            coordinates[0].x,
            coordinates[0].y
        )

        // Inner Loop which draws the Lines from point to point of a single coordinate sets
        for (index in 0 until coordinates.size - 1) {

            // Control Points
            val control1X = (coordinates[index].x + coordinates[index + 1].x) / 2f
            val control1Y = coordinates[index].y
            val control2X = (coordinates[index].x + coordinates[index + 1].x) / 2f
            val control2Y = coordinates[index + 1].y

            // Defining the path from the last stayed to the next point
            path.cubicTo(
                x1 = control1X,
                y1 = control1Y,
                x2 = control2X,
                y2 = control2Y,
                x3 = coordinates[index + 1].x,
                y3 = coordinates[index + 1].y
            )
        }

        // Drawing path after defining all the points of a single coordinate set in the path
        drawPath(
            path = path,
            color = lineColor[i],
            style = Stroke(
                width = 4f
            )
        )
    }

    // This loop draws the circles or the points of the coordinates1
    graphCoordinatesList.forEachIndexed { index, offsets ->
        offsets.forEach {
            // This function draws the Circle points
            drawCircle(
                color = dotColor[index],
                radius = 8f,
                center = it
            )
        }
    }
}