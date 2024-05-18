package com.tareq.barcodescanner.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://api.barcodelookup.com/"
    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            expectSuccess = true
            defaultRequest {
                url(BASE_URL)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("HTTP Client", message)
                    }
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }
}