package com.example.assignment1.screen

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.assignment1.FirebaseViewModel
import com.example.assignment1.R
import com.example.assignment1.data.DataSource
import com.example.assignment1.data.DataSource.current_user
import com.example.assignment1.model.NearbyUser
import com.example.assignment1.model.User
import com.example.assignment1.model.Video
import java.text.DecimalFormat
import kotlin.reflect.typeOf

@Composable
fun FindScreen(navController: NavHostController,
               viewModel: FirebaseViewModel){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
    ){
        Text(
            text = "  Find nearby",
            fontSize = 25.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.TopCenter)
        )
        var nearByUsers by remember { mutableStateOf(emptyList<NearbyUser>()) }
        viewModel.retrieveNearbyUsers(
            currentUserLocation = current_user.value.location
        ) { result ->
            result.onSuccess {
                    userList ->
                nearByUsers = userList
            }.onFailure {
                //Toast.makeText(context, exception.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
            }
        }
        if(nearByUsers.isNotEmpty()){
            Log.w("Near By", "Call List")
            nearbyList(userList = nearByUsers, modifier = Modifier.padding(bottom = 100.dp, top = 50.dp), navController)
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
}

@Composable
fun nearbyList(userList: List<NearbyUser>, modifier: Modifier=Modifier, navController: NavHostController){
    LazyColumn(modifier= modifier){
        Log.w("Near By", "In List")
        items(userList){
                User ->
            nearbyCard(
                user = User.user,
                distance = User.distance,
                modifier = Modifier.padding(8.dp),
                navController
            )
        }
    }
}

@Composable
fun nearbyCard(user:User, distance:Double,modifier: Modifier=Modifier, navController: NavHostController){
    Card(modifier = modifier
        .clickable{
            Log.w("OtherUserProfile","clicked");
            navController.navigate(route = "OtherUserProfileScreen/${user.username}")
        }){
        Log.w("Near By", "In Card")
        Column{
            val byteArray = Base64.decode(user.profileImg, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            if(bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                )
            }else{
                Image(
                    painterResource(id = R.drawable.baseline_account_box_24),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                )
            }

            Text(
                text = user.name,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            Row(modifier = Modifier.padding(start = 10.dp)){
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    modifier = Modifier.size(30.dp),
                    contentDescription = "back_button",
                    tint = Color.Black
                )

                val df:DecimalFormat = DecimalFormat("0.00");

                Log.w("original distance:", distance.toString())
                Log.w("format distance:", df.format(distance.toDouble()))
                Log.w("test format distance:", String.format("%.2f", 3.223))
                Text(
                    text = distance.toString() + " km",
                    modifier = Modifier.padding(start = 10.dp, bottom =20.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}