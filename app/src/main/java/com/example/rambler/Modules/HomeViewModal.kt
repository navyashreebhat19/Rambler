package com.example.rambler.Modules

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.rambler.Activities.StepsCountData


class HomeViewModal(application: Application) : AndroidViewModel(application) {
    private val app = application
    private val stepsFolder = StepsCountData()
    private val stepInfo = stepsFolder.stepData
    private val sortedData = MediatorLiveData<List<StepCount>>()
    private var ascending = false

    init {
        sortedData.addSource(stepInfo) { list: List<StepCount>? ->
            list?.let {
                sortedData.value = sortSummaries(list)
            }
        }
    }

    fun getSortedStepsSummaries(): LiveData<List<StepCount>> {
        return sortedData
    }

    fun setSortedAscending(): Boolean {
        return ascending
    }

    private fun sortSummaries(list: List<StepCount>): List<StepCount>? {
        return if (ascending) {
            list.sortedBy { it.date }
        } else {
            list.sortedByDescending { it.date }
        }
    }

    fun reverseOrder() {
        ascending = !ascending
        stepInfo.value?.let { list ->
            Log.d(TAG,"Sort in ascending" + setSortedAscending())
            sortedData.value = sortSummaries(list)
        }
    }

    fun reloadSteps() {
        stepsFolder.reloadSteps(app, NUM_DAYS)
    }
}