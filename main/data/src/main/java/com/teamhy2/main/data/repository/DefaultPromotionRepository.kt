package com.teamhy2.main.data.repository

import com.teamhy2.main.data.datastore.PromotionDataStore
import com.teamhy2.main.domain.datasource.PromotionDataSource
import com.teamhy2.main.domain.model.Promotion
import com.teamhy2.main.domain.repository.PromotionRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class DefaultPromotionRepository @Inject constructor(
    private val dataSource: PromotionDataSource,
    private val dataStore: PromotionDataStore,
) : PromotionRepository {
    override val isPromotionDismissed: Flow<Boolean>
        get() = dataStore.isPromotionDismissed

    override suspend fun fetchPromotionData(): Result<Promotion> {
        return dataSource.fetchPromotionData()
    }

    override suspend fun savePromotionDismissPeriod(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<Unit> =
        runCatching {
            dataStore.savePromotionDismissPeriod(startDate, endDate)
        }
}
