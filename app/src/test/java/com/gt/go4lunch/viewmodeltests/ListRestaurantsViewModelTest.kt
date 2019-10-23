package com.gt.go4lunch.viewmodeltests

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gt.go4lunch.data.*
import com.gt.go4lunch.testutils.CoroutinesTestRules
import com.gt.go4lunch.usecases.GoogleListRestaurant
import com.gt.go4lunch.viewmodels.ListRestaurantsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import net.danlew.android.joda.JodaTimeAndroid
import org.junit.Assert.assertEquals
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
class ListRestaurantsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @get:Rule
    var coroutinesTestRule = CoroutinesTestRules()

    private lateinit var listRestaurantUseCase: GoogleListRestaurant

    private val loc = android.location.Location("TestLoc").apply {
        latitude = 48.0175400
        longitude = 6.5882000
    }

    val googleNearbyPlacesResponse: PlacesSearchApiResponse = PlacesSearchApiResponse(
        results = listOf(
            Result(
                name = "Nom du restaurant ici !!!",
                vicinity = "Adresse du restaurant ici !!!",
                icon = "url de l'image ici !!!",
                geometry = Geometry(
                    location = Location(
                        lat = 16584.54654,
                        lng = 3654.5754
                    )
                ),
                types = listOf(
                    "Veggie", "Vegan"
                )
            ), Result(
                name = "Un deuxième nom de restaurant ici",
                vicinity = "Une deuxième adresse !",
                openingHours = OpeningHours(
                    openNow = false
                ),
                icon = "Une deuxième url d'image",
                geometry = Geometry(
                    location = Location(
                        lat = 496846521.4,
                        lng = 68546151.6478
                    )
                ),
                types = listOf(
                    "non Veggie", "non Vegan"
                )
            )
        )
    )

    val googleDetailsApiResponse = PlacesDetailsApiResponse(
        result = Result(
            name = "Nom du restaurant ici !!!",
            vicinity = "Adresse du restaurant ici !!!",
            openingHours = OpeningHours(
                openNow = true
            ),
            icon = "url de l'image ici !!!",
            geometry = Geometry(
                location = Location(
                    lat = 16584.54654,
                    lng = 3654.5754
                )
            ),
            types = listOf(
                "Veggie", "Vegan"
            ),
            rating = 5.0
        ),
        htmlAttributions = null,
        status = null
    )

    @Before
    fun setUseCase() {
        listRestaurantUseCase = object :
            GoogleListRestaurant {
            override suspend fun getListRestaurant(location: String): PlacesSearchApiResponse? =
                googleNearbyPlacesResponse

            override suspend fun getDetailRestaurant(restaurantID: String): PlacesDetailsApiResponse? {
                return googleDetailsApiResponse
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
    fun `should expose list of models (restaurant) - get name`() = runBlockingTest {
        //given
        googleNearbyPlacesResponse.results?.get(0)?.apply {
            name = "Bonjour"
        }
        val listRestaurantsViewModel = ListRestaurantsViewModel(listRestaurantUseCase)

        listRestaurantsViewModel.fetchListRestaurants(loc)

        //then
        assertEquals("Bonjour", listRestaurantsViewModel.listRestaurants.value?.get(0)?.name)
    }

    @Test
    fun `should expose list of models (restaurant) - get address`() = runBlockingTest {
        //given
        googleNearbyPlacesResponse.results?.get(0)?.apply {
            vicinity = "adresse"
        }
        val listRestaurantsViewModel = ListRestaurantsViewModel(listRestaurantUseCase)

        listRestaurantsViewModel.fetchListRestaurants(loc)

        //then
        assertEquals("adresse", listRestaurantsViewModel.listRestaurants.value?.get(0)?.address)
    }

    @Test
    fun `should expose list of models (restaurant) - get url picture`() = runBlockingTest {
        //given
        googleNearbyPlacesResponse.results?.get(0)?.apply {
            photos = listOf(Photo(
                htmlAttributions = null,
                height = null,
                photoReference = "url de l'image"
            ))
        }
        val listRestaurantsViewModel = ListRestaurantsViewModel(listRestaurantUseCase)

        listRestaurantsViewModel.fetchListRestaurants(loc)

        //then
        assertEquals(
            "url de l'image",
            listRestaurantsViewModel.listRestaurants.value?.get(0)?.urlPicture
        )
    }

    @Test
    fun `should expose list of models (restaurant) - get types`() = runBlockingTest {
        //given
        googleNearbyPlacesResponse.results?.get(0)?.apply {
            types = listOf("veggie", "Très bon")
        }
        val listRestaurantsViewModel = ListRestaurantsViewModel(listRestaurantUseCase)

        listRestaurantsViewModel.fetchListRestaurants(loc)

        //then
        assertEquals(
            "veggie, Très bon",
            listRestaurantsViewModel.listRestaurants.value?.get(0)?.types
        )
    }

    @Test
    fun `should expose list of models (restaurant) - get second restaurant name`() =
        runBlockingTest {
            //given
            googleNearbyPlacesResponse.results?.get(1)?.apply {
                name = "second restaurant name here"
            }
            val listRestaurantsViewModel = ListRestaurantsViewModel(listRestaurantUseCase)

            listRestaurantsViewModel.fetchListRestaurants(loc)

            //then
            assertEquals(
                "second restaurant name here",
                listRestaurantsViewModel.listRestaurants.value?.get(1)?.name
            )
        }

    @Test
    fun `should transform Location to string for query`() {

        //given
        loc.latitude = 6654.564
        loc.longitude = 5643.68546

        val listRestaurantsViewModel = ListRestaurantsViewModel(listRestaurantUseCase)

        val stringToTest = listRestaurantsViewModel.transformLocationQueryReady(loc)

        //then
        assertEquals("6654.564,5643.68546", stringToTest)
    }

    @Test
    fun `should expose list of models (restaurant) - get distance`() = runBlockingTest {
        //given
        googleNearbyPlacesResponse.results?.get(0)?.apply {
            geometry = Geometry(
                location = Location(
                    lat = 48.1833300,
                    lng = 6.4500000
                )
            )
        }
        val listRestaurantsViewModel = ListRestaurantsViewModel(listRestaurantUseCase)

        listRestaurantsViewModel.fetchListRestaurants(loc)

        //then
        assertEquals("21113", listRestaurantsViewModel.listRestaurants.value?.get(0)?.distance)
    }

}