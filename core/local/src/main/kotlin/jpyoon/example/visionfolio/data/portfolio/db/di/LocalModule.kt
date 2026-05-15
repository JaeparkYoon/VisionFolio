package jpyoon.example.visionfolio.data.portfolio.db.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jpyoon.example.visionfolio.data.portfolio.db.VfDatabase
import jpyoon.example.visionfolio.data.portfolio.db.chat.ChatDao
import jpyoon.example.visionfolio.data.portfolio.db.dividend.DividendCacheDao
import jpyoon.example.visionfolio.data.portfolio.db.dividendoverride.DividendOverrideDao
import jpyoon.example.visionfolio.data.portfolio.db.holding.HoldingDao
import jpyoon.example.visionfolio.data.portfolio.db.importsource.ImportSourceDao
import jpyoon.example.visionfolio.data.portfolio.db.returns.ReturnEntryDao
import jpyoon.example.visionfolio.data.portfolio.db.snapshot.SnapshotDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideVfDatabase(@ApplicationContext context: Context): VfDatabase =
        VfDatabase.get(context)

    @Provides
    fun provideHoldingDao(database: VfDatabase): HoldingDao = database.holdingDao()

    @Provides
    fun provideSnapshotDao(database: VfDatabase): SnapshotDao = database.snapshotDao()

    @Provides
    fun provideImportSourceDao(database: VfDatabase): ImportSourceDao = database.importSourceDao()

    @Provides
    fun provideDividendCacheDao(database: VfDatabase): DividendCacheDao = database.dividendCacheDao()

    @Provides
    fun provideChatDao(database: VfDatabase): ChatDao = database.chatDao()

    @Provides
    fun provideReturnEntryDao(database: VfDatabase): ReturnEntryDao = database.returnEntryDao()

    @Provides
    fun provideDividendOverrideDao(database: VfDatabase): DividendOverrideDao = database.dividendOverrideDao()
}
