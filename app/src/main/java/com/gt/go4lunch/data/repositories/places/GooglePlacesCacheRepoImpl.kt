package com.gt.go4lunch.data.repositories.places

import android.media.Image
import com.gt.go4lunch.data.*
import com.gt.go4lunch.data.repositories.room.*
import org.joda.time.DateTime


class GooglePlacesCacheRepoImpl(private val placesRepo: GooglePlacesRepo) :
    GooglePlacesCacheRepo {

    companion object {
        val instance: GooglePlacesCacheRepo by lazy {
            GooglePlacesCacheRepoImpl(GooglePlacesRepoImpl())
        }
    }

    private val resultsDao = AppDatabase.getInstance().resultDao()
    private val periodsDao = AppDatabase.getInstance().periodDao()
    private val resultDetailsDao = AppDatabase.getInstance().resultDetailsDao()


    override suspend fun getListRestaurants(location: String): PlacesSearchApiResponse? {

        val latUser = location.substringBefore(",").toDouble()
        val lngUser = location.substringAfter(",").toDouble()

        val dateTimeNow = DateTime()

        val cacheResults = resultsDao.getResultsByCache(latUser, lngUser, dateTimeNow.toString())

        return if (cacheResults.isEmpty()) {

            resultsDao.deleteAllResults(dateTimeNow.toString())

            val resultsApi = placesRepo.getNearbyRestaurants(location)

            val ttl = dateTimeNow.plusMinutes(30)

            saveResultListRestaurant(resultsApi, ttl.toString(), latUser, lngUser)

            resultsApi
        } else {
            mapCacheResultsListRestaurant(cacheResults)
        }

    }


    private suspend fun saveResultListRestaurant(
        resultApi: PlacesSearchApiResponse?,
        ttl: String,
        actualLatUser: Double,
        actualLngUser: Double
    ) {
        resultApi?.results?.map {
            resultsDao.insertResult(
                ResultTable(
                    geometry = GeometryTable(
                        location = LocationTable(
                            lat = it.geometry.location.lat,
                            lng = it.geometry.location.lng
                        )
                    ), opening_hours = OpeningHoursTable(
                        openNow = it.openingHours?.openNow
                    ), name = it.name,
                    types = it.types?.joinToString(),
                    vicinity = it.vicinity,
                    ttl = ttl,
                    latUser = actualLatUser,
                    lngUser = actualLngUser,
                    restaurantId = it.placeId
                )
            )
        }
    }

    private fun mapCacheResultsListRestaurant(cacheResults: List<ResultTable>): PlacesSearchApiResponse {

        val results = mutableListOf<Result>()

        for (resultsDao in cacheResults) {

            resultsDao.let {

                results.add(
                    Result(
                        name = it.name,
                        vicinity = it.vicinity,
                        types = it.types?.split(", "),
                        geometry = Geometry(
                            Location(
                                it.geometry.location.lat,
                                it.geometry.location.lng
                            )
                        ),
                        openingHours = OpeningHours(
                            openNow = it.opening_hours?.openNow,
                            periods = null,
                            weekdayText = null
                        )
                    )
                )
            }
        }

        return PlacesSearchApiResponse(
            htmlAttributions = null,
            status = null,
            results = results
        )
    }

    override suspend fun getDetailsRestaurant(restaurantId: String): PlacesDetailsApiResponse? {

        val dateTimeNow = DateTime()

        val cacheRating = resultDetailsDao.getRatingByCache(restaurantId, dateTimeNow.toString())
        val cachePeriods = periodsDao.getPeriodsByCache(restaurantId, dateTimeNow.toString())

        return if (cacheRating == null || cachePeriods.isEmpty()) {

            resultsDao.deleteAllResults(dateTimeNow.toString())
            periodsDao.deleteAllPeriods(dateTimeNow.toString())

            val resultsApi = placesRepo.getDetailsRestaurant(restaurantId)

            val ttl = dateTimeNow.plusMinutes(30)

            saveDetailsRestaurant(resultsApi, ttl.toString(), restaurantId)

            resultsApi
        } else {
            mapCacheDetailsRestaurant(cacheRating, cachePeriods)
        }
    }

    private suspend fun saveDetailsRestaurant(
        resultApi: PlacesDetailsApiResponse?,
        ttl: String,
        restaurantID: String
    ) {
        resultApi?.result.let { result ->
            resultDetailsDao.insertResultDetails(
                ResultDetailsTable(
                    ttl = ttl,
                    restaurantId = restaurantID,
                    rating = result?.rating
                )
            )
            val periods = result?.openingHours?.periods
            if (periods != null) {
                for (period in periods) {
                    periodsDao.insertPeriod(
                        PeriodsTable(
                            open = OpenTable(
                                dayopen = period.open?.day,
                                timeopen = period.open?.time
                            ),
                            close = CloseTable(
                                dayclose = period.close?.day,
                                timeclose = period.close?.time
                            ),
                            ttl = ttl,
                            restaurantId = restaurantID
                        )
                    )
                }
            }

        }
    }

    private fun mapCacheDetailsRestaurant(
        cacheResultsDetails: Double,
        cacheListPeriods: List<PeriodsTable>
    ): PlacesDetailsApiResponse {

        return PlacesDetailsApiResponse(
            result = Result(
                openingHours = OpeningHours(
                    periods = cacheListPeriods.map {
                        Period(
                            open = Open(
                                day = it.open?.dayopen,
                                time = it.open?.timeopen
                            ),
                            close = Close(
                                day = it.close?.dayclose,
                                time = it.close?.timeclose
                            )
                        )
                    },
                    openNow = null,
                    weekdayText = null
                ),
                rating = cacheResultsDetails,
                name = "",
                geometry = Geometry(Location(0.0,0.0))
            ),
            htmlAttributions = null,
            status = null
        )
    }

    override suspend fun getPhotoRestaurant(photoId: String): Image? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}