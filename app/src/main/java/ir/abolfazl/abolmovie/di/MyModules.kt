package ir.abolfazl.abolmovie.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.abolfazl.abolmovie.utils.BASE_URL
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.model.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyModules {

    @Provides
    @Singleton
    fun provideGlide(@ApplicationContext context:Context) : RequestManager{

        return Glide.with(context)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit{
        val client = OkHttpClient
            .Builder()
            .addInterceptor {
                val oldRequest = it.request()
                val newRequest = oldRequest.newBuilder()
                newRequest.addHeader(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0YjgzMzdhYWI2YWQ5ODRlMjYyZWZjNjVhOGI4MzIwOSIsInN1YiI6IjY0YmQ3NTEwZTlkYTY5MDEyZTBlNjhlYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.-WyF-jn66efec1xQttkbkM6HF_q94KzlJAMVp2LjOTU"
                )
                newRequest.addHeader("accept", "application/json")
                it.proceed(newRequest.build())

            }.build()

        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : ApiService{

        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(apiService: ApiService) : MainRepository{
        return MainRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideFirebase() : FirebaseAuth {

        return Firebase.auth
    }
}