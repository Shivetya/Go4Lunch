package com.gt.go4lunch.usecasestests

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gt.go4lunch.data.*
import com.gt.go4lunch.data.repositories.places.GooglePlacesCacheRepo
import com.gt.go4lunch.testutils.CoroutinesTestRules
import com.gt.go4lunch.usecases.ListRestaurantsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import net.danlew.android.joda.JodaTimeAndroid
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import java.io.InputStream

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class ListRestaurantsUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @get:Rule
    var coroutinesTestRule = CoroutinesTestRules()

    private lateinit var googlePlacesCacheRepo: GooglePlacesCacheRepo
    private val loc = android.location.Location("TestLoc")

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
        googlePlacesCacheRepo = object :
            GooglePlacesCacheRepo {
            override fun getListRestaurants(location: String): PlacesSearchApiResponse = googlePlacesResponse
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
        val listRestaurantsUseCase = ListRestaurantsUseCase(googlePlacesCacheRepo)

        listRestaurantsUseCase.fetchListRestaurants("")

        //then
        assertEquals("Bonjour", listRestaurantsUseCase.listRestaurants.value?.get(0)?.name)
    }

    @Test
    fun `should expose list of models (restaurant) - get address`() = runBlockingTest {
        //given
        googlePlacesResponse.results?.get(0)?.apply {
            vicinity = "adresse"
        }
        val listRestaurantsUseCase = ListRestaurantsUseCase(googlePlacesCacheRepo)

        listRestaurantsUseCase.fetchListRestaurants("")

        //then
        assertEquals("adresse", listRestaurantsUseCase.listRestaurants.value?.get(0)?.address)
    }

    @Test
    fun `should expose list of models (restaurant) - get url picture`() = runBlockingTest {
        //given
        googlePlacesResponse.results?.get(0)?.apply {
            icon = "url de l'image"
        }
        val listRestaurantsUseCase = ListRestaurantsUseCase(googlePlacesCacheRepo)

        listRestaurantsUseCase.fetchListRestaurants("")

        //then
        assertEquals("url de l'image", listRestaurantsUseCase.listRestaurants.value?.get(0)?.urlPicture)
    }

    @Test
    fun `should expose list of models (restaurant) - get is open`() = runBlockingTest {
        //given
        googlePlacesResponse.results?.get(0)?.apply {
            openingHours = OpeningHours().apply {
                openNow = true
            }
        }
        val listRestaurantsUseCase = ListRestaurantsUseCase(googlePlacesCacheRepo)

        listRestaurantsUseCase.fetchListRestaurants("")

        //then
        listRestaurantsUseCase.listRestaurants.value?.get(0)?.isOpen?.let { assertTrue(it) }
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
        val listRestaurantsUseCase = ListRestaurantsUseCase(googlePlacesCacheRepo)

        listRestaurantsUseCase.fetchListRestaurants("")

        //then
        assertEquals(listOf(((-65413251.45864).toFloat()), 546541.165846F), listRestaurantsUseCase.listRestaurants.value?.get(0)?.latLng)
    }

    @Test
    fun `should expose list of models (restaurant) - get types`() = runBlockingTest {
        //given
        googlePlacesResponse.results?.get(0)?.apply {
            types = listOf("veggie")
        }
        val listRestaurantsUseCase = ListRestaurantsUseCase(googlePlacesCacheRepo)

        listRestaurantsUseCase.fetchListRestaurants("")

        //then
        assertEquals("veggie",
            listRestaurantsUseCase.listRestaurants.value?.get(0)?.types?.get(0)
        )
    }

    @Test
    fun `should expose list of models (restaurant) - get second restaurant name`() = runBlockingTest {
        //given
        googlePlacesResponse.results?.get(1)?.apply {
            name = "second restaurant name here"
        }
        val listRestaurantsUseCase = ListRestaurantsUseCase(googlePlacesCacheRepo)

        listRestaurantsUseCase.fetchListRestaurants("")

        //then
        assertEquals("second restaurant name here", listRestaurantsUseCase.listRestaurants.value?.get(1)?.name)
    }

    @Test
    fun `should transform Location to string for query`(){

        //given
        loc.latitude = 6654.564
        loc.longitude = 5643.68546

        val listRestaurantsUseCase = ListRestaurantsUseCase(googlePlacesCacheRepo)

        val stringToTest = listRestaurantsUseCase.transformLocationQueryReady(loc)

        //then
        assertEquals("location=6654.564,5643.68546", stringToTest)
    }

}