import com.teamhy2.app.configureHiltAndroid
import com.teamhy2.app.configureKotestAndroid
import com.teamhy2.app.configureKotlinAndroid

plugins {
	id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureKotestAndroid()
