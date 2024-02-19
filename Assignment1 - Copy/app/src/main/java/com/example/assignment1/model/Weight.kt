package com.example.assignment1.model



data class Weight(
    val date: String = "",
    val weightData: Double = 0.0,

    )

/*fun loadWeight(): List<Weight> {
    val weightList = mutableListOf<Weight>()

    for (i in DataSource.weights.indices) {
        val weight = DataSource.weights[i]
        weightList.add(weight)
    }
    return weightList
}

@Composable
fun saveWeight(date: String, weight: String){
    val newWeight = Weight(date, weight.toDouble())
    weights.add(newWeight)
}*/

