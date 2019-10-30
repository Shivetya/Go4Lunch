package com.gt.go4lunch.data.repositories.places

import com.gt.go4lunch.data.PlacesDetailsApiResponse
import com.gt.go4lunch.data.PlacesSearchApiResponse
import com.gt.go4lunch.utils.ApiKeyGitIgnore
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
        private var serviceDetailsRestaurant: GooglePlacesApi

        private const val KEY_NAME = "key"
        private const val API_KEY = ApiKeyGitIgnore.API_PLACES_KEY

        private const val TYPE_NAME = "type"
        private const val TYPE_VALUE = "restaurant"

        private const val RADIUS_NAME = "radius"
        private const val RADIUS_VALUE = "10000"

        private const val INPUT_TYPE_NAME = "inputtype"
        private const val INPUT_TYPE_VALUE = "textquery"

        private const val FIELDS_NAME = "fields"
        private const val FIELDS_VALUE = "opening_hours,rating"

        private const val MAXWIDTH_NAME = "maxwidth"
        private const val MAXWIDTH_VALUE = "200"

        private const val MAXHEIGHT_NAME = "maxheight"
        private const val MAXHEIGHT_VALUE = "200"

        init {

            //first part for nearby Restaurants request

            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers = trustManagerFactory.trustManagers
            if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
            }
            val trustManager = trustManagers[0] as X509TrustManager

            val paramsInterceptorNearbyRestaurant = Interceptor{ chain ->
                var request = chain.request()
                val url = request.url()
                    .newBuilder()
                    .addQueryParameter(
                        RADIUS_NAME,
                        RADIUS_VALUE
                    )
                    .addQueryParameter(
                        TYPE_NAME,
                        TYPE_VALUE
                    )
                    .addQueryParameter(
                        KEY_NAME,
                        API_KEY
                    )
                    .build()
                request = request.newBuilder().url(url).build()
                chain.proceed(request)
            }

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val clientNearbyRestaurant = OkHttpClient.Builder()
                .sslSocketFactory(TLSSocketFactory(), trustManager)
                .addInterceptor(paramsInterceptorNearbyRestaurant)
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofitNearbyRestaurant = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .client(clientNearbyRestaurant)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            serviceNearbyRestaurant = retrofitNearbyRestaurant.create(
                GooglePlacesApi::class.java)


            //second part for Details restaurant request

            val paramsInterceptorDetailsRestaurant = Interceptor{ chain ->
                var request = chain.request()
                val url = request.url()
                    .newBuilder()
                    .addQueryParameter(
                        FIELDS_NAME,
                        FIELDS_VALUE
                    )
                    .addQueryParameter(
                        KEY_NAME,
                        API_KEY
                    )
                    .build()
                request = request.newBuilder().url(url).build()
                chain.proceed(request)
            }

            val clientDetailsRestaurant = OkHttpClient.Builder()
                .sslSocketFactory(TLSSocketFactory(), trustManager)
                .addInterceptor(paramsInterceptorDetailsRestaurant)
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofitDetailsRestaurant = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .client(clientDetailsRestaurant)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            serviceDetailsRestaurant = retrofitDetailsRestaurant.create(
                GooglePlacesApi::class.java)
        }
    }

    override fun getNearbyRestaurants(location: String): PlacesSearchApiResponse? {
        return serviceNearbyRestaurant.getNearbyRestaurants(location).execute().body()
    }

    override fun getDetailsRestaurant(restaurantId: String): PlacesDetailsApiResponse? {
        return serviceDetailsRestaurant.getDetailsForRestaurant(restaurantId).execute().body()
    }

}