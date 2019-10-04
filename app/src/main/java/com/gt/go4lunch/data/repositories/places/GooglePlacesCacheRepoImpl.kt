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

            resultsDao.deleteAllResults()

            val resultsApi = placesRepo.getNearbyRestaurants(location)

            val ttl = dateTimeNow.plusMinutes(30)

            saveResult(resultsApi, ttl.toString(), latUser, lngUser)

            resultsApi
        } else {
            mapCacheResults(cacheResults)
        }

    }


    private suspend fun saveResult(resultApi: PlacesSearchApiResponse?, ttl: String, actualLatUser: Double, actualLngUser: Double){
        resultApi?.results?.map {
            resultsDao.insertResult(ResultTable(
                geometry = GeometryTable(
                    location = LocationTable(
                        lat = it.geometry.location.lat,
                        lng = it.geometry.location.lng
                    )
                ), opening_hours = OpeningHoursTable(
                    openNow = it.openingHours?.openNow
                ), name = it.name,
                types = it.types?.joinToString(),
                iconURL = it.icon,
                vicinity = it.vicinity,
                ttl = ttl,
                latUser = actualLatUser,
                lngUser = actualLngUser
            ))
        }
    }

    private fun mapCacheResults(cacheResults: List<ResultTable>): PlacesSearchApiResponse{

        return PlacesSearchApiResponse().apply {

            results = mutableListOf()

            for (resultsDao in cacheResults){

                resultsDao.let {

                    (results as MutableList<Result>).add(Result().apply {

                        this.name = it.name
                        this.vicinity = it.vicinity
                        this.types = it.types?.split(", ")
                        this.icon = it.iconURL
                        this.geometry = Geometry().apply {
                            this.location = Location().apply {
                                this.lat = it.geometry.location.lat
                                this.lng = it.geometry.location.lng
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

}