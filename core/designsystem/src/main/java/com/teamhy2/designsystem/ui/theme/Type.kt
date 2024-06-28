package com.teamhy2.designsystem.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.teamhy2.designsystem.R

val SuiteBold = FontFamily(Font(R.font.suite_bold, FontWeight.Bold))
val SuiteSemiBold = FontFamily(Font(R.font.suite_semi_bold, FontWeight.SemiBold))
val SuiteMedium = FontFamily(Font(R.font.suite_medium, FontWeight.Medium))
val SuiteExtraBold = FontFamily(Font(R.font.suite_extra_bold, FontWeight.ExtraBold))

val PretendardSemiBold = FontFamily(Font(R.font.pretendard_semi_bold, FontWeight.SemiBold))
val PretendardRegular = FontFamily(Font(R.font.pretendard_regular, FontWeight.Normal))

@Stable
class HY2Typography(
	head: TextStyle,
	title01: TextStyle,
	title02: TextStyle,
	title03: TextStyle,
	body01: TextStyle,
	body02: TextStyle,
	body03: TextStyle,
	body04: TextStyle,
	body05: TextStyle,
	body06: TextStyle,
	caption: TextStyle
) {
	var head: TextStyle by mutableStateOf(head)
		private set
	var title01: TextStyle by mutableStateOf(title01)
		private set
	var title02: TextStyle by mutableStateOf(title02)
		private set
	var title03: TextStyle by mutableStateOf(title03)
		private set
	var body01: TextStyle by mutableStateOf(body01)
		private set
	var body02: TextStyle by mutableStateOf(body02)
		private set
	var body03: TextStyle by mutableStateOf(body03)
		private set
	var body04: TextStyle by mutableStateOf(body04)
		private set
	var body05: TextStyle by mutableStateOf(body05)
		private set
	var body06: TextStyle by mutableStateOf(body06)
		private set
	var caption: TextStyle by mutableStateOf(caption)
		private set

	fun copy(
		head: TextStyle = this.head,
		title01: TextStyle = this.title01,
		title02: TextStyle = this.title02,
		title03: TextStyle = this.title03,
		body01: TextStyle = this.body01,
		body02: TextStyle = this.body02,
		body03: TextStyle = this.body03,
		body04: TextStyle = this.body04,
		body05: TextStyle = this.body05,
		body06: TextStyle = this.body06,
		caption: TextStyle = this.caption
	): HY2Typography = HY2Typography(
		head,
		title01,
		title02,
		title03,
		body01,
		body02,
		body03,
		body04,
		body05,
		body06,
		caption
	)

	fun update(other: HY2Typography) {
		head = other.head
		title01 = other.title01
		title02 = other.title02
		title03 = other.title03
		body01 = other.body01
		body02 = other.body02
		body03 = other.body03
		body04 = other.body04
		body05 = other.body05
		body06 = other.body06
		caption = other.caption
	}
}

@Composable
fun HY2Typography(): HY2Typography {
	return HY2Typography(
		head = TextStyle(
			fontFamily = SuiteSemiBold,
			fontSize = 18.sp,
			lineHeight = 22.sp
		),
		title01 = TextStyle(
			fontFamily = SuiteBold,
			fontSize = 24.sp,
			lineHeight = 30.sp
		),
		title02 = TextStyle(
			fontFamily = PretendardSemiBold,
			fontSize = 18.sp,
			lineHeight = 22.sp
		),
		title03 = TextStyle(
			fontFamily = PretendardSemiBold,
			fontSize = 16.sp,
			lineHeight = 26.sp
		),
		body01 = TextStyle(
			fontFamily = SuiteExtraBold,
			fontSize = 30.sp,
			lineHeight = 32.sp
		),
		body02 = TextStyle(
			fontFamily = SuiteSemiBold,
			fontSize = 16.sp,
			lineHeight = 20.sp
		),
		body03 = TextStyle(
			fontFamily = SuiteMedium,
			fontSize = 14.sp,
			lineHeight = 32.sp
		),
		body04 = TextStyle(
			fontFamily = SuiteMedium,
			fontSize = 12.sp,
			lineHeight = 15.sp
		),
		body05 = TextStyle(
			fontFamily = PretendardRegular,
			fontSize = 16.sp,
			lineHeight = 26.sp
		),
		body06 = TextStyle(
			fontFamily = PretendardRegular,
			fontSize = 18.sp,
			lineHeight = 26.sp
		),
		caption = TextStyle(
			fontFamily = PretendardRegular,
			fontSize = 12.sp,
			lineHeight = 18.sp
		)
	)
}
