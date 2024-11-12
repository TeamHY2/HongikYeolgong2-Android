package com.benenfeldt.remote.token

/**
 * USER - 구글 로그인을 마치고, 회원가입을 한 유저
 *
 * GUEST - 구글 로그인을 했지만, 회원가입을 하지 않은 유저
 *
 * ADMIN - 관리자 계정, 하지만 앱으로 로그인 하는 경우는 없음
 * */

enum class TokenRole {
    USER,
    GUEST,
    ADMIN,
}
