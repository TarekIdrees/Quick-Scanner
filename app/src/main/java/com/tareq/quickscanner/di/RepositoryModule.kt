package com.tareq.quickscanner.di

import com.tareq.domain.repository.QuickScannerRepository
import com.tareq.repository.QuickScannerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindScannerRepository(repository: QuickScannerRepositoryImpl): QuickScannerRepository
}