package com.gt.go4lunch.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message (

    val message: String,
    @ServerTimestamp val dateMessage: Date,
    val userSender: User,
    val urlImage: String?

    )