import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nuclearandroid_hw2_service.R

class EventReminderWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val prefs = applicationContext.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val reviewLeft = prefs.getBoolean("review_left", false)

        if (!reviewLeft) {
            sendNotification("Пожалуйста, оставьте отзыв о посещенном мероприятии")
        }

        return Result.success()

    }

    private fun sendNotification(message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "event_reminder_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Review Reminder", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Напоминание")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            //.setAutoCancel(false)
            .build()

        notificationManager.notify(1, notification)
    }
}
