import com.teamhy2.app.configureCoroutineKotlin
import com.teamhy2.app.configureKotest
import com.teamhy2.app.configureKotlin

plugins {
	kotlin("jvm")
}

kotlin {
    jvmToolchain(17)
}

configureKotlin()
configureKotest()
configureCoroutineKotlin()
