package com.gt.go4lunch.data.repositories

import com.gt.go4lunch.data.PlacesSearchApiResponse
import com.gt.go4lunch.utils.TLSSocketFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.util.*
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class GooglePlacesRepoImpl: GooglePlacesRepo {

    companion object {

        private var serviceNearbyRestaurant: GooglePlacesApi

        private const val KEY_NAME = "key"
        private const val API_KEY = "AIzaSyBFW3Nex_BK6xrra4taD3xKMXcJ0SdM700"

        private const val TYPE_NAME = "type"
        private const val TYPE_VALUE = "restaurant"

        private const val RADIUS_NAME = "radius"
        private const val RADIUS_VALUE = "10000"

        private const val INPUT_TYPE_NAME = "inputtype"
        private const val INPUT_TYPE_VALUE = "textquery"

        init {

            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers = trustManagerFactory.trustManagers
            if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
            }
            val trustManager = trustManagers[0] as X509TrustManager

            val paramsInterceptor = Interceptor{ chain ->
                var request = chain.request()
                val url = request.url()
                    .newBuilder()
                    .addQueryParameter(RADIUS_NAME, RADIUS_VALUE)
                    .addQueryParameter(TYPE_NAME, TYPE_VALUE)
                    .addQueryParameter(KEY_NAME, API_KEY )
                    .build()
                request = request.newBuilder().url(url).build()
                chain.proceed(request)
            }

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .sslSocketFactory(TLSSocketFactory(), trustManager)
                .addInterceptor(paramsInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            serviceNearbyRestaurant = retrofit.create(GooglePlacesApi::class.java)
        }
    }



    override fun getNearbyRestaurants(location: String): PlacesSearchApiResponse? {
        return serviceNearbyRestaurant.getNearbyRestaurants(location).execute().body()
    }
}