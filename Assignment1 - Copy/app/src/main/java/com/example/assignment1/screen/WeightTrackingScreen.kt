package com.example.assignment1.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Box

import androidx.compose.ui.Alignment

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.assignment1.FirebaseViewModel

import com.example.assignment1.model.Video
import com.example.assignment1.model.Weight


@Composable
fun weightTrackingScreen(navController: NavHostController, viewModel: FirebaseViewModel) {
    var clicked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var weights by remember { mutableStateOf(emptyList<Weight>()) }

    viewModel.retrieveWeightData(
        context
    ) { result ->
        result.onSuccess {
                weightList ->
            weights = weightList
        }.onFailure {
            //Toast.makeText(context, exception.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
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
            text = "  Your Weight Log",
            fontSize = 30.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp)
        )
    }
    if(weights.isNotEmpty()){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
        ) {
            WeightList(
                weightList = weights,
                modifier = Modifier.padding(bottom = 100.dp, top = 50.dp),
            )
        }
    }
    else{
        Text(text = "Empty",
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier
                .padding(start = 100.dp, top = 100.dp)
        )
    }
    Box(
        contentAlignment = Alignment.BottomEnd,
    ) {
        IconButton(modifier = Modifier
            .padding(bottom = 130.dp, end = 30.dp)
            .height(75.dp)
            .width(75.dp)
            .background(Color(0xFF406F9E), CircleShape),
            onClick = { clicked = true}) {
            Icon(
                imageVector = Icons.Filled.Add,
                modifier = Modifier.size(100.dp),
                contentDescription = "add_button",
                tint = Color.White
            )
        }
    }

    if(clicked){
        navController.navigate("Weight Screen")
        clicked = false
    }
}

@Composable
fun WeightList(weightList: List<Weight>, modifier: Modifier=Modifier){
    LazyColumn(modifier= modifier){

        items(weightList){
                weight ->
            WeightCard(weight = weight,
                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
            )
        }
    }
}

@Composable
fun WeightCard(weight: Weight, modifier: Modifier=Modifier){
    Card(modifier = modifier){
        Row(modifier = Modifier
            .fillMaxWidth()){
            Box(modifier = Modifier
                .padding(start = 20.dp)
                .width(200.dp)){
                Text(
                    text = weight.date,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(modifier = Modifier){
                Text(
                    text = weight.weightData.toString() + " kg",
                    fontSize = 25.sp,
                    style = MaterialTheme.typography.headlineSmall
                )

            }
/*            if(itemIndex != 0){
                if(weights[itemIndex!!].weightData < weights[(itemIndex - 1)!!].weightData){
                    Box(modifier = Modifier
                        .padding(end = 15.dp, top = 5.dp)){
                        Text(
                            text = "(  -" + String.format("%.2f",(weights[(itemIndex - 1)!!].weightData - weights[itemIndex!!].weightData)) +"kg )",
                            fontSize = 20.sp,
                            color = Color(0xFF406F9E),
                            style = MaterialTheme.typography.headlineSmall
                        )

                    }
                }
                else if(weights[itemIndex!!].weightData > weights[(itemIndex - 1)!!].weightData){
                    Box(modifier = Modifier
                        .padding(end = 15.dp, top = 5.dp)){
                        Text(
                            text = "( +" + String.format("%.2f",(weights[itemIndex!!].weightData - weights[(itemIndex-1)!!].weightData)) +"kg )",
                            fontSize = 20.sp,
                            color = Color(0xFF406F9E),
                            style = MaterialTheme.typography.headlineSmall
                        )

                    }
                }
            }*/

        }
    }
}
