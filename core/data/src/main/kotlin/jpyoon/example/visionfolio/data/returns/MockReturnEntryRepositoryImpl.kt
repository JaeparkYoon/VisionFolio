package jpyoon.example.visionfolio.data.returns

import jpyoon.example.visionfolio.data.di.AppCoroutineScope
import jpyoon.example.visionfolio.data.portfolio.db.returns.ReturnEntryDao
import jpyoon.example.visionfolio.data.portfolio.db.returns.toDomain
import jpyoon.example.visionfolio.data.portfolio.db.returns.toEntity
import jpyoon.example.visionfolio.data.repository.ReturnEntryRepository
import jpyoon.example.visionfolio.domain.model.MonthlyReturn
import jpyoon.example.visionfolio.domain.model.ReturnCategory
import jpyoon.example.visionfolio.domain.model.ReturnEntry
import jpyoon.example.visionfolio.domain.model.YtdSummary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockReturnEntryRepositoryImpl @Inject constructor(
    private val dao: ReturnEntryDao,
    @param:AppCoroutineScope private val appScope: CoroutineScope,
) : ReturnEntryRepository {

    init {
        appScope.launch(Dispatchers.IO) {
            if (dao.count() == 0) {
                SEED_ENTRIES.forEach { dao.upsert(it.toEntity()) }
            }
        }
    }

    override fun observeByYear(year: Int): Flow<List<ReturnEntry>> =
        dao.observeByYear(year).map { list -> list.map { it.toDomain() } }

    override fun observeAll(): Flow<List<ReturnEntry>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun upsert(entry: ReturnEntry) = dao.upsert(entry.toEntity())

    override suspend fun delete(id: String) = dao.deleteById(id)

    override suspend fun getYtdSummary(year: Int): YtdSummary {
        val entries = dao.observeByYear(year).first().map { it.toDomain() }
        val incomeCategories = setOf(ReturnCategory.SALARY, ReturnCategory.SIDE_INCOME, ReturnCategory.BONUS)
        val totalIncome = entries.filter { it.category in incomeCategories }.sumOf { it.amount }
        val totalInvestment = entries.filter { it.category == ReturnCategory.INVESTMENT }.sumOf { it.amount }
        val savingsRate = if (totalIncome > 0) totalInvestment.toDouble() / totalIncome * 100.0 else 0.0
        val monthly = (1..12).map { m ->
            val monthEntries = entries.filter { it.month == m }
            MonthlyReturn(
                month = m,
                income = monthEntries.filter { it.category in incomeCategories }.sumOf { it.amount },
                investment = monthEntries.filter { it.category == ReturnCategory.INVESTMENT }.sumOf { it.amount },
            )
        }
        return YtdSummary(
            year = year,
            totalIncome = totalIncome,
            totalInvestment = totalInvestment,
            savingsRate = savingsRate,
            monthlyBreakdown = monthly,
        )
    }

    companion object {
        private val SEED_ENTRIES: List<ReturnEntry> = buildList {
            val year = 2025
            for (m in 1..6) {
                add(ReturnEntry("salary-$year-$m", year, m, ReturnCategory.SALARY, 4_500_000L, "월급"))
                add(ReturnEntry("invest-$year-$m", year, m, ReturnCategory.INVESTMENT, 1_500_000L, "적립식 투자"))
            }
            add(ReturnEntry("bonus-$year-1", year, 1, ReturnCategory.BONUS, 3_000_000L, "연말 상여금"))
            add(ReturnEntry("side-$year-3", year, 3, ReturnCategory.SIDE_INCOME, 800_000L, "프리랜서 수입"))
        }
    }
}
