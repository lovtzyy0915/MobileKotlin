package com.example.assignment1.model


data class LatLng (
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)

data class User(
    var username: String = "",
    var password: String = "",
    var name: String = "",
    var profileImg: String = "",
    var age: String = "",
    var contact: String = "",
    var location: LatLng = LatLng(0.0,0.0)
)

data class NearbyUser(
    val user: User,
    val distance: Double
)
