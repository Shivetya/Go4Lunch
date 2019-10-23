package com.gt.go4lunch.viewmodeltests

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gt.go4lunch.data.*
import com.gt.go4lunch.testutils.CoroutinesTestRules
import com.gt.go4lunch.usecases.GoogleListRestaurant
import com.gt.go4lunch.viewmodels.GoogleMapViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import net.danlew.android.joda.JodaTimeAndroid
import org.junit.Assert
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
class GoogleMapViewModelTest {


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @get:Rule
    var coroutinesTestRule = CoroutinesTestRules()

    private lateinit var listRestaurantUseCase: GoogleListRestaurant

    private val loc = android.location.Location("TestLoc").apply {
        latitude = 48.0175400
        longitude = 6.5882000
    }

    val googlePlacesResponse: PlacesSearchApiResponse = PlacesSearchApiResponse(
        results = listOf(
            Result(
                name = "Nom du restaurant ici !!!",
                geometry = Geometry(
                    location = Location(
                        lat = 16584.54654,
                        lng = 3654.5754
                    )
                )
            ), Result(
                name = "Un deuxième nom de restaurant ici",
                geometry = Geometry(
                    location = Location(
                        lat = 496846521.4,
                        lng = 68546151.6478
                    )
                )
            )
        )
    )

    @Before
    fun setUseCase() {
        listRestaurantUseCase = object :
            GoogleListRestaurant {
            override suspend fun getListRestaurant(location: String): PlacesSearchApiResponse? =
                googlePlacesResponse

            override suspend fun getDetailRestaurant(restaurantID: String): PlacesDetailsApiResponse? {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
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
    fun `should expose list of models (restaurantMarker) - get name`() = runBlockingTest {
        //given
        googlePlacesResponse.results?.get(0)?.apply {
            name = "Bonjour"
        }
        val googleMapViewModel = GoogleMapViewModel(listRestaurantUseCase)

        googleMapViewModel.fetchListRestaurantMarker(loc)

        //then
        Assert.assertEquals("Bonjour", googleMapViewModel.listRestaurantMarker.value?.get(0)?.name)
    }

    @Test
    fun `should expose list of models (restaurantMarker) - get latitude`() = runBlockingTest {
        //given
        googlePlacesResponse.results?.get(0)?.apply {
            geometry = Geometry(
                location = Location(
                    lat = 48.0175400,
                    lng = 6.5882000
                )
            )
        }
        val googleMapViewModel = GoogleMapViewModel(listRestaurantUseCase)

        googleMapViewModel.fetchListRestaurantMarker(loc)

        //then
        Assert.assertEquals(48.0175400, googleMapViewModel.listRestaurantMarker.value?.get(0)?.lat)
    }

    @Test
    fun `should expose list of models (restaurantMarker) - get longitude`() = runBlockingTest {
        //given
        googlePlacesResponse.results?.get(0)?.apply {
            geometry = Geometry(
                location = Location(
                    lat = 48.0175400,
                    lng = 6.5882000
                )
            )
        }

        val googleMapViewModel = GoogleMapViewModel(listRestaurantUseCase)

        googleMapViewModel.fetchListRestaurantMarker(loc)

        //then
        Assert.assertEquals(6.5882000, googleMapViewModel.listRestaurantMarker.value?.get(0)?.lng)
    }

    @Test
    fun `should expose list of models (restaurantMarker) - get second name`() = runBlockingTest {
        //given
        googlePlacesResponse.results?.get(1)?.apply {
            name = "restaurant"
        }
        val googleMapViewModel = GoogleMapViewModel(listRestaurantUseCase)

        googleMapViewModel.fetchListRestaurantMarker(loc)

        //then
        Assert.assertEquals(
            "restaurant",
            googleMapViewModel.listRestaurantMarker.value?.get(1)?.name
        )
    }
}