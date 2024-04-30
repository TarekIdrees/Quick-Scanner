package com.tareq.barcodescanner.di

import com.tareq.data.local.ScanItemsSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScanItemsSourceModule {
    @Singleton
    @Provides
    fun provideScanItemsSource(): ScanItemsSource {
        return ScanItemsSource
    }
}