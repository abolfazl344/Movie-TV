package ir.abolfazl.abolmovie.model

import io.reactivex.Single
import ir.abolfazl.abolmovie.apiManager.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainRepository {

    private val apiService: ApiService

    init {

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

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun getPopularMovie() : Single<Movie> {

        return apiService.getPopularMovie()
    }

    fun getTopMovie() : Single<Movie>{

        return apiService.getTopMovie()
    }

    fun getNowPlaying() : Single<Movie>{

        return apiService.getNowPlayingMovie()
    }

    fun discoverMovie() : Single<Movie>{

        return apiService.discoverMovie()
    }

    fun discoverSerial() : Single<Serial>{

        return apiService.discoverSerial()
    }
}