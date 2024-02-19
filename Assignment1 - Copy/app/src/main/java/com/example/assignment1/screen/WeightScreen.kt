package com.example.assignment1.screen


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.navigation.NavHostController
import com.example.assignment1.FirebaseViewModel
import com.example.assignment1.R
import com.example.assignment1.model.User
import com.example.assignment1.model.Weight
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightScreen(navController: NavHostController,
                 viewModel: FirebaseViewModel){
    var showAlert by remember { mutableStateOf(false) }
    var showAlertDialog by remember { mutableStateOf(false) }

    var dateInput by remember { mutableStateOf("") }
    var weightInput by remember { mutableStateOf("") }
    val context  = LocalContext.current

    val calendarState = rememberSheetState()

    val disabledDates = mutableListOf<LocalDate>()
    //device is follow the US date
    val currentDate = LocalDate.now().plusDays(1)
    var currentDateToAdd = currentDate
    //LocalDate.MAX is too big, system can't afford
    val endDate = LocalDate.now().plusYears(10)

    while (currentDateToAdd <= endDate) {
        disabledDates.add(currentDateToAdd)
        currentDateToAdd = currentDateToAdd.plusDays(1)
    }

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            style = CalendarStyle.MONTH,

            disabledDates = disabledDates
        ),
        selection = CalendarSelection.Date {
                date -> dateInput = date.toString()
        })

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)){
        IconButton(modifier = Modifier
            .padding(start = 15.dp, top = 15.dp, bottom = 40.dp)
            .height(50.dp)
            .width(50.dp),
            onClick = {
                navController.popBackStack()}) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                modifier = Modifier.size(100.dp),
                contentDescription = "back_button",
                tint = Color.Black

            )
        }
        Column(
            modifier = Modifier.padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){

            val img = painterResource(R.drawable.weight__1_)
            Image(
                painter = img,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp))


            Text(
                text = "Record Your Weight",
                fontSize = 25.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 20.dp, top = 30.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )
            Row(modifier = Modifier
                .padding(bottom = 20.dp)) {
                IconButton(modifier = Modifier
                    .border(1.dp, color = Color.Black)
                    .padding(top = 5.dp)
                    .height(50.dp)
                    .width(50.dp),
                    onClick = { calendarState.show()}) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        modifier = Modifier.size(100.dp),
                        contentDescription = "back_button",
                        tint = Color.Black
                    )
                }
                OutlinedTextField(
                    value = dateInput,
                    onValueChange = {dateInput = it},
                    placeholder = {Text(text = "Pick a date")},
                    singleLine = true,
                    enabled = false,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Black, // Change this to your desired color
                        unfocusedBorderColor = Color.Black // Change this to your desired color
                    ),
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .fillMaxWidth()
                )
            }

            OutlinedTextField(
                value = weightInput,
                onValueChange = {weightInput = it},
                placeholder = {Text(text = "Input your weight in kg")},
                singleLine = true,
                modifier = Modifier
                    .padding(bottom = 25.dp)
                    .fillMaxWidth())

            Button(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth(),

                onClick = {
                    if(dateInput == "" || weightInput == ""){
                        Toast.makeText(context, "All data fields are mandatory", Toast.LENGTH_SHORT).show()
                    }else{
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
    }

    if (showAlert) {
        if(!showAlertDialog){
            viewModel.checkExistDate(dateInput, context){
                    isDateAvailable ->
                if(isDateAvailable){
                    val weight = Weight(dateInput,weightInput.toDouble())
                    viewModel.saveWeightToFirebase(weight,context)
                }
                else{
                    Toast.makeText(context, "Weight for this date already recorded!", Toast.LENGTH_SHORT).show()
                }
            }
            showAlertDialog = true
        }

        AlertDialog(
            onDismissRequest = {
                showAlert = false
            },
            title = {
                Text("Saved")
            },
            text = {
                Text("Weight has been successfully recorded.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showAlert = false
                        showAlertDialog = false
                        navController.navigate(route = "Weight Tracking Screen")
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

}



