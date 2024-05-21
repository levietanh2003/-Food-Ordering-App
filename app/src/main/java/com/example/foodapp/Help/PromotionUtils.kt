import java.text.SimpleDateFormat
import java.util.*

object PromotionUtils {

    private const val DATE_FORMAT = "dd/MM/yyyy HH:mm:ss"

//    fun calculatePromotionEnd(createdAt: String?, endAt: String?): String {
//        if (createdAt == null || endAt == null) {
//            return "Invalid date"
//        }
//
//        return try {
//            val createdAtMillis = createdAt.toLong() * 1000
//            val endAtMillis = endAt.toLong() * 1000
//            val currentTime = System.currentTimeMillis()
//            val remainingTime = endAtMillis - currentTime
//
//            if (remainingTime > 0) {
//                val days = remainingTime / (1000 * 60 * 60 * 24)
//                val hours = (remainingTime / (1000 * 60 * 60)) % 24
//                val minutes = (remainingTime / (1000 * 60)) % 60
//
//                "Ends in $days days, $hours hours, $minutes minutes"
//            } else {
//                "Promotion ended"
//            }
//        } catch (e: Exception) {
//            "Invalid timestamp"
//        }
//    }

    fun formatTimestamp(timestamp: String?): String {
        if (timestamp == null) return "Unknown date"
        return try {
            val date = Date(timestamp.toLong() * 1000)
            val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            dateFormat.format(date)
        } catch (e: Exception) {
            "Invalid date"
        }
    }

    fun getRemainingTime(createdAt: String?, endAt: String?): Long {
        if (createdAt == null || endAt == null) {
            return -1L
        }
        return try {
            val createdAtMillis = createdAt.toLong() * 1000
            val endAtMillis = endAt.toLong() * 1000
            val currentTime = System.currentTimeMillis()

            // Ensure the current time is between createdAt and endAt
            if (currentTime in createdAtMillis..endAtMillis) {
                endAtMillis - currentTime
            } else {
                -1L
            }
        } catch (e: Exception) {
            -1L
        }
    }

}
