package com.gt.go4lunch.data.repositories.places

import com.gt.go4lunch.data.*
import com.gt.go4lunch.data.repositories.room.*
import org.joda.time.DateTime


class GooglePlacesCacheRepoImpl(private val placesRepo: GooglePlacesRepo):
    GooglePlacesCacheRepo {

    companion object{
        val instance: GooglePlacesCacheRepo by lazy {
            GooglePlacesCacheRepoImpl(GooglePlacesRepoImpl())
        }
    }

    private val resultsDao = AppDatabase.getInstance().resultDao()


    override suspend fun getListRestaurants(location: String): PlacesSearchApiResponse?{

        val latUser = location.substringBefore(",").toDouble()
        val lngUser = location.substringAfter(",").toDouble()

        val dateTimeNow = DateTime()

        val cacheResults = resultsDao.getResultsByCache(latUser, lngUser, dateTimeNow.toString())

        return if (cacheResults.isEmpty()){

            resultsDao.deleteAllResults(dateTimeNow.toString())

            val resultsApi = placesRepo.getNearbyRestaurants(location)

            val ttl = dateTimeNow.plusMinutes(30)

            saveResultListRestaurant(resultsApi, ttl.toString(), latUser, lngUser)

            resultsApi
        } else {
            mapCacheResultsListRestaurant(cacheResults)
        }

    }


    private suspend fun saveResultListRestaurant(resultApi: PlacesSearchApiResponse?, ttl: String, actualLatUser: Double, actualLngUser: Double){
        resultApi?.results?.map {
            resultsDao.insertResult(ResultTable(
                geometry = GeometryTable(
                    location = LocationTable(
                        lat = it.geometry.location.lat,
                        lng = it.geometry.location.lng
                    )
                ), opening_hours = OpeningHoursTable(
                    openNow = it.openingHours?.openNow,
                    periods = null
                ), name = it.name,
                types = it.types?.joinToString(),
                vicinity = it.vicinity,
                ttl = ttl,
                latUser = actualLatUser,
                lngUser = actualLngUser,
                restaurantId = it.placeId
            ))
        }
    }

    private fun mapCacheResultsListRestaurant(cacheResults: List<ResultTable>): PlacesSearchApiResponse{

        return PlacesSearchApiResponse().apply {

            results = mutableListOf()

            for (resultsDao in cacheResults){

                resultsDao.let {

                    (results as MutableList<Result>).add(Result().apply {

                        this.name = it.name
                        this.vicinity = it.vicinity
                        this.types = it.types?.split(", ")
                        this.geometry = Geometry().apply {
                            this.location = Location().apply {
                                this.lat = it.geometry?.location?.lat
                                this.lng = it.geometry?.location?.lng
                            }
                        }
                        this.openingHours = OpeningHours().apply {
                            this.openNow = it.opening_hours?.openNow
                        }
                    })
                }
            }
        }
    }

    override suspend fun getDetailsRestaurant(restaurantId: String): PlacesDetailsApiResponse? {

        val dateTimeNow = DateTime()

        val cacheResults = resultsDao.getResultDetailsByCache(restaurantId, dateTimeNow.toString())

        return if (cacheResults == null){

            resultsDao.deleteAllResults(dateTimeNow.toString())

            val resultsApi = placesRepo.getDetailsRestaurant(restaurantId)

            val ttl = dateTimeNow.plusMinutes(30)

            saveDetailsRestaurant(resultsApi, ttl.toString(), restaurantId)

            resultsApi
        } else {
            mapCacheDetailsRestaurant(cacheResults)
        }
    }

    private suspend fun saveDetailsRestaurant(resultApi: PlacesDetailsApiResponse?, ttl: String, restaurantID: String){
        resultApi?.result.let { result ->
            resultsDao.insertResult(ResultTable(
                opening_hours = OpeningHoursTable(
                    periods = result?.openingHours?.periods?.map {
                        PeriodsTable(
                            close = CloseTable(
                                day = it.close?.day,
                                time = it.close?.time
                            ),
                            open = OpenTable(
                                day = it.open?.day,
                                time = it.open?.time
                            )
                        )
                    }
                ),
                ttl = ttl,
                restaurantId = restaurantID,
                rating = result?.rating
            ))
        }
    }

    private fun mapCacheDetailsRestaurant(cacheResults: ResultTable): PlacesDetailsApiResponse{
        return PlacesDetailsApiResponse().apply {
            result = Result().apply {
                openingHours = OpeningHours().apply {
                    periods = cacheResults.opening_hours?.periods?.map {
                        Period().apply {
                            open = Open().apply {
                                day = it?.open?.day
                                time = it?.open?.time
                            }
                            close = Close().apply {
                                day = it?.close?.day
                                time = it?.close?.time
                            }
                        }
                    }
                }
                rating = cacheResults.rating
            }
        }
    }

}