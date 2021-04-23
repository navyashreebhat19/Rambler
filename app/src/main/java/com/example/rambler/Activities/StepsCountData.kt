package com.example.rambler.Activities
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.rambler.Modules.StepCount
import com.example.rambler.Modules.getAccount
import com.example.rambler.Modules.getFitnessOptions
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.Bucket
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import java.util.*
import java.util.concurrent.TimeUnit

class StepsCountData() {
    val stepData = MutableLiveData<List<StepCount>>()

    fun reloadSteps(context: Context, numDays: Int) {
        Log.d(TAG,"Refresh days:" + numDays)

        val fitnessOptions = getFitnessOptions()
        val date = GregorianCalendar()
        date[Calendar.HOUR_OF_DAY] = 0
        date[Calendar.MINUTE] = 0
        date[Calendar.SECOND] = 0
        date[Calendar.MILLISECOND] = 0

        date.add(Calendar.DAY_OF_MONTH, 1)
        val endTime = date.timeInMillis
        date.add(Calendar.DATE, -numDays)
        val startTime = date.timeInMillis

        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()

        val account =
            getAccount(context, fitnessOptions)
        Fitness.getHistoryClient(context, account)
            .readData(readRequest)
            .addOnSuccessListener { response ->
                Log.d(TAG,"Retrieve Steps" + response.dataSets)
                Log.d(TAG,"Retrieve steps success")
                val summaries = getStepCounts(response.buckets)
                Log.d(TAG,"Step count summary" + summaries.size)

                stepData.value = summaries
            }
            .addOnFailureListener { e -> Log.e(TAG,"getsteps failure" + e.message) }

    }

    private fun getStepCounts(buckets: List<Bucket>): List<StepCount> {
        val stepSummaries = mutableListOf<StepCount>()

        for (bucket in buckets) {
            for (dataSet in bucket.dataSets) {
                for (dataPoint in dataSet.dataPoints) {
                    val start = dataPoint.getStartTime(TimeUnit.MILLISECONDS)
                    val date = Date(start)

                    for (field in dataPoint.dataType.fields) {
                        val value = dataPoint.getValue(field).asInt()
                        val stepSummary =
                            StepCount(
                                date,
                                value
                            )
                        Log.d(TAG, "Add Step summary" + stepSummary)
                        stepSummaries.add(stepSummary)
                    }
                }

            }
        }

        stepSummaries.sortBy { it.date }
        return stepSummaries
    }
}
