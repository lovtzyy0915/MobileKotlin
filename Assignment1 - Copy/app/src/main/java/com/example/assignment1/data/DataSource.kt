package com.example.assignment1.data


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.assignment1.FirebaseViewModel
import com.example.assignment1.model.LatLng
import com.example.assignment1.model.User
import com.example.assignment1.model.Video
import com.example.assignment1.model.Weight
import com.example.assignment1.model.addVideo


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



object DataSource {

    var current_user = mutableStateOf( User(
        username = "",
        password = "",
        name = "",
        age = "",
        profileImg = "",
        contact = "",
        location = LatLng(0.0, 0.0)
    ))
    var current_video = mutableStateOf( Video(
        videoID = "",
        username = "",
        videoName = "",
        videoUrl ="",
        videoType = "",
        videoDuration = "",
        videoThumbnail = "",
        videoAuthor = ""
    ))


}



@Composable
fun saveVideo(youtubeUrl: String, videoName: String, videoType: String){
    Log.w("Save Video", "Hi")
    val context = LocalContext.current
    var apiResponse by remember { mutableStateOf("") }
    LaunchedEffect(youtubeUrl) {
        try {

            // Make the API request on the IO dispatcher
            withContext(Dispatchers.IO) {
                val result = addVideo(youtubeUrl, videoName, videoType, FirebaseViewModel(), context)

                // Update the state on the main thread
                withContext(Dispatchers.Main) {
                    apiResponse = result
                }

            }

        } catch (e: Exception) {
            // Handle exceptions if needed
            apiResponse = "Error: ${e.message}"
        }
    }


}


