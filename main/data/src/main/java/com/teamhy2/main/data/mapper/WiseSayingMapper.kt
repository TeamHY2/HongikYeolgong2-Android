package com.teamhy2.main.data.mapper

import com.benenfeldt.remote.dto.WiseSayingResponse
import com.teamhy2.main.domain.model.WiseSaying

fun WiseSayingResponse.toDomain(): WiseSaying {
    return WiseSaying(
        quote = quote,
        author = author,
    )
}
