package com.tareq.barcodescanner.di

import android.content.Context
import androidx.room.Room
import com.tareq.data.local.QuickScannerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        @Named("databaseName") databaseName: String
    ): QuickScannerDatabase {
        return Room
            .databaseBuilder(context, QuickScannerDatabase::class.java, databaseName)
            .build()
    }

    @Singleton
    @Provides
    @Named("databaseName")
    fun provideDataBaseName(): String = "quick_scanner_db"

    @Provides
    @Singleton
    fun provideProductDao(database: QuickScannerDatabase) = database.getProductDao()

    @Provides
    @Singleton
    fun provideWifiDao(database: QuickScannerDatabase) = database.getWifiDao()

    @Provides
    @Singleton
    fun provideContactDao(database: QuickScannerDatabase) = database.getContactDao()

    @Provides
    @Singleton
    fun provideEmailDao(database: QuickScannerDatabase) = database.getEmailDao()
}