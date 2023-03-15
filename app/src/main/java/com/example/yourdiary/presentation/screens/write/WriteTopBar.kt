package com.example.yourdiary.presentation.screens.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.yourdiary.model.Diary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    diary: Diary?,
    onBackPressed: () -> Unit
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Arrow Icon"
                )
            }
        },
        title = {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Watering Plants",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "15 MAR 2023, 9:27 AM",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    ),
                    textAlign = TextAlign.Center
                )
            }
        },
        actions = {
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date Icon"
                )
            }
            if (diary != null) {

            }
        }
    )
}

@Composable
fun DeleteDiaryAction(
    selectedDiary: Diary?,
    onDeleteClicked: () -> Unit
) {


}