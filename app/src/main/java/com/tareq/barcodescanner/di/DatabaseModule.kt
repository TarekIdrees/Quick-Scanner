package com.tareq.barcodescanner.di

import android.content.Context
import androidx.room.Room
import com.tareq.data.local.QrBrCodeDatabase
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
    ): QrBrCodeDatabase {
        return Room
            .databaseBuilder(context, QrBrCodeDatabase::class.java, databaseName)
            .build()
    }

    @Singleton
    @Provides
    @Named("databaseName")
    fun provideDataBaseName(): String = "qr_br_code_db"

    @Provides
    @Singleton
    fun provideProductDao(database: QrBrCodeDatabase) = database.getProductDao()
}