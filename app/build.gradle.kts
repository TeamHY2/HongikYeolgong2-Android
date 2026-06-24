import java.io.FileInputStream
import java.util.Properties

plugins {
    id("hongikyeolgong2.android.application")
    alias(libs.plugins.google.services)
    id("com.google.firebase.crashlytics")
}

// local.properties(로컬 개발용)에서 서명 정보를 읽되, CI에서는 환경 변수가 우선한다.
val localProperties =
    Properties().apply {
        val file = rootProject.file("local.properties")
        if (file.exists()) {
            FileInputStream(file).use { load(it) }
        }
    }

// 우선순위: 환경 변수(CI) > local.properties(로컬). 둘 다 없으면 null.
fun signingProperty(
    propertyKey: String,
    envKey: String,
): String? =
    (System.getenv(envKey) ?: localProperties.getProperty(propertyKey))
        ?.trim()
        ?.removeSurrounding("\"")
        ?.takeIf { it.isNotBlank() }

val keystorePath = signingProperty("storeFile", "KEYSTORE_FILE")
// 키스토어 파일이 실제로 존재할 때만 release 서명을 구성한다.
// (키스토어가 없는 일반 CI 빌드/PR 빌드는 기존처럼 서명 없이 통과)
val hasReleaseSigning = keystorePath != null && file(keystorePath).exists()

android {
    namespace = "com.teamhy2.hongikyeolgong2"

    defaultConfig {
        applicationId = "com.teamhy2.hongikyeolgong2"
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
    }

    signingConfigs {
        if (hasReleaseSigning) {
            create("release") {
                storeFile = file(keystorePath!!)
                storePassword = signingProperty("storePassword", "KEYSTORE_PASSWORD")
                keyAlias = signingProperty("keyAlias", "KEY_ALIAS")
                keyPassword = signingProperty("keyPassword", "KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            manifestPlaceholders.putAll(
                mapOf(
                    "appIcon" to "@mipmap/ic_app_logo_debug",
                    "roundIcon" to "@mipmap/ic_app_logo_debug_round",
                ),
            )
        }

        release {
            isDebuggable = false
            if (hasReleaseSigning) {
                signingConfig = signingConfigs.getByName("release")
            }
            manifestPlaceholders.putAll(
                mapOf(
                    "appIcon" to "@mipmap/ic_app_logo",
                    "roundIcon" to "@mipmap/ic_app_logo_round",
                ),
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.core.notification)
    implementation(projects.core.designsystem)
    implementation(projects.core.remote)
    implementation(projects.core.auth)
    implementation(projects.core.fcm)

    implementation(projects.main.presentation)
    implementation(projects.main.data)
    implementation(projects.main.domain)

    implementation(projects.friend.presentation)
    implementation(projects.friend.data)
    implementation(projects.friend.domain)

    implementation(projects.onboarding.presentation)
    implementation(projects.onboarding.data)
    implementation(projects.onboarding.domain)

    implementation(projects.setting.presentation)
    implementation(projects.setting.data)
    implementation(projects.setting.domain)

    implementation(projects.timer.presentation)
    implementation(projects.timer.data)
    implementation(projects.timer.domain)

    implementation(projects.ranking.domain)
    implementation(projects.ranking.data)
    implementation(projects.ranking.presentation)

    implementation(projects.record.domain)
    implementation(projects.record.data)
    implementation(projects.record.presentation)

    implementation(projects.user.data)
    implementation(projects.user.domain)

    implementation(projects.notification.data)
    implementation(projects.notification.domain)

    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)
    implementation(libs.firebase.messaging.ktx)

    implementation(libs.accompanist.permissions)
}
