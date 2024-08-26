package com.teamhy2.onboarding.data.repository

import com.teamhy2.onboarding.domain.repository.DepartmentRepository
import javax.inject.Inject

class InMemoryDepartmentRepository
    @Inject
    constructor() : DepartmentRepository {
        override suspend fun getAllDepartments(): List<String> {
            return listOf(
                "건설도시공학부",
                "건설환경공학과",
                "건축학부",
                "경영학부",
                "경제학부",
                "공연예술학부",
                "금속조형디자인과",
                "기계시스템디자인공학과",
                "국어교육과",
                "국어국문학과",
                "도시공학과",
                "독어독문학과",
                "동양화과",
                "도예유리과",
                "디자인경영융합학부",
                "디자인·예술경영학부",
                "디자인학부",
                "물리교육과",
                "법학부",
                "불어불문학과",
                "사회교육과",
                "산업디자인학과",
                "산업·데이터공학과",
                "섬유미술패션디자인과",
                "수학교육과",
                "신소재화공시스템공학부",
                "영어교육과",
                "영어영문학과",
                "역사교육과",
                "예술학과",
                "응용미술학과",
                "일본어문학과",
                "전자전기공학부",
                "조소과",
                "컴퓨터공학과",
                "판화과",
                "프랑스어문학과",
                "회화과",
            )
        }
    }
