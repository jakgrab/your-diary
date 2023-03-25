package com.example.yourdiary.presentation.screens.write

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.yourdiary.model.Affair
import com.example.yourdiary.model.Diary
import com.example.yourdiary.model.GalleryState
import com.example.yourdiary.presentation.components.GalleryUploader
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WriteContent(
    uiState: UiState,
    pagerState: PagerState,
    galleryState: GalleryState,
    currentPage: Int,
    title: String,
    onTitleChanged: (String) -> Unit,
    description: String,
    onDescriptionChanged: (String) -> Unit,
    paddingValues: PaddingValues,
    onSaveClicked: (Diary) -> Unit,
    onImageSelected: (Uri) -> Unit
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = scrollState.maxValue) {
        scrollState.scrollTo(scrollState.maxValue)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(bottom = 24.dp)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                HorizontalPager(
                    modifier = Modifier
                        .height(140.dp)
                        .clip(Shapes().medium),
                    state = pagerState,
                    count = Affair.values().size
                ) { page ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Affair.values()[page].containerColor)
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            modifier = Modifier.size(120.dp),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(Affair.values()[page].icon)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Affair Image"
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
                CurrentPageIndicator(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    length = Affair.values().size,
                    pagerPosition = currentPage
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = onTitleChanged,
                placeholder = {
                    Text(text = "Title")
                },
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Unspecified,
                    disabledIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Unspecified,
                    placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(scrollState.maxValue)
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    }
                ),
                maxLines = 1,
                singleLine = true
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = onDescriptionChanged,
                placeholder = {
                    Text(text = "Add some description")
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Unspecified,
                    disabledIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Unspecified,
                    placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.clearFocus() }
                )
            )
        }

        Column(verticalArrangement = Arrangement.Bottom) {
            Spacer(modifier = Modifier.height(12.dp))
            GalleryUploader(
                galleryState = galleryState,
                onAddClicked = { focusManager.clearFocus() },
                onImageSelected = onImageSelected,
                onImageClicked = {}
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = Shapes().small,
                onClick = {
                    if (uiState.title.isNotEmpty() && uiState.description.isNotEmpty()) {
                        onSaveClicked(
                            Diary().apply {
                                this.title = uiState.title
                                this.description = uiState.description
                                this.affair = Affair.values()[currentPage].name
                            }
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Fields cannot be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
private fun CurrentPageIndicator(
    modifier: Modifier = Modifier,
    length: Int,
    pagerPosition: Int
) {
    Row(modifier = modifier) {
        repeat(length) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(
                        if (it == pagerPosition) MaterialTheme.colorScheme.tertiary
                        else MaterialTheme.colorScheme.onSurface
                    )

            )
            if (it != length - 1) {
                Spacer(modifier = Modifier.width(3.dp))
            }
        }
    }
}

@Preview
@Composable
fun CurrentPageIndicatorPreview() {
    CurrentPageIndicator(length = 6, pagerPosition = 4)
}



