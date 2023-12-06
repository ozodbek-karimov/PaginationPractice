package pl.ozodbek.paginationpractice.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import kotlinx.serialization.json.Json
import pl.ozodbek.paginationpractice.util.Constants.Companion.BASE_URL
import pl.ozodbek.paginationpractice.data.FakeStoreAPI
import retrofit2.Converter

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15,TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun createSerializationConverterFactory(): Converter.Factory {
        val contentType = "application/json".toMediaType()
        val json = Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
            coerceInputValues = true
            allowSpecialFloatingPointValues = true
            isLenient = true
            prettyPrint = true
        }

        return json.asConverterFactory(contentType)
    }



    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        createSerializationConverterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(createSerializationConverterFactory)
            .build()
    }



    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): FakeStoreAPI {
        return retrofit.create(FakeStoreAPI::class.java)
    }
}
