package com.teamhy2.onboarding.domain.repository

interface DepartmentRepository {
    suspend fun getAllDepartments(): List<String>
}
