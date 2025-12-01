package com.teamhy2.friend.data.mapper

import com.benenfeldt.remote.dto.FriendStudyResponse
import com.teamhy2.friend.domain.model.Friend
import com.teamhy2.main.domain.util.parseTimeToSeconds
import kotlin.time.Duration.Companion.seconds

fun FriendStudyResponse.toDomain(): Friend {
    return Friend(
        id = friendId,
        userId = userId,
        nickname = friendNickname,
        studyTime = parseTimeToSeconds(studyTime).seconds,
    )
}
