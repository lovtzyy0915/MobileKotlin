package com.example.assignment1.screen

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.assignment1.R
import com.example.assignment1.data.DataSource
import com.example.assignment1.data.DataSource.current_user
import com.example.assignment1.data.saveVideo
import com.example.assignment1.model.LatLng
import com.example.assignment1.model.User



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController){
    var showAlert by remember { mutableStateOf(false) }
    var showAlertDialog by remember { mutableStateOf(false) }


    var nameInput by remember { mutableStateOf("") }
    var typeInput by remember { mutableStateOf("") }
    var urlInput by remember { mutableStateOf("") }

    val currentUser = DataSource.current_user.value

    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ){
        Text(text = "Edit",
            fontSize = 25.sp,
            color = Color.Blue,
            modifier = Modifier
                .align(alignment = Alignment.End)
                .clickable {
                    navController.navigate("Edit Profile Screen")
                })

        val byteArray = Base64.decode(current_user.value.profileImg, Base64.DEFAULT)
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
            text = currentUser.name,
            fontSize = 25.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 10.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        Text(
            text = "Age: " + checkEmpty(currentUser.age),
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier
                .padding(start = 30.dp, bottom = 15.dp, top = 15.dp)
                .align(alignment = Alignment.Start)
        )
        Text(
            text = "Contact: " + checkEmpty(currentUser.contact),
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
            if(currentUser.location.latitude != 0.0 && currentUser.location.longitude != 0.0){
                Column {
                    Text(
                        text = "Latitude: ${currentUser.location.latitude}, Longitude: ${currentUser.location.longitude}",
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


        Button(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth(),

            onClick = {
                navController.navigate("Weight Tracking Screen")
            }
        ){
            Text(
                fontSize = 25.sp,
                text="Weight Record"
            )
        }
        Button(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            onClick = {
                showAlert = true
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF6C8BAA)),
        ){
            Text(
                fontSize = 20.sp,
                text="Logout"
            )
        }


        Spacer(modifier = Modifier.height(150.dp))
    }
    if (showAlert) {
        if(!showAlertDialog){
            saveVideo(urlInput, nameInput, typeInput)
            showAlertDialog = true
        }
        Log.w("show Alert", "1")
        AlertDialog(
            onDismissRequest = {
                showAlert = false
            },
            title = {
                Text("Logout")
            },
            text = {
                Text("Are you confirm to logout?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showAlert = false
                        showAlertDialog = false
                        navController.navigate(route = "Login Screen")
                        current_user.value = User(
                            username = "",
                            password = "",
                            name = "",
                            age = "",
                            profileImg = "",
                            contact = "",
                            location = LatLng(0.0,0.0)
                        )
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showAlert = false
                        showAlertDialog = false
                    }
                ){
                    Text("Cancel")
                }
            }

        )
    }

}

fun checkEmpty(parameter: String): String {
    if(parameter == ""){
        return "Haven't set yet"
    }else{
        return parameter
    }
}





