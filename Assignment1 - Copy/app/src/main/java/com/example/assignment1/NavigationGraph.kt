package com.example.assignment1

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.assignment1.screen.BottomNavigationBar
import com.example.assignment1.screen.DetailScreen
import com.example.assignment1.screen.EditProfileScreen
import com.example.assignment1.screen.InsertScreen
import com.example.assignment1.screen.LoginScreen
import com.example.assignment1.screen.OtherUserProfileScreen
import com.example.assignment1.screen.SignUpScreen
import com.example.assignment1.screen.WeightScreen
import com.example.assignment1.screen.weightTrackingScreen


@Composable
fun Nav(){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Login Screen"){
        composable(route = "Login Screen"){
            LoginScreen(navController = navController, viewModel = FirebaseViewModel())
        }
        composable(route = "Sign Up Screen"){
            SignUpScreen(navController = navController, viewModel = FirebaseViewModel())
        }

        composable(route = "Insert Screen"){
            InsertScreen(navController)
        }

        composable(route = "Weight Tracking Screen"){
            weightTrackingScreen(navController, viewModel = FirebaseViewModel())
        }


        composable(route = "Weight Screen"){
            WeightScreen(navController, viewModel = FirebaseViewModel())
        }

        composable(route = "Find Screen"){
            BottomNavigationBar(navController = navController, initialScreen = 0)
        }

        composable(route = "Edit Profile Screen"){
            //EditProfileScreen(navController = navController, viewModel = FirebaseViewModel())
            EditProfileScreen(navController = navController, viewModel = FirebaseViewModel())
        }

        composable(route = "Profile Screen"){
            BottomNavigationBar(navController = navController,
                initialScreen = 2)
        }

        composable(route = "Home Screen"){
            BottomNavigationBar(navController = navController,
                initialScreen = 1)
        }

        composable(
            route = "OtherUserProfileScreen/{username}",
            arguments = listOf(
                navArgument(name = "username") {
                    type = NavType.StringType
                }
            )
        ) {
            Log.w("OtherUserProfile","In graph");
            val username = it.arguments?.getString("username")
            OtherUserProfileScreen(
                username = username,
                navController = navController,
                viewModel = FirebaseViewModel()
            )
        }


        composable(
            route = "DetailScreen/{videoID}",
            arguments = listOf(
                navArgument(name = "videoID") {
                    type = NavType.StringType
                }
            )
        ) {
            val videoID = it.arguments?.getString("videoID")
            DetailScreen(
                videoId = videoID,
                navController = navController,
                viewModel = FirebaseViewModel()
            )
        }

    }
}