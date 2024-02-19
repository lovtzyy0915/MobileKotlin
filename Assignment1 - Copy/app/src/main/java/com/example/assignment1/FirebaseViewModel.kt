package com.example.assignment1

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.assignment1.data.DataSource.current_user
import com.example.assignment1.model.LatLng
import com.example.assignment1.model.NearbyUser
import com.example.assignment1.model.User
import com.example.assignment1.model.Video
import com.example.assignment1.model.Weight

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.*

class  FirebaseViewModel(): ViewModel() {

    fun checkUserPassword(
        username: String,
        password: String,
        context: Context,
        onComplete: (Result<User>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {

        val firestoreRef = Firebase.firestore
            .collection("users")
            .document(username)
        try {
            firestoreRef.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null && document.exists()) {
                            // Document exists, check the password
                            val user = document.toObject(User::class.java)
                            if (user != null && checkPassword(password, user.password)) {
                                // Passwords match, return the user
                                retrieveUserData(username, context) { userDataResult ->
                                    onComplete(userDataResult)
                                }

                            } else {
                                // Incorrect password
                                onComplete(Result.failure(Exception("Incorrect password")))
                            }
                        } else {
                            // Username not found
                            onComplete(Result.failure(Exception("Username not found")))
                        }
                    } else {
                        // Handle other exceptions
                        onComplete(Result.failure(task.exception ?: Exception("Unknown error")))
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun checkPassword(inputPassword: String, storedPassword: String): Boolean {
        return inputPassword == storedPassword
    }

    fun retrieveUserData(
        username: String,
        context: Context,
        data: (Result<User>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {

        val firestoreRef = Firebase.firestore
            .collection("users")
            .document(username)

        try {
            firestoreRef.get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val user = it.toObject<User>()!!
                        data(Result.success(user))
                    } else {
                        Toast.makeText(context, "No Student Data Found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun saveUser(
        user: User,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch {
        val firestoreRef = Firebase.firestore
            .collection("users")
            .document(user.username)

        try {
            firestoreRef.set(user)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully Sign Up!", Toast.LENGTH_SHORT).show()
                }

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun checkExistUsername(
        username: String,
        context: Context,
        result: (Boolean) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {

        val firestoreRef = Firebase.firestore
            .collection("users")
            .document(username)

        try {
            firestoreRef.get()
                .addOnSuccessListener {
                    if (!it.exists()) {
                        result(true)
                    } else {
                        result(false)
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun updateUser(
        user: User,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch {
        val firestoreRef = Firebase.firestore
            .collection("users")
            .document(current_user.value.username)
        Log.w("update User", "3")

        try {
            Log.w("update User", "4")
            firestoreRef.update(
                "name", user.name,
                "contact", user.contact,
                "location", user.location,
                "profileImg", user.profileImg,
                "age", user.age
            ).addOnSuccessListener {
                Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun saveVideoToFirebase(
        video: Video,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch {

        try {
            val firestoreRef = Firebase.firestore
                .collection("videos")
                .document(current_user.value.username)
                .collection("id")


            firestoreRef.add(video)
                .addOnSuccessListener { documentReference ->
                    // Get the auto-generated document ID
                    val autoGeneratedId = documentReference.id

                    val firestoreRef2 = Firebase.firestore
                        .collection("videos")
                        .document(current_user.value.username)
                        .collection("id")
                        .document(documentReference.id)
                    // Update the document with the generated ID
                    firestoreRef2.update(
                        "videoID", autoGeneratedId
                    ).addOnSuccessListener {
                        Toast.makeText(context, "Successfully Added!", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { e ->
                        Toast.makeText(
                            context,
                            "Failed to update videoID: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "Failed to save video: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }


    fun retrieveVideoData(
        username: String,
        context: Context,
        data: (Result<List<Video>>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        Log.w("retrieveVideoData", "1")

        val firestoreRef = Firebase.firestore
            .collection("videos")
            .document(username)
            .collection("id")

        try {
            firestoreRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val videoList = mutableListOf<Video>()
                    for (document in querySnapshot) {
                        Log.w("retrieveVideo", "1")
                        val video = document.toObject<Video>()
                        Log.w("retrieveVideo", "2")
                        if (video != null) {
                            Log.w("retrieveVideo", "3")
                            videoList.add(video)
                            Log.w("Video ID 1 retrieve", video.videoID)
                        }
                    }
                    if (videoList.isNotEmpty()) {
                        Log.w("retrieveVideo", videoList.joinToString { it.username })
                        data(Result.success(videoList))
                    }
                }

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun retrieveSpecifiedVideoData(
        id: String,
        context: Context,
        data: (Result<Video>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {

        val firestoreRef = Firebase.firestore
            .collection("videos")
            .document(current_user.value.username)
            .collection("id")
            .document(id)

        try {
            firestoreRef.get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val video = it.toObject<Video>()!!
                        data(Result.success(video))
                    } else {
                        Toast.makeText(context, "No Video Data Found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteVideoData(
        id: String,
        context: Context,
        navController: NavController
    )= CoroutineScope(Dispatchers.IO).launch{
        val firestoreRef = Firebase.firestore
            .collection("videos")
            .document(current_user.value.username)
            .collection("id")
            .document(id)

        try{
            firestoreRef.delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully Delete Data", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
        }catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun retrieveNearbyUsers(
        currentUserLocation: LatLng,
        data: (Result<List<NearbyUser>>) -> Unit
    ) {
        val firestoreRef = Firebase.firestore
            .collection("users")

        firestoreRef.get()
            .addOnSuccessListener { querySnapshot ->
                val nearbyUsers = mutableListOf<NearbyUser>()

                for (document in querySnapshot.documents) {
                    Log.w("Near By", "Have document")
                    val user = document.toObject(User::class.java)

                    if (user != null && user.username != current_user.value.username) {

                        Log.w("Near By username", user.username)
                        Log.w("Near By", "In Document User List")
                        var distance = calculateDistance(currentUserLocation, user.location)
                        Log.w("Near By Distance", distance.toString())
                        if (distance <= 10.0) {
                            nearbyUsers.add(NearbyUser(user, distance))
                        }
                    }
                }

                data(Result.success(nearbyUsers))
            }
            .addOnFailureListener { exception ->
                data(Result.failure(exception))
            }
    }

    private fun calculateDistance(currentLocation: LatLng, targetLocation: LatLng): Double {
        val R = 6371 // Radius of the Earth in kilometers
        val lat1 = Math.toRadians(currentLocation.latitude)
        val lon1 = Math.toRadians(currentLocation.longitude)
        val lat2 = Math.toRadians(targetLocation.latitude)
        val lon2 = Math.toRadians(targetLocation.longitude)

        val dlon = lon2 - lon1
        val dlat = lat2 - lat1

        val a = sin(dlat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dlon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c // Distance in kilometers
    }

    fun saveWeightToFirebase(
        weight: Weight,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch {

            val firestoreRef = Firebase.firestore
                .collection("weights")
                .document(current_user.value.username)
                .collection("date")
                .document(weight.date)

            try {
                firestoreRef.set(weight)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Successfully Saved!", Toast.LENGTH_SHORT).show()
                    }

            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
    }

    fun checkExistDate(
        date: String,
        context: Context,
        result: (Boolean) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {

        val firestoreRef = Firebase.firestore
            .collection("weights")
            .document(current_user.value.username)
            .collection("date")
            .document(date)

        try {
            firestoreRef.get()
                .addOnSuccessListener {
                    if (!it.exists()) {
                        result(true)
                    } else {
                        result(false)
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun retrieveWeightData(
        context: Context,
        data: (Result<List<Weight>>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        Log.w("retrieveVideoData", "1")

        val firestoreRef = Firebase.firestore
            .collection("weights")
            .document(current_user.value.username)
            .collection("date")

        try {
            firestoreRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val weightList = mutableListOf<Weight>()
                    for (document in querySnapshot) {
                        Log.w("retrieveWeight", "1")
                        val weight = document.toObject<Weight>()
                        Log.w("retrieveWeight", "2")
                        if (weight != null) {
                            Log.w("retrieveWeight", "3")
                            weightList.add(weight)
                        }
                    }
                    if (weightList.isNotEmpty()) {
                        data(Result.success(weightList))
                    }
                }

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}
