package com.teamhy2.record.share

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.FileProvider
import com.teamhy2.record.share.InstagramImageSharer.CACHE_FOLDER
import com.teamhy2.record.share.InstagramImageSharer.SHARE_FILE_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

/**
 * ImageSharer 인터페이스는 이미지를 공유하는 기능을 정의합니다.
 *
 * 구현체는 주어진 [bitmap] 이미지를 공유하고, 사용자가 원하는 공유 채널(예, 인스타그램)으로 전송하는 로직을 포함해야 합니다.
 */
interface ImageSharer {
    /**
     * 주어진 [bitmap] 이미지를 공유합니다.
     *
     * @param context 공유 작업에 필요한 [Context]
     * @param bitmap 공유할 [Bitmap] 이미지
     */
    fun shareImage(
        context: Context,
        bitmap: Bitmap,
    ): Unit
}

/**
 * InstagramImageSharer는 ImageSharer 인터페이스의 인스타그램 전용 구현체입니다.
 *
 * 이 객체는 다음의 작업을 수행합니다:
 * 1. 캐시 디렉토리 내에 "images" 폴더를 생성합니다.
 * 2. [bitmap]을 JPEG 형식으로 [CACHE_FOLDER]에 [SHARE_FILE_NAME] 이름으로 저장합니다.
 * 3. FileProvider를 통해 저장된 파일의 content Uri를 획득합니다.
 * 4. 인텐트를 생성하여 인스타그램 앱으로 공유합니다.
 * 5. 공유 후 5초 뒤에 저장된 파일을 삭제합니다.
 */
object InstagramImageSharer : ImageSharer {
    private const val IMAGE_QUALITY: Int = 100
    private const val CACHE_FOLDER: String = "images"
    private const val SHARE_FILE_NAME: String = "share_image.jpg"

    /**
     * [context]를 통해 [bitmap] 이미지를 캐시 디렉토리에 저장하고,
     * FileProvider를 이용해 content Uri를 생성한 후, 인스타그램 앱으로 공유하는 인텐트를 발행합니다.
     *
     * @param context 공유 작업에 필요한 [Context]
     * @param bitmap 공유할 [Bitmap] 이미지
     */
    override fun shareImage(
        context: Context,
        bitmap: Bitmap,
    ) {
        val cachePath: File = File(context.cacheDir, CACHE_FOLDER).apply { mkdirs() }
        val file: File = File(cachePath, SHARE_FILE_NAME)

        FileOutputStream(file).use { out: FileOutputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, out)
        }

        val fileUri =
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file,
            )

        val shareIntent: Intent =
            Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, fileUri)
                setPackage("com.instagram.android")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

        context.startActivity(Intent.createChooser(shareIntent, "Share Image"))

        CoroutineScope(Dispatchers.IO).launch {
            delay(5000L)
            file.delete()
        }
    }
}
