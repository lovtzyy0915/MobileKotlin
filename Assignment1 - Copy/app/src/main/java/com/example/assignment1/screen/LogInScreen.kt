package com.example.assignment1.screen


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Icon
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.assignment1.FirebaseViewModel
import com.example.assignment1.R
import com.example.assignment1.data.DataSource.current_user
import com.example.assignment1.model.LatLng
import com.example.assignment1.model.User
import kotlin.Result


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: FirebaseViewModel
){
    var username:String by remember { mutableStateOf("")}
    var name:String by remember { mutableStateOf("")}
    var age:String by remember { mutableStateOf("") }
    var profileImg:String by remember { mutableStateOf("")}
    var contact:String by remember { mutableStateOf("")}

    var location by remember {
        mutableStateOf(
            LatLng(
                0.0,
                0.0
            )
        )
    }


    var usernameInput:String by remember { mutableStateOf("") }
    var passwordInput:String by remember { mutableStateOf("") }

    val context = LocalContext.current
    Column(
        modifier = Modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        val img = painterResource(R.drawable.running_icons)
        Image(
            painter = img,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp))

        Text(
            text = "Sign In",
            fontSize = 25.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 20.dp, top = 30.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        OutlinedTextField(
            value = usernameInput,
            onValueChange = {usernameInput = it},
            placeholder = {
                Icon(
                    painter = painterResource(R.drawable.baseline_account_box_24),
                    contentDescription = null)
                Text(text = "Username",
                    modifier = Modifier.padding(start = 30.dp))},
            singleLine = true,
            modifier = Modifier
                .padding(bottom = 25.dp)
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = passwordInput,
            onValueChange = {passwordInput = it},
            placeholder = {
                Icon(
                    painter = painterResource(R.drawable.baseline_lock_24),
                    contentDescription = null)
                Text(text = "Password",
                modifier = Modifier.padding(start = 30.dp))
                          },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth()
        )
        Button(
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxWidth(),

            onClick = {
                if(usernameInput == "" || passwordInput == ""){
                    Toast.makeText(context, "All data fields are mandatory", Toast.LENGTH_SHORT).show()
                }
                else{
                    viewModel.checkUserPassword(
                        username = usernameInput,
                        password = passwordInput,
                        context = context
                    ) { result -> result.onSuccess {
                            user ->
                        username = user.username
                        name = user.name
                        profileImg = user.profileImg
                        age = user.age
                        contact = user.contact
                        location = user.location

                        // Update the mutable state
                        current_user.value = User(username,"",name,profileImg,age,contact,location)
                        navController.navigate("Home Screen")
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
                text="Login"
            )
        }


        Spacer(modifier = Modifier.height(100.dp))
        Row(){
            Text(text = "Do not have an account?" )
            Text(text = "Sign Up",
                color = Color.Blue,
                modifier = Modifier.clickable{
                    navController.navigate("Sign Up Screen")
                })
        }
    }
}




