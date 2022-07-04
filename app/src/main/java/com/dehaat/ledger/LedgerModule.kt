package com.dehaat.ledger

import com.dehaat.androidbase.coroutine.Dispatchers
import com.dehaat.androidbase.coroutine.IDispatchers
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lib.dehaat.ledger.data.LedgerRepository
import lib.dehaat.ledger.data.source.ILedgerDataSource
import lib.dehaat.ledger.domain.ILedgerRepository
import lib.dehaat.ledger.framework.network.LedgerAPIService
import lib.dehaat.ledger.framework.network.LedgerDataSource
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class LedgerModule {

    @Binds
    abstract fun provideLedgerDataSource(
        remote: LedgerDataSource
    ): ILedgerDataSource

    @Binds
    abstract fun provideLedgerRepository(impl: LedgerRepository): ILedgerRepository

    companion object {

        @Provides
        fun provideLedgerAPIService(
            retrofit: Retrofit
        ): LedgerAPIService = retrofit.create(LedgerAPIService::class.java)

        @Provides
        fun provideDispatchers(): IDispatchers = Dispatchers()
    }

}