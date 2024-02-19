package com.example.assignment1.screen


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
import androidx.compose.material3.AlertDialog
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
import com.example.assignment1.model.User


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavHostController,
    viewModel: FirebaseViewModel
){
    var showAlert by remember { mutableStateOf(false) }
    var showAlertDialog by remember { mutableStateOf(false) }

    var usernameInput:String by remember { mutableStateOf("") }
    var passwordInput:String by remember { mutableStateOf("") }
    var confirmPasswordInput:String by remember { mutableStateOf("") }
    var nameInput:String by remember { mutableStateOf("") }

    val context = LocalContext.current
    Column(
        modifier = Modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        val img = painterResource(R.drawable.sign_up)
        Image(
            painter = img,
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 25.dp))

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
        OutlinedTextField(
            value = confirmPasswordInput,
            onValueChange = {confirmPasswordInput = it},
            placeholder = {
                Icon(
                    painter = painterResource(R.drawable.baseline_lock_24),
                    contentDescription = null
                    )
                Text(text = "Confirm Password",
                    modifier = Modifier.padding(start = 30.dp))
            },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = nameInput,
            onValueChange = {nameInput = it},
            placeholder = {
                Icon(
                    painter = painterResource(R.drawable.baseline_border_color_24),
                    contentDescription = null)
                Text(text = "Your Profile Name",
                    modifier = Modifier.padding(start = 30.dp))
            },
            singleLine = true,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth()
        )
        Button(
            modifier = Modifier
                .padding(top = 15.dp)
                .fillMaxWidth(),

            onClick = {
                if(usernameInput == "" || passwordInput == "" || confirmPasswordInput == "" || nameInput == ""){
                        Toast.makeText(context, "All data fields are mandatory", Toast.LENGTH_SHORT).show()
                }
                else{
                    viewModel.checkExistUsername(usernameInput, context){
                            isUsernameAvailable ->
                        if(isUsernameAvailable){
                            if(passwordInput == confirmPasswordInput){
                                val user = User(
                                    username = usernameInput,
                                    name = nameInput,
                                    password = passwordInput,
                                )
                                viewModel.saveUser(user = user, context = context)
                                showAlert = true
                            }
                            else{
                                Toast.makeText(context, "Inconsistent passwords", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Toast.makeText(context, "Username already exist", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        ){
            Text(
                fontSize = 25.sp,
                text="Sign Up"
            )
        }


        Spacer(modifier = Modifier.height(50.dp))
        Row(){
            Text(text = "Already have an account? " )
            Text(text = "Sign In",
                color = Color.Blue,
                modifier = Modifier.clickable{
                    navController.navigate("Login Screen")
                })
        }
    }
    if (showAlert) {
         AlertDialog(
             onDismissRequest = {
                 showAlert = false
             },
             title = {
                 Text("Sign Up Successfully")
             },
             text = {
                 Text("Navigate to Log In Page.")
             },
             confirmButton = {
                 Button(
                     onClick = {
                         showAlert = false
                         showAlertDialog = false
                         navController.navigate(route = "Login Screen")
                     }
                 ) {
                     Text("OK")
                 }
             }
         )
     }

}




