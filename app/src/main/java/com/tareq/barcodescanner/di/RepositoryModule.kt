package com.tareq.barcodescanner.di

import com.tareq.repository.ScannerRepository
import com.tareq.repository.ScannerRepositoryImpl
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
    abstract fun bindScannerRepository(repository: ScannerRepositoryImpl): ScannerRepository
}