package com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.anirban.mbitsassignment.ui.theme.InterFontFamily
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
        CardViewUI(
            cardHeading = "Monthly Progress",
            body = {}
        )
    }
}

/**
 * This function is the Card view Template used
 *
 * @param modifier To be passed by the Parent Class
 * @param cardHeading Heading or title of this Card
 * @param body The UI which will be drawn inside this card
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardViewUI(
    modifier: Modifier = Modifier,
    cardHeading: String,
    body: @Composable () -> Unit
) {

    // This function draws an elevated Card View
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        shape = RoundedCornerShape(8.dp)
    ) {

        // Heading Text of the CardView
        Text(
            text = cardHeading,
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp),

            // Text and Font Properties
            textAlign = TextAlign.Center,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.W800,
            fontSize = 16.sp
        )

        // Height Spacing of 4 dp
        Spacer(modifier = Modifier.height(4.dp))

        // Graph Body Function
        body()
    }
}