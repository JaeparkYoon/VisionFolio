package jpyoon.example.visionfolio.data.fingerprint

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import me.tatarka.inject.annotations.Inject

class ScreenshotFingerprinter @Inject constructor(
    private val context: Context,
) {

    suspend fun compute(uri: String): Long {
        val bitmap = context.contentResolver.openInputStream(Uri.parse(uri))?.use { stream ->
            BitmapFactory.decodeStream(stream)
        } ?: return 0L

        val headerHeight = (bitmap.height * 0.2).toInt().coerceAtLeast(1)
        val cropped = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, headerHeight)
        if (cropped !== bitmap) bitmap.recycle()

        val scaled = Bitmap.createScaledBitmap(cropped, HASH_SIZE, HASH_SIZE, true)
        if (scaled !== cropped) cropped.recycle()

        val pixels = IntArray(HASH_SIZE * HASH_SIZE)
        scaled.getPixels(pixels, 0, HASH_SIZE, 0, 0, HASH_SIZE, HASH_SIZE)
        scaled.recycle()

        val grays = pixels.map { px ->
            (0.299 * Color.red(px) + 0.587 * Color.green(px) + 0.114 * Color.blue(px))
        }
        val avg = grays.average()

        var hash = 0L
        for (i in grays.indices) {
            if (grays[i] >= avg) {
                hash = hash or (1L shl i)
            }
        }
        return hash
    }

    fun isSimilar(a: Long, b: Long, threshold: Int = DEFAULT_THRESHOLD): Boolean =
        hammingDistance(a, b) <= threshold

    private fun hammingDistance(a: Long, b: Long): Int =
        (a xor b).countOneBits()

    companion object {
        private const val HASH_SIZE = 8
        private const val DEFAULT_THRESHOLD = 10
    }
}
