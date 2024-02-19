package com.example.assignment1.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.assignment1.R
import com.example.assignment1.data.saveVideo
import com.example.assignment1.ui.theme.Assignment1Theme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertScreen(navController: NavHostController){
    var showAlert by remember { mutableStateOf(false) }
    var showAlertDialog by remember { mutableStateOf(false) }


    var nameInput by remember { mutableStateOf("") }
    var typeInput by remember { mutableStateOf("") }
    var urlInput by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box() {
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
    }
    Column(
        modifier = Modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        val img = painterResource(R.drawable.video)
        Image(
            painter = img,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp))


        Text(
            text = "Insert Fitness Video",
            fontSize = 25.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 20.dp, top = 30.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        OutlinedTextField(
            value = nameInput,
            onValueChange = {nameInput = it},
            placeholder = {Text(text = "Name your video")},
            singleLine = true,
            modifier = Modifier
                .padding(bottom = 25.dp)
                .fillMaxWidth())

        Spinner(
            context = LocalContext.current,
            itemsResId = R.array.fitness_type,
            selectedItem = typeInput,
            onItemSelected = { typeInput = it },
            modifier = Modifier
                .padding(bottom = 25.dp)
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = urlInput,
            onValueChange = {urlInput = it},
            placeholder = {Text(text = "Video URL")},
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
                if(nameInput == "" || typeInput == "" || urlInput == ""){
                    Toast.makeText(context, "All data fields are mandatory", Toast.LENGTH_SHORT).show()
                }
                else{
                    showAlert = true
                }
            }
        ){
            Text(
                fontSize = 25.sp,
                text="Save"
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
                Text("Video Saved")
            },
            text = {
                Text("Your video has been successfully saved.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showAlert = false
                        showAlertDialog = false
                        navController.navigate(route = "Home Screen")
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

}


@Composable
fun Spinner(
    context: Context,
    itemsResId: Int,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val resources = context.resources
    val items = resources.getStringArray(itemsResId)

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clickable { expanded = true }
            .background(Color.Gray)
    ) {

        Text(
            fontFamily = FontFamily.Serif,
            text = if (selectedItem.isNotEmpty()) selectedItem else "Select Type",
            modifier = Modifier
                .padding(16.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onItemSelected(item)
                    expanded = false
                }, text = {Text(text = item)})
            }
        }
    }
}




