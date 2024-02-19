package com.example.assignment1.screen

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.assignment1.FirebaseViewModel
import com.example.assignment1.R
import com.example.assignment1.data.DataSource.current_user

import com.example.assignment1.model.User



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherUserProfileScreen(navController: NavHostController, username: String?,viewModel: FirebaseViewModel){
    var thisUser by remember { mutableStateOf(User()) }
    Log.w("OtherUserProfile","In screen 1");
    val context = LocalContext.current;
    if (username != null) {
        viewModel.retrieveUserData(username,context){ result -> result.onSuccess { user ->
            // Update the mutable state
            thisUser = User(user.username,"",user.name,user.profileImg,user.age,user.contact,user.location)

        }.onFailure { exception ->
            // Handle the failure result
            Toast.makeText(context, exception.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
        }

        }
    }
    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ){
        IconButton(modifier = Modifier
            .padding(start = 15.dp, top = 15.dp, bottom = 40.dp)
            .height(50.dp)
            .width(50.dp),
            onClick = {
                navController.popBackStack()
            }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                modifier = Modifier.size(100.dp),
                contentDescription = "back_button",
                tint = Color.Black

            )
        }

        val byteArray = Base64.decode(thisUser.profileImg, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        if(bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
            )
        }else{
            Image(
                painterResource(id = R.drawable.baseline_account_box_24),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
            )
        }

        Text(
            text = thisUser.name,
            fontSize = 25.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 10.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        Text(
            text = "Age: " + checkEmpty(thisUser.age),
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier
                .padding(start = 30.dp, bottom = 15.dp, top = 15.dp)
                .align(alignment = Alignment.Start)
        )
        Text(
            text = "Contact: " + checkEmpty(thisUser.contact),
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier
                .padding(start = 30.dp, bottom = 15.dp, top = 15.dp)
                .align(alignment = Alignment.Start)
        )
        Row(
        ) {
            Text(
                text = "Location: ",
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier
                    .padding(start = 30.dp,bottom = 15.dp, top = 15.dp)

            )
            if(thisUser.location.latitude != 0.0 && thisUser.location.longitude != 0.0){
                Column {
                    Text(
                        text = "Latitude: ${thisUser.location.latitude}, Longitude: ${thisUser.location.longitude}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                    )
                }
            }
            else{
                Text(
                    text = " Haven't set yet",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier
                        .padding(bottom = 15.dp, top = 15.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(150.dp))
    }
}





