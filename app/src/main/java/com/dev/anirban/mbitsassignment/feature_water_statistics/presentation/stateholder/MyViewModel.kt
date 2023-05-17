package com.dev.anirban.mbitsassignment.feature_water_statistics.presentation.stateholder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 *  This is the viewModel class
 */
class MyViewModel : androidx.lifecycle.ViewModel() {

    // User Input of Float
    var inputData: String by mutableStateOf("")
        private set

    // Y Axis Reading Data Coordinate Sets
    var yAxisReadingsData: MutableList<MutableList<Float>> by mutableStateOf(
        mutableListOf(
            mutableListOf(
                6f, 5f, 4f, 6f, 7.5f, 7f, 6f
            )
        )
    )
        private set

    // This function is called when the user inputs something
    fun onInputChange(newValue: String) {
        inputData = newValue
    }

    // This function is called when the user clicks the submit Button
    fun updateList() {

        // Creating another List
        val temp: MutableList<MutableList<Float>> = mutableListOf(
            mutableListOf()
        )

        // Filling the temporary List
        yAxisReadingsData.forEachIndexed { index, floats ->
            floats.forEach {
                temp[index].add(it)
            }
        }

        // Replacing the list with the new Item
        temp[0].removeAt(2)
        temp[0].add(2, inputData.toFloat())

        // Assigning the formatted list to the old List
        yAxisReadingsData = temp
    }
}