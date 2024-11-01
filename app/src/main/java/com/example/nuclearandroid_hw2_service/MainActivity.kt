package com.example.nuclearandroid_hw2_service

import EventReminderWorker
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.nuclearandroid_hw2_service.R

import android.content.Context
import android.widget.Button
import java.util.UUID
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val workManager by lazy { WorkManager.getInstance(this) }
    private val prefs by lazy { getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    private val reviewKey = "review_left"
    private val reminderWorkIdKey = "reminder_work_id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val visitEventButton = findViewById<Button>(R.id.visitEventButton)
        val leaveReviewButton = findViewById<Button>(R.id.leaveReviewButton)
        prefs.edit().putBoolean(reviewKey, false).apply()

        val deleteReviewButton = findViewById<Button>(R.id.deleteReviewButton)


        visitEventButton.setOnClickListener {
            visitEvent()
        }

        leaveReviewButton.setOnClickListener {
            leaveReview()
        }

        deleteReviewButton.setOnClickListener {
            deleteReview()
        }
    }

    private fun visitEvent() {
        val reminderRequest = OneTimeWorkRequestBuilder<EventReminderWorker>()
            .setInitialDelay(3, TimeUnit.SECONDS)
            .build()

        prefs.edit().putString(reminderWorkIdKey, reminderRequest.id.toString()).apply()

        workManager.enqueue(reminderRequest)
    }

    private fun leaveReview() {
        prefs.edit().putBoolean(reviewKey, true).apply()

        val reminderWorkId = prefs.getString(reminderWorkIdKey, null)
        reminderWorkId?.let {
            workManager.cancelWorkById(UUID.fromString(it))
        }
    }

    private fun deleteReview() {
        prefs.edit().putBoolean(reviewKey, false).apply()
        prefs.edit().remove(reminderWorkIdKey).apply()


        val reminderWorkId = prefs.getString(reminderWorkIdKey, null)
        reminderWorkId?.let {
            workManager.cancelWorkById(UUID.fromString(it))
        }
    }
}



