import java.text.SimpleDateFormat
import java.util.*

object PromotionUtils {

    private const val DATE_FORMAT = "dd/MM/yyyy HH:mm:ss"
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
