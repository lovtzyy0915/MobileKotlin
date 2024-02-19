package com.example.assignment1.screen

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.assignment1.FirebaseViewModel
import com.example.assignment1.data.DataSource.current_video
import com.example.assignment1.model.Video
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    videoId: String?,
    viewModel: FirebaseViewModel,
    navController: NavHostController
) {

    var videoID: String by remember { mutableStateOf("") }
    var userNAME: String by remember { mutableStateOf("") }
    var videoNAME: String by remember { mutableStateOf("") }
    var videoURL: String by remember { mutableStateOf("") }
    var videoTYPE: String by remember { mutableStateOf("") }
    var videoDURATION: String by remember { mutableStateOf("") }
    var videoTHUMBNAIL: String by remember { mutableStateOf("") }
    var videoAUTHOR: String by remember { mutableStateOf("") }
    var webView: WebView? by remember { mutableStateOf(null) }

    val context = LocalContext.current

    if (videoId != null) {
        LaunchedEffect(videoId) {
            try {
                viewModel.retrieveSpecifiedVideoData(
                    id = videoId,
                    context = context
                ) { result ->
                    result.onSuccess { video ->
                        videoTHUMBNAIL = video.videoThumbnail
                        videoID = video.videoID
                        userNAME = video.username
                        videoNAME = video.videoName
                        videoURL = video.videoUrl
                        videoTYPE = video.videoType
                        videoDURATION = video.videoDuration
                        videoAUTHOR = video.videoAuthor
                        Log.w("current video thumbsnail 1", videoTHUMBNAIL)
                        // Update the mutable state
                        current_video.value = Video(
                            videoID,
                            userNAME,
                            videoNAME,
                            videoURL,
                            videoTYPE,
                            videoDURATION,
                            videoTHUMBNAIL,
                            videoAUTHOR
                        )
                    }.onFailure { exception ->
                        // Handle the failure result
                        Toast.makeText(
                            context,
                            exception.message ?: "Unknown error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    val video =
                        "<iframe width=\"100%\" height=\"40%\" src=\"https://www.youtube.com/embed/${current_video.value.videoThumbnail}\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
                    webView?.loadData(video, "text/html", "utf-8")


                }


            } catch (e: Exception) {
                // Handle exceptions if any
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }


        Column(
            modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF21486F)),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp),
                    onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "back_button",
                        tint = Color.White

                    )
                }
                Text(
                    text = current_video.value.videoName,
                    color = Color.White,
                    fontSize = 25.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 15.dp)
                )
            }
            //val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
            Box(modifier.fillMaxWidth()) {

                AndroidView(factory = { context ->
                    WebView(context).apply {
                        webView = this
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        Log.w("current video thumbsnail 2", current_video.value.videoThumbnail)
                        loadData("Loading Youtube Video...", "text/html", "utf-8")
                        settings.javaScriptEnabled = true
                        webChromeClient = WebChromeClient()
                    }
                })
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 250.dp, start = 20.dp)
                ) {
                    Text(
                        text = "Type: ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = current_video.value.videoType,
                        fontSize = 20.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 290.dp, start = 20.dp)
                ) {
                    Text(
                        text = "Author: ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = current_video.value.videoAuthor,
                        fontSize = 20.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 330.dp, start = 20.dp)
                ) {
                    Text(
                        text = "Duration: ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = current_video.value.videoDuration,
                        fontSize = 20.sp
                    )
                    Text(
                        text = " mins",
                        fontSize = 20.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 370.dp, start = 20.dp)
                ) {
                    Text(
                        text = "URL: ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    val annotatedString = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                textDecoration = TextDecoration.Underline,
                                color = Color.Blue,
                                fontSize = 20.sp
                            )
                        ) {
                            append(current_video.value.videoUrl)
                            addStringAnnotation(
                                tag = "URL",
                                annotation = current_video.value.videoUrl,
                                start = 0,
                                end = current_video.value.videoUrl.length
                            )
                        }
                    }
                    val uriHandler = LocalUriHandler.current
                    ClickableText(
                        text = annotatedString,
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(
                                tag = "URL",
                                start = offset,
                                end = offset
                            )
                                .firstOrNull()?.let { annotation ->
                                    uriHandler.openUri(annotation.item)
                                }
                        }
                    )
                }
                Button(
                    modifier = Modifier
                        .padding(top = 500.dp, start = 50.dp, end = 50.dp)
                        .fillMaxWidth(),
                    onClick = {
                        viewModel.deleteVideoData(
                            current_video.value.videoID,
                            context,
                            navController
                        )
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFFDC1B00)),
                ) {
                    Text(
                        fontSize = 20.sp,
                        color = Color.White,
                        text = "Delete Video"
                    )
                }
            }
        }
    }


}

@Composable
fun VideoDetail(
    modifier: Modifier,
    navController: NavHostController,
    viewModel: FirebaseViewModel,
    context: Context
): Unit {
    Column(
        modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF21486F)),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(modifier = Modifier
                .padding(start = 15.dp, top = 15.dp),
                onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "back_button",
                    tint = Color.White

                )
            }
            Text(
                text = current_video.value.videoName,
                color = Color.White,
                fontSize = 25.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 15.dp)
            )
        }
        //val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        Box(modifier.fillMaxWidth()) {

            AndroidView(factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    Log.w("current video thumbsnail 2", current_video.value.videoThumbnail)
                    val video =
                        "<iframe width=\"100%\" height=\"40%\" src=\"https://www.youtube.com/embed/${current_video.value.videoThumbnail}\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"

                    loadData(video, "text/html", "utf-8")
                    settings.javaScriptEnabled = true
                    webChromeClient = WebChromeClient()
                }
            })
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 250.dp, start = 20.dp)
            ) {
                Text(
                    text = "Type: ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = current_video.value.videoType,
                    fontSize = 20.sp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 290.dp, start = 20.dp)
            ) {
                Text(
                    text = "Author: ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = current_video.value.videoAuthor,
                    fontSize = 20.sp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 330.dp, start = 20.dp)
            ) {
                Text(
                    text = "Duration: ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = current_video.value.videoDuration,
                    fontSize = 20.sp
                )
                Text(
                    text = " mins",
                    fontSize = 20.sp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 370.dp, start = 20.dp)
            ) {
                Text(
                    text = "URL: ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                val annotatedString = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            textDecoration = TextDecoration.Underline,
                            color = Color.Blue,
                            fontSize = 20.sp
                        )
                    ) {
                        append(current_video.value.videoUrl)
                        addStringAnnotation(
                            tag = "URL",
                            annotation = current_video.value.videoUrl,
                            start = 0,
                            end = current_video.value.videoUrl.length
                        )
                    }
                }
                val uriHandler = LocalUriHandler.current
                ClickableText(
                    text = annotatedString,
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(
                            tag = "URL",
                            start = offset,
                            end = offset
                        )
                            .firstOrNull()?.let { annotation ->
                                uriHandler.openUri(annotation.item)
                            }
                    }
                )
            }
            Button(
                modifier = Modifier
                    .padding(top = 500.dp, start = 50.dp, end = 50.dp)
                    .fillMaxWidth(),
                onClick = {
                    viewModel.deleteVideoData(
                        current_video.value.videoID,
                        context,
                        navController
                    )
                },
                colors = ButtonDefaults.buttonColors(Color(0xFFDC1B00)),
            ) {
                Text(
                    fontSize = 20.sp,
                    color = Color.White,
                    text = "Delete Video"
                )
            }
        }
    }
}

