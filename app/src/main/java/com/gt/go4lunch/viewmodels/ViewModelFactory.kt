package com.gt.go4lunch.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gt.go4lunch.data.repositories.location.LocationRepo
import com.gt.go4lunch.data.repositories.location.LocationRepoImpl
import com.gt.go4lunch.usecases.GoogleListRestaurantsUseCase
import com.gt.go4lunch.usecases.UsersFirestoreUseCase

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(private val usersFirestoreUseCase: UsersFirestoreUseCase,
                                           private val locationRepo: LocationRepo,
                                           private val googleListRestaurantsUseCase: GoogleListRestaurantsUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return when {
            modelClass.isAssignableFrom(LoggedViewModel::class.java) -> LoggedViewModel(usersFirestoreUseCase, locationRepo) as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(usersFirestoreUseCase) as T
            modelClass.isAssignableFrom(ListRestaurantsViewModel::class.java) -> ListRestaurantsViewModel(googleListRestaurantsUseCase) as T
            modelClass.isAssignableFrom(GoogleMapViewModel::class.java) -> GoogleMapViewModel(googleListRestaurantsUseCase) as T
            else -> throw IllegalArgumentException("Wrong UseCase Parameter")
        }
    }

    companion object {

        val INSTANCE = ViewModelFactory(UsersFirestoreUseCase(),
            LocationRepoImpl.instance,
            GoogleListRestaurantsUseCase.instance
        )
    }

}