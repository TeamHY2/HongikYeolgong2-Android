package com.teamhy2.record.share

/**
 * RecordShareImageManager는 이미지 저장(ImageSaver)과 공유(ImageSharer) 기능을
 * 캡슐화한 싱글톤 객체입니다.
 */
object RecordShareImageManager {
    val imageSaver: ImageSaver = MediaStoreImageSaver
    val imageSharer: ImageSharer = InstagramImageSharer
}
