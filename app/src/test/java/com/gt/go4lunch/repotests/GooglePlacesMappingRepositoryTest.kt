package com.gt.go4lunch.repotests

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gt.go4lunch.data.*
import com.gt.go4lunch.data.repositories.GooglePlacesRepo
import com.gt.go4lunch.testutils.CoroutinesTestRules
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import net.danlew.android.joda.JodaTimeAndroid
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.io.InputStream

@ExperimentalCoroutinesApi
class GooglePlacesMappingRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRules()

    private lateinit var googlePlacesRepo: GooglePlacesRepo

    val googlePlacesResponse: PlacesSearchApiResponse = PlacesSearchApiResponse().apply {
        results = listOf(Result().apply {
            name = "Nom du restaurant ici !!!"
            vicinity = "Adresse du restaurant ici !!!"
            openingHours = OpeningHours().apply {
                openNow = true
            }
            icon = "url de l'image ici !!!"
            geometry = Geometry().apply {
                location = Location().apply {
                    lat = 16584.54654F
                    lng = 3654.5754F
                }
            }
            types = listOf("Veggie", "Vegan")
        }, Result().apply {
            name = "Un deuxième nom de restaurant ici"
            vicinity = "Une deuxième adresse !"
            openingHours = OpeningHours().apply {
                openNow = false
            }
            icon = "Une deuxième url d'image"
            geometry = Geometry().apply {
                location = Location().apply {
                    lat = 496846521.4F
                    lng = 68546151.6478F
                }
            }
            types = listOf("non Veggie", "non Vegan")
        })
    }

    @Before
    fun setRepo(){
        googlePlacesRepo = object :GooglePlacesRepo{
            override fun getNearbyRestaurants(): PlacesSearchApiResponse = googlePlacesResponse
        }
    }

    //This fun is for mock context to allow init of jodatime in tests.
    @Before
    fun prepareTimeBeforeTests() {
        val context = Mockito.mock(Context::class.java)
        val appContext = Mockito.mock(Context::class.java)
        val resources = Mockito.mock(Resources::class.java)
        Mockito.`when`(resources.openRawResource(ArgumentMatchers.anyInt()))
            .thenReturn(Mockito.mock(InputStream::class.java))
        Mockito.`when`(appContext.resources).thenReturn(resources)
        Mockito.`when`(context.applicationContext).thenReturn(appContext)
        JodaTimeAndroid.init(context)
    }


    @Test
    fun `should expose list of models (restaurant) - get name`() = runBlockingTest {
        //given
        googlePlacesResponse.results?.get(0)?.apply {
            name = "Bonjour"
        }
        val googlePlacesMappingRepo = GooglePlacesMappingRepository(googlePlacesRepo)

        googlePlacesMappingRepo.fetchNearbyRestaurants()

        //then
        Assert.assertEquals("Bonjour", googlePlacesMappingRepo.restaurants.value?.get(0)?.name)
    }

    @Test
    fun `should expose list of models (restaurant) - get address`() = runBlockingTest {
        //given
        googlePlacesResponse.results?.get(0)?.apply {
            vicinity = "adresse"
        }
        val googlePlacesMappingRepo = GooglePlacesMappingRepository(googlePlacesRepo)

        googlePlacesMappingRepo.fetchNearbyRestaurants()

        //then
        Assert.assertEquals("adresse", googlePlacesMappingRepo.restaurants.value?.get(0)?.address)
    }

    @Test
    fun `should expose list of models (restaurant) - get url picture`() = runBlockingTest {
        //given
        googlePlacesResponse.results?.get(0)?.apply {
            icon = "url de l'image"
        }
        val googlePlacesMappingRepo = GooglePlacesMappingRepository(googlePlacesRepo)

        googlePlacesMappingRepo.fetchNearbyRestaurants()

        //then
        Assert.assertEquals("url de l'image", googlePlacesMappingRepo.restaurants.value?.get(0)?.urlPicture)
    }

    @Test
    fun `should expose list of models (restaurant) - get is open`() = runBlockingTest {
        //given
        googlePlacesResponse.results?.get(0)?.apply {
            openingHours = OpeningHours().apply {
                openNow = true
            }
        }
        val googlePlacesMappingRepo = GooglePlacesMappingRepository(googlePlacesRepo)

        googlePlacesMappingRepo.fetchNearbyRestaurants()

        //then
        assertTrue(googlePlacesMappingRepo.restaurants.value?.get(0)?.isOpen)
    }

    @Test
    fun `should expose list of models (restaurant) - get latitude and longitude`() = runBlockingTest {
        //given
        googlePlacesResponse.results?.get(0)?.apply {
            geometry = Geometry().apply {
                location = Location().apply {
                    lat = (-65413251.45864).toFloat()
                    lng = 546541.165846F
                }
            }
        }
        val googlePlacesMappingRepo = GooglePlacesMappingRepository(googlePlacesRepo)

        googlePlacesMappingRepo.fetchNearbyRestaurants()

        //then
        Assert.assertEquals(listOf(((-65413251.45864).toFloat()), 546541.165846F), googlePlacesMappingRepo.restaurants.value?.get(0)?.latLng)
    }

    @Test
    fun `should expose list of models (restaurant) - get types`() = runBlockingTest {
        //given
        googlePlacesResponse.results?.get(0)?.apply {
            types = listOf("veggie")
        }
        val googlePlacesMappingRepo = GooglePlacesMappingRepository(googlePlacesRepo)

        googlePlacesMappingRepo.fetchNearbyRestaurants()

        //then
        Assert.assertEquals("veggie", googlePlacesMappingRepo.restaurants.value?.get(0)?.types[0])
    }

    @Test
    fun `should expose list of models (restaurant) - get second restaurant name`() = runBlockingTest {
        //given
        googlePlacesResponse.results?.get(1)?.apply {
            name = "second restaurant name here"
        }
        val googlePlacesMappingRepo = GooglePlacesMappingRepository(googlePlacesRepo)

        googlePlacesMappingRepo.fetchNearbyRestaurants()

        //then
        Assert.assertEquals("second restaurant name here", googlePlacesMappingRepo.restaurants.value?.get(1)?.name)
    }

}