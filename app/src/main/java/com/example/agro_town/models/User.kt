package com.example.agro_town.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    var id: String = "",
    var firstName: String = "",
    val lastName: String = "",
    var email: String = "",
    var image: String = "",
    val mobile: Long = 0,
    val gender: String = "",
    val profileCompleted: Int = 0

): Parcelable {
    constructor(firstName: String, email: String, id: String) : this() {
        this.firstName = firstName
        this.email = email
        this.id = id
    }
}

