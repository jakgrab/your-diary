package com.example.yourdiary.presentation.screens.write

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.yourdiary.model.Affair
import com.example.yourdiary.model.Diary
import com.example.yourdiary.model.GalleryState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import java.time.ZonedDateTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun WriteScreen(
    uiState: UiState,
    affairName: () -> String,
    pagerState: PagerState,
    galleryState: GalleryState,
    currentPage: Int,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onDateAndTimeUpdated: (ZonedDateTime) -> Unit,
    onBackPressed: () -> Unit,
    onSaveClicked: (Diary) -> Unit,
    onImageSelected: (Uri) -> Unit
) {
    // update the affair when selecting an existing Diary
    LaunchedEffect(key1 = uiState.affair) {
        pagerState.scrollToPage(Affair.valueOf(uiState.affair.name).ordinal)
    }

    Scaffold(
        topBar = {
            WriteTopBar(
                selectedDiary = uiState.selectedDiary,
                affairName = affairName,
                onDateAndTimeUpdated = onDateAndTimeUpdated,
                onBackPressed = onBackPressed,
                onDeleteConfirmed = onDeleteConfirmed
            )
        }
    ) {
        WriteContent(
            uiState = uiState,
            pagerState = pagerState,
            galleryState = galleryState,
            currentPage = currentPage,
            title = uiState.title,
            onTitleChanged = onTitleChanged,
            description = uiState.description,
            onDescriptionChanged = onDescriptionChanged,
            paddingValues = it,
            onSaveClicked = onSaveClicked,
            onImageSelected = onImageSelected
        )
    }
}