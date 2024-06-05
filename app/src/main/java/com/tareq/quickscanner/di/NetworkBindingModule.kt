package com.tareq.quickscanner.di

import com.tareq.data.remote.ProductApi
import com.tareq.data.remote.ProductApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindingModule {
    @Singleton
    @Binds
    abstract fun bindProductApi(productApiImpl: ProductApiImpl): ProductApi
}