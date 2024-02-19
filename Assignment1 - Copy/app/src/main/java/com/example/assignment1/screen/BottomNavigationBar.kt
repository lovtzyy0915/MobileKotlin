package com.example.assignment1.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.assignment1.FirebaseViewModel
import com.example.assignment1.Nav
import com.example.assignment1.R

@Composable
fun BottomNavigationBar(navController: NavHostController, initialScreen: Int) {
    val selectedItem = rememberSaveable{ mutableIntStateOf(initialScreen) }

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()

    ){
        NavigationBar(
            Modifier.background(Color.Red)

        ){
            NavigationBarItem(
                icon = {
                    Icon(painter = painterResource(R.drawable.baseline_find_in_page_24), contentDescription = null,
                        tint = if(selectedItem.value == 0) Color(0xFF406F9E) else Color.Black)
                },
                label = {
                    Text("Find",
                        color = if(selectedItem.value == 0) Color(0xFF406F9E) else Color.Black)
                },
                selected = selectedItem.value == 0,
                onClick = {selectedItem.value = 0},
            )
            NavigationBarItem(
                icon = {
                    Icon(painter = painterResource(R.drawable.baseline_subscriptions_24), contentDescription = null,
                        tint = if(selectedItem.value == 1) Color(0xFF406F9E) else Color.Black)


                },
                label = {
                    Text("Videos",
                        color = if(selectedItem.value == 1) Color(0xFF406F9E) else Color.Black)
                },
                selected = selectedItem.value == 1,
                onClick = {selectedItem.value = 1}
            )
            NavigationBarItem(
                icon = {
                    Icon(painter = painterResource(R.drawable.baseline_account_box_24), contentDescription = null,
                        tint = if(selectedItem.value == 2) Color(0xFF406F9E) else Color.Black)
                },
                label = {
                    Text("Profile",
                        color = if(selectedItem.value == 2) Color(0xFF406F9E) else Color.Black)
                },
                selected = selectedItem.value == 2,
                onClick = {selectedItem.value = 2
                    Log.w("OtherUserProfile","in Bottom Navigation Bar");}
            )
        }
    }
    when(selectedItem.value){
        -1 -> Nav()
        0 -> FindScreen(navController, FirebaseViewModel())
        1 -> HomeScreen(navController, FirebaseViewModel())
        2 -> ProfileScreen(navController)
    }

}