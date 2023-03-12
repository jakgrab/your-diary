package com.example.yourdiary.model

import androidx.compose.ui.graphics.Color
import com.example.yourdiary.R
import com.example.yourdiary.ui.theme.*

enum class Affair(
    val icon: Int,
    val contentColor: Color,
    val containerColor: Color
) {
    Work(
        icon = R.drawable.briefcase,
        contentColor = Color.Black,
        containerColor = WorkColor
    ),
    Fun(
    icon = R.drawable.`fun`,
        contentColor = Color.White,
        containerColor = FunColor
    ),
    Reading(
        icon = R.drawable.book,
        contentColor = Color.White,
        containerColor = BookColor
    ),
    Sport(
        icon = R.drawable.soccer_ball,
        contentColor = Color.White,
        containerColor = SportsColor,
    ),
    House(
        icon = R.drawable.house,
        contentColor = Color.Black,
        containerColor = HouseColor
    ),
    Cooking(
        icon = R.drawable.cooking,
        contentColor = Color.Black,
        containerColor = CookingColor,
    ),
    Travel(
        icon = R.drawable.car,
        contentColor = Color.Black,
        containerColor = CarColor
    ),
    School(
        icon = R.drawable.backpack,
        contentColor = Color.White,
        containerColor = SchoolColor
    ),
    Project(
        icon = R.drawable.laptop,
        contentColor = Color.White,
        containerColor = LaptopColor
    ),
    WateringPlants(
        icon = R.drawable.plant,
        contentColor = Color.Black,
        containerColor = PlantColor
    ),
    Shopping(
        icon = R.drawable.shopping,
        contentColor = Color.White,
        containerColor = ShoppingColor
    )
//    Thoughts(
//        icon = R.drawable.not
//    )
}