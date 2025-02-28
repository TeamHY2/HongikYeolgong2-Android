package com.teamhy2.record.share

object RecordShareImageManager {
    val imageSaver: ImageSaver = MediaStoreImageSaver
    val imageSharer: ImageSharer = InstagramImageSharer
}
