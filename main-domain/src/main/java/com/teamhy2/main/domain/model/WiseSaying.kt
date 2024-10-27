package com.teamhy2.main.domain.model

data class WiseSaying(
    val quote: String,
    val author: String,
) {
    companion object {
        val DEFAULT =
            WiseSaying(
                quote = "우선 무엇이 되고자 하는지 자신에게 말하라.\n그리고 나서 할 일을 하라.",
                author = "에픽테토스",
            )
    }
}
