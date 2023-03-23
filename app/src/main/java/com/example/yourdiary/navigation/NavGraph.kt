@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.yourdiary.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.yourdiary.model.Affair
import com.example.yourdiary.model.GalleryImage
import com.example.yourdiary.presentation.components.DisplayAlertDialog
import com.example.yourdiary.presentation.screens.auth.AuthenticationScreen
import com.example.yourdiary.presentation.screens.auth.AuthenticationViewModel
import com.example.yourdiary.presentation.screens.home.HomeScreen
import com.example.yourdiary.presentation.screens.home.HomeViewModel
import com.example.yourdiary.presentation.screens.write.WriteScreen
import com.example.yourdiary.presentation.screens.write.WriteViewModel
import com.example.yourdiary.util.Constants.APP_ID
import com.example.yourdiary.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.example.yourdiary.model.RequestState
import com.example.yourdiary.model.rememberGalleryState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController,
    onDateLoaded: () -> Unit
) {
    NavHost(startDestination = startDestination, navController = navController) {
        authenticationRoute(
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            },
            onDateLoaded = onDateLoaded
        )
        homeRoute(
            navigateToWrite = { navController.navigate(Screen.Write.route) },
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route)
            },
            onDateLoaded = onDateLoaded,
            navigateToWriteWithArgs = { diaryId ->
                navController.navigate(Screen.Write.passDiaryId(diaryId = diaryId))
            }
        )
        writeRoute(
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit,
    onDateLoaded: () -> Unit
) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val authenticated by viewModel.authenticated
        val loadingState by viewModel.loadingState
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        LaunchedEffect(key1 = Unit) {
            onDateLoaded()
        }

        AuthenticationScreen(
            authenticated = authenticated,
            loadingState = loadingState,
            oneTapState = oneTapState,
            messageBarState = messageBarState,
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
            onTokenIdReceived = { tokenId ->
                viewModel.signInWithMongoAtlas(
                    tokenId = tokenId,
                    onSuccess = {
                        messageBarState.addSuccess("Successfully Authenticated")
                        viewModel.setLoading(false)
                    },
                    onError = { exception ->
                        messageBarState.addError(Exception(exception))
                        viewModel.setLoading(false)
                    }
                )
            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
                viewModel.setLoading(false)
            },
            navigateToHome = navigateToHome
        )
    }
}

fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
    navigateToAuth: () -> Unit,
    onDateLoaded: () -> Unit
) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = viewModel()
        val diaries by viewModel.diaries

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = diaries) {
            if (diaries !is RequestState.Loading) {
                onDateLoaded()
            }
        }

        HomeScreen(
            diaries = diaries,
            drawerState = drawerState,
            onMenuClicked = {
                scope.launch { drawerState.open() }
            },
            onSignOutClicked = { signOutDialogOpened = true },
            navigateToWrite = navigateToWrite,
            navigateToWriteWithArgs = navigateToWriteWithArgs
        )

        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure you want to Sign Out from your Google Account?",
            dialogOpened = signOutDialogOpened,
            onCloseDialog = { signOutDialogOpened = false },
            onYesClicked = {
                scope.launch(Dispatchers.IO) {
                    val user = App.create(APP_ID).currentUser
                    if (user != null) {
                        user.logOut()
                        withContext(Dispatchers.Main) { navigateToAuth() }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
fun NavGraphBuilder.writeRoute(onBackPressed: () -> Unit) {

    composable(
        route = Screen.Write.route,
        arguments = listOf(
            navArgument(name = WRITE_SCREEN_ARGUMENT_KEY) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
        val viewModel: WriteViewModel = viewModel()
        val uiState = viewModel.uiState
        val context = LocalContext.current

        LaunchedEffect(key1 = uiState) {
            Log.d("SelectedDiary", "${uiState.selectedDiaryId}")
        }

        val pagerState = rememberPagerState()
        val galleryState = rememberGalleryState()
        val pageNumber by remember { derivedStateOf { pagerState.currentPage } }

        WriteScreen(
            uiState = uiState,
            affairName = { Affair.values()[pageNumber].name },
            pagerState = pagerState,
            galleryState = galleryState,
            currentPage = pageNumber,
            onTitleChanged = {
                viewModel.setTitle(it)
            },
            onDescriptionChanged = {
                viewModel.setDescription(it)
            },
            onBackPressed = onBackPressed,
            onDeleteConfirmed = {
                viewModel.deleteDiary(
                    onSuccess = {
                        Toast.makeText(context, "Diary Deleted", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    },
                    onError = {error ->
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onDateAndTimeUpdated = {
                viewModel.updateDateAndTime(zonedDateTime = it)
            },
            onSaveClicked = {
                viewModel.updateOrInsertDiary(
                    diary = it,
                    onSuccess = {
                        onBackPressed()
                    },
                    onError = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onImageSelected = {image ->
                galleryState.addImage(
                    GalleryImage(image)
                )
            }
        )
    }
}