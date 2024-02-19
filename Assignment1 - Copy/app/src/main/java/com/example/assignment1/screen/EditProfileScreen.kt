package com.example.assignment1.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationRequest
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.assignment1.FirebaseViewModel
import com.example.assignment1.R
import com.example.assignment1.data.DataSource.current_user
import com.example.assignment1.model.LatLng
import com.example.assignment1.model.User
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.location.*
import android.os.Looper


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavHostController,
    viewModel: FirebaseViewModel
){

    var nameInput:String by remember { mutableStateOf(current_user.value.name) }
    var contactInput:String by remember { mutableStateOf(current_user.value.contact) }
    var ageInput:String by remember { mutableStateOf(current_user.value.age) }
    val context = LocalContext.current
    var myLocation by remember {
        mutableStateOf(
            LatLng(
                current_user.value.location.latitude,
                current_user.value.location.longitude
            )
        )
    }
    var getMaps by remember { mutableStateOf(false) }
    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    // Request location permissions
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission is granted, do nothing here
            } else {
                Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
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
        Text(
            text = "  Edit Profile",
            fontSize = 30.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp)
        )
    }

    Column(
        modifier = Modifier.padding(top = 80.dp, start = 40.dp, end = 40.dp),
        horizontalAlignment = Alignment.Start
    ){
        var uri by remember{
            mutableStateOf<Uri?>(null)
        }
        var uriBase64Image by remember{
            mutableStateOf<String?>(null)
        }


        val singlePhotoPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = {uri = it}
        )

        Row(modifier = Modifier.padding(bottom = 10.dp)) {
            Text(text = "Profile Image",
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 10.dp, top = 10.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(Color(0xFF6C8BAA)),
                onClick = {
                singlePhotoPicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) {
                Text("Upload",
                    fontSize = 17.sp)
            }
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .size(62.dp)
                    .padding(start = 10.dp)
            )
        }

        Row(modifier = Modifier.padding(bottom = 10.dp)){
            Text(text = "Name",
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 10.dp, end = 10.dp, bottom = 10.dp)
            )
            OutlinedTextField(
                value = nameInput,
                onValueChange = {nameInput = it},
                placeholder = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_account_box_24),
                        contentDescription = null)
                    Text(text = "Username",
                        modifier = Modifier.padding(start = 30.dp))},
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Row(modifier = Modifier.padding(bottom = 10.dp)){
            Text(text = "Age",
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 10.dp, end = 10.dp, bottom = 10.dp)
            )
            OutlinedTextField(
                value = ageInput,
                onValueChange = {ageInput = it},
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Row(modifier = Modifier.padding(bottom = 10.dp)){
            Text(text = "Contact",
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 10.dp, end = 10.dp, bottom = 10.dp)
            )
            OutlinedTextField(
                value = contactInput,
                onValueChange = {contactInput = it},
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()

            )
        }
        Row(modifier = Modifier.padding(bottom = 10.dp)) {
            Text(
                text = "Location",
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 10.dp, top = 10.dp)
            )
            if (!getMaps) {
                Button(colors = ButtonDefaults.buttonColors(Color(0xFF6C8BAA)),
                    onClick = {
                        Log.w("myLocation", "Clicked")
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Log.w("myLocation", "Accepted")
                        // Permission is already granted, get the location
                        getLocation(context) { lat, long ->
                            myLocation = LatLng(lat, long)
                            Log.w("myLocation", lat.toString())
                            getMaps = true
                        }
                    } else {
                        // Request location permission
                        Log.w("myLocation", "Denied")
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }) {
                    Text(text = "Get Location",
                        fontSize = 17.sp)
                }
            } else {
                val mapsLatLng = com.google.android.gms.maps.model.LatLng(
                    myLocation.latitude,
                    myLocation.longitude
                )
                var cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(mapsLatLng, 2f)
                }
                Column {
                    Text(
                        text = "Latitude: ${myLocation.latitude}, Longitude: ${myLocation.longitude}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                    )
                    GoogleMap(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(start = 10.dp),
                        cameraPositionState = cameraPositionState,
                        uiSettings = uiSettings.copy(zoomControlsEnabled = true)
                    ) {
                        Marker(
                            state = MarkerState(position = mapsLatLng),
                            title = "Current Location",
                            snippet = "Marker"
                        )
                    }
                }
            }
        }

        Button(
            modifier = Modifier
                .padding(top = 25.dp)
                .fillMaxWidth(),

            onClick = {
                if(nameInput == ""){
                    nameInput = current_user.value.name
                }
                if(contactInput == ""){
                    contactInput = current_user.value.contact
                }
                if(myLocation.latitude == 0.0 && myLocation.longitude == 0.0){
                    myLocation = current_user.value.location
                }
                if(ageInput == ""){
                    ageInput = current_user.value.age
                }
                if(uri == null){
                    uriBase64Image = current_user.value.profileImg
                }
                else{
                    uriBase64Image = uri?.let { convertImageToBase64(it, context) }
                    //uriBase64Image?.let { Log.w("Base64", it) }
                }
                Log.w("update User", "1")
                var updatedUser = uriBase64Image?.let { User(name = nameInput, contact = contactInput, location = myLocation, profileImg = it, age = ageInput) }
                if (updatedUser != null) {
                    Log.w("update User", "2")
                    viewModel.updateUser(updatedUser, context)
                    viewModel.retrieveUserData(current_user.value.username,context){
                        result -> result.onSuccess {
                                user ->
                            // Update the mutable state
                            current_user.value = User(user.username,"",user.name,user.profileImg,user.age,user.contact,user.location)
                            navController.navigate("Profile Screen")
                        }.onFailure { exception ->
                            // Handle the failure result
                            Toast.makeText(context, exception.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
                }
        ){
            Text(
                fontSize = 25.sp,
                text="Save"
            )
        }


    }
}


private fun convertImageToBase64(uri: Uri, context: Context): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bytes = inputStream?.readBytes()
    inputStream?.close()

    return Base64.encodeToString(bytes, Base64.DEFAULT)
}



@SuppressLint("MissingPermission")
private fun getLocation(context: Context, callback: (Double, Double) -> Unit){
    Log.w("get Location", "In")
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    Log.w("get Location", "In 2")

    fusedLocationClient.lastLocation
        .addOnSuccessListener {
                location ->
            Log.w("get Location", "In 3")
            Log.w("get Location", location.toString())
            if (location != null) {
                val lat = location.latitude
                val long = location.longitude
                Log.w("get Location lat", lat.toString())
                Log.w("get Location long", long.toString())
                callback(lat, long)
                Log.w("get Location", "In 4")
            }
        }
        .addOnFailureListener{
                exception -> exception.printStackTrace()
            Log.w("get Location", "Failed")

        }
}

