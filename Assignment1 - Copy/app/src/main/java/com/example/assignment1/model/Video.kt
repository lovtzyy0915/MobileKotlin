package com.example.assignment1.model

import android.content.Context
import android.util.Log
import com.example.assignment1.FirebaseViewModel
import com.example.assignment1.data.DataSource.current_user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

data class Video(
    var videoID: String = "",
    var username: String = "",
    var videoName: String = "",
    var videoUrl: String = "",
    var videoType: String = "",
    var videoDuration: String = "",
    var videoThumbnail: String = "",
    var videoAuthor: String = ""

)


fun extractVideoId(youtubeUrl: String): String? {
    val pattern =
        "(?<=watch\\?v=|\\videos\\|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed\\?src=|src\\?feature=player_embedded&v=|&v=)([^&=%?]{11})"
    val compiledPattern = Pattern.compile(pattern)
    val matcher = compiledPattern.matcher(youtubeUrl)

    return if (matcher.find()) {
        matcher.group(1)
    } else {
        null
    }
}

fun loadVideo(viewModel: FirebaseViewModel, context: Context, callback:(List<Video>) -> Unit){
    val videos = mutableListOf<Video>()
    viewModel.retrieveVideoData(
        username = current_user.value.username,
        context = context
    ) { result ->
        result.onSuccess {
            videoList ->
            videos.addAll(videoList)
            Log.w("Video ID 1 load", videos[0].videoID)
            callback(videos)
    }.onFailure {
        // Handle the failure result
            //Toast.makeText(context, exception.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
        callback(emptyList())
    }
    }
    Log.w("loadVideo", "2")

}


suspend fun addVideo(apiUrl: String, videoName: String, videoType: String, viewModel: FirebaseViewModel, context: Context): String {

    Log.w("API testing make Request", apiUrl)
    var videoThumbnail = extractVideoId(apiUrl)
    return withContext(Dispatchers.IO) {
        try {
            // Create URL object
            val url = URL(apiUrl)


            // Open connection
            val urlConnection = url.openConnection() as HttpURLConnection

            // Set up the request method and other properties
            urlConnection.requestMethod = "GET"
            urlConnection.connectTimeout = 5000
            urlConnection.readTimeout = 5000

            // Get the response code
            val responseCode = urlConnection.responseCode

            // Read the response if the request was successful (HTTP 200 OK)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = urlConnection.inputStream
                val bufferedReader = inputStream.bufferedReader()
                // Read the response as a string
                val response = bufferedReader.use { it.readText() }

                // Disconnect the HttpURLConnection
                urlConnection.disconnect()

                Log.w("API testing make Request response", response)
                val authorPattern = "\"author\":\"([^\"]+)\""

                val durationPattern = "\"lengthSeconds\":\"(\\d+)\""
                val compiledPattern1 = Pattern.compile(authorPattern)
                val compiledPattern2 = Pattern.compile(durationPattern)

                val matcher1 = compiledPattern1.matcher(response)
                val matcher2 = compiledPattern2.matcher(response)

                val author = if (matcher1.find()) matcher1.group(1) else ""
                val duration = if (matcher2.find()) matcher2.group(1).toDouble()/60 else ""
                val formattedDuration = String.format("%.2f", duration)


                val newVideo: Video? = videoThumbnail?.let {
                    Video("",current_user.value.username,videoName, apiUrl, videoType, formattedDuration, it, author)
                }

                if (newVideo != null) {
                    viewModel.saveVideoToFirebase(newVideo, context)
                    /*videos.add(newVideo) // Append the newVideo to the videos list*/
                } else {
                    Log.w("SaveVideo", "Failed to extract video ID or newVideo is null.")
                }
                return@withContext response
            } else {
                // Disconnect the HttpURLConnection
                urlConnection.disconnect()

                throw Exception("API testing Error: $responseCode")
            }
        } catch (e: Exception) {
            Log.w("API testing Error: ", e)
            throw Exception("API testing Error: ${e.message}")

        }
    }
}