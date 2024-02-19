package com.example.assignment1.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.assignment1.FirebaseViewModel
import com.example.assignment1.model.Video
import com.example.assignment1.model.loadVideo


@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: FirebaseViewModel
){
    val context = LocalContext.current
    var videoList by remember { mutableStateOf(emptyList<Video>()) }
    loadVideo(viewModel, context){
        loadedVideoList ->
        videoList = loadedVideoList
        Log.w("Video ID 1 home", videoList[0].videoID)
    }
    var clicked by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
    ){
        Text(
            text = "  Your Fitness Videos",
            fontSize = 25.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.TopCenter)
        )
        if(videoList.isNotEmpty()){
            VideoList(videoList = videoList, modifier = Modifier.padding(bottom = 100.dp, top = 50.dp), navController)
        }
        else{
            Text(text = "Empty",
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier
                    .padding(top = 100.dp)
                    .align(alignment = Alignment.TopCenter)
            )
        }
    }
    Box(

        contentAlignment = Alignment.BottomEnd,
    ) {
        IconButton(modifier = Modifier
            .padding(bottom = 120.dp, end = 30.dp)
            .height(75.dp)
            .width(75.dp)
            .background(Color(0xFF406F9E), CircleShape),
            onClick = { clicked = true}) {
            Icon(
                imageVector = Icons.Filled.Add,
                modifier = Modifier.size(100.dp),
                contentDescription = "add_button",
                tint = Color.White
            )
        }
    }
    if(clicked){
        navController.navigate("Insert Screen")
        clicked = false
    }
}

@Composable
fun VideoList(videoList: List<Video>, modifier: Modifier=Modifier, navController: NavHostController){
    LazyColumn(modifier= modifier){

        items(videoList){
            video ->
            Log.w("Video ID 1", video.videoID)
            VideoCard(video = video,
                modifier = Modifier.padding(8.dp),
                navController,
                videoID = video.videoID,
            )
        }
    }
}

@Composable
fun VideoCard(video: Video, modifier: Modifier=Modifier, navController: NavHostController,   videoID: String){
    Card(modifier = modifier
        .clickable{
            Log.w("Video ID 1", videoID)
            navController.navigate(route = "DetailScreen/$videoID")
        }){
        Column{
            Image(
                painter = rememberAsyncImagePainter(model = "https://img.youtube.com/vi/" + video.videoThumbnail + "/0.jpg"),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(194.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = video.videoName,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
