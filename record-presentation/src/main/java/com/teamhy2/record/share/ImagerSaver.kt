package com.teamhy2.record.share

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore

/**
 * ImageSaver 인터페이스는 이미지를 저장 기능을 정의합니다.
 *
 * 구현체는 주어진 [bitmap]을 저장하고, 저장 성공 여부를 [onComplete] 콜백으로 전달합니다.
 */
interface ImageSaver {
    /**
     * 주어진 [bitmap]을 저장하고, 저장 성공 여부를 콜백 [onComplete]로 전달합니다.
     *
     * @param context 이미지를 저장할 때 필요한 [Context]
     * @param bitmap 저장할 [Bitmap] 이미지
     * @param onComplete 저장 성공 여부를 전달하는 콜백
     */
    fun saveImage(
        context: Context,
        bitmap: Bitmap,
        onComplete: (Boolean) -> Unit,
    )
}

/**
 * MediaStoreImageSaver는 ImageSaver 인터페이스의 MediaStore 기반 구현체입니다.
 * 이 객체는 이미지를 RELATIVE_PATH 경로에 저장합니다.
 */
object MediaStoreImageSaver : ImageSaver {
    private const val IMAGE_QUALITY: Int = 100
    private const val RELATIVE_PATH: String = "Pictures/ShareImages"
    private const val MIME_TYPE: String = "image/jpeg"

    /**
     * [context]를 통해 MediaStore에 접근하여, [bitmap]을 JPEG 포맷으로 압축 후 저장합니다.
     * 저장 성공 여부는 [onComplete] 콜백을 통해 전달됩니다.
     */
    override fun saveImage(
        context: Context,
        bitmap: Bitmap,
        onComplete: (Boolean) -> Unit,
    ) {
        val filename: String = "RecordShareImage_${System.currentTimeMillis()}.jpg"

        val contentValues: ContentValues =
            ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, MIME_TYPE)
                put(MediaStore.Images.Media.RELATIVE_PATH, RELATIVE_PATH)
            }

        val resolver: ContentResolver = context.contentResolver
        val imageUri: Uri? =
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (imageUri != null) {
            resolver.openOutputStream(imageUri)?.use { outputStream ->
                val success: Boolean =
                    bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, outputStream)
                onComplete(success)
            } ?: onComplete(false)
        } else {
            onComplete(false)
        }
    }
}
