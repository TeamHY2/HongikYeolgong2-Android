import com.teamhy2.app.configureCoroutineAndroid
import com.teamhy2.app.configureHiltAndroid
import com.teamhy2.app.configureKotestAndroid
import com.teamhy2.app.configureKotlinAndroid
import com.teamhy2.app.configureCoroutinesTest

plugins {
	id("com.android.library")
}

configureKotlinAndroid()
configureCoroutineAndroid()
configureHiltAndroid()
configureKotestAndroid()
configureCoroutinesTest()
