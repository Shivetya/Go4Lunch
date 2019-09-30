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

        val ttl = dateTimeNow.plusMinutes(30)

        val cacheResults = resultsDao.getResultsByCache(latUser, lngUser, ttl.toString())

        return if (cacheResults.isEmpty()){

            resultsDao.deleteAllResults()

            val resultsApi = placesRepo.getNearbyRestaurants(location)

            saveResult(resultsApi, ttl)

            resultsApi
        } else {
            mapCacheResults(cacheResults)
        }

    }


    private suspend fun saveResult(resultApi: PlacesSearchApiResponse?, ttl: DateTime){
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
                ttl = ttl.toString()
            ))
        }
    }

    private fun mapCacheResults(cacheResults: List<ResultTable>): PlacesSearchApiResponse{

        return PlacesSearchApiResponse().apply {
            for (result in cacheResults){
                result.let {
                    Result().apply {
                        name = it.name
                        vicinity = it.vicinity
                        types = it.types?.split(", ")
                        icon = it.iconURL
                        geometry = Geometry().apply {
                            location = Location().apply {
                                lat = it.geometry.location.lat
                                lng = it.geometry.location.lng
                            }
                        }
                        openingHours = OpeningHours().apply {
                            openNow = it.opening_hours?.openNow
                        }
                    }
                }
            }
        }
    }

}