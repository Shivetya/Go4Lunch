package com.gt.go4lunch.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gt.go4lunch.data.repositories.location.LocationRepo
import com.gt.go4lunch.data.repositories.location.LocationRepoImpl
import com.gt.go4lunch.usecases.UsersFirestoreUseCase
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(private val usersFirestoreUseCase: UsersFirestoreUseCase,
                                           private val locationRepo: LocationRepo
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return when {
            modelClass.isAssignableFrom(LoggedViewModel::class.java) -> LoggedViewModel(usersFirestoreUseCase, locationRepo) as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(usersFirestoreUseCase) as T
            else -> throw IllegalArgumentException("Wrong UseCase Parameter")
        }
    }

    companion object {

        val INSTANCE = ViewModelFactory(UsersFirestoreUseCase(), LocationRepoImpl.instance)
    }

}