package com.example.yourdiary.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.yourdiary.model.Affair
import com.example.yourdiary.model.Diary
import com.example.yourdiary.ui.theme.Elevation
import com.example.yourdiary.util.toInstant
import io.realm.kotlin.ext.realmListOf
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalTime
import java.util.*

@Composable
fun DiaryHolder(diary: Diary, onClick: (String) -> Unit) {
    var componentHeight by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current
    var galleryOpened by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick(diary._id.toString()) }
    ) {
        Spacer(modifier = Modifier.width(14.dp))
        Surface(
            modifier = Modifier
                .width(2.dp)
                .height(componentHeight + 14.dp),
            tonalElevation = Elevation.Level1
        ) {}
        Spacer(modifier = Modifier.width(20.dp))

        Surface(
            modifier = Modifier
                .clip(shape = Shapes().medium)
                .onGloballyPositioned {
                    componentHeight = with(localDensity) { it.size.height.toDp() }
                },
            tonalElevation = Elevation.Level1
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                DiaryHeader(
                    title = diary.title,
                    affairName = diary.affair,
                    time = diary.date.toInstant()
                )
                Text(
                    text = diary.description,
                    modifier = Modifier.padding(14.dp),
                    style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis
                )
                if (diary.images.isNotEmpty()) {
                    ShowGalleryButton(
                        galleryOpened = galleryOpened,
                        onClick = {
                            galleryOpened = !galleryOpened
                        }
                    )
                }
                AnimatedVisibility(
                    visible = galleryOpened,
                    enter = fadeIn() + expandVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                    exit = shrinkVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessVeryLow
                        )
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Gallery(images = diary.images)
                    }
                }
            }
        }

    }
}

@Composable
fun DiaryHeader(title: String, affairName: String, time: Instant) {
    val affair by remember { mutableStateOf(Affair.valueOf(affairName)) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(affair.containerColor)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(3f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = affair.icon),
                contentDescription = "Affair icon"
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = title,
                color = affair.contentColor,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
        }
        Text(
            modifier = Modifier.weight(1f),
            text = SimpleDateFormat("hh:mm a", Locale.US)
                .format(Date.from(time)),
            color = affair.contentColor,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize
        )
    }
}

@Composable
fun ShowGalleryButton(
    galleryOpened: Boolean,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Text(
            text = if (galleryOpened) "Hide Gallery" else "Show Gallery",
            style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize)
        )
    }
}

@Preview
@Composable
fun DiaryHolderPreview() {
    DiaryHolder(diary = Diary().apply {
        title = "My Diary"
        description =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                    " Vivamus in urna neque. Aliquam iaculis leo vitae eros " +
                    "porttitor elementum. Vivamus neque ipsum, malesuada in congue" +
                    " sit amet, ornare in est. Nunc dapibus egestas tincidunt. " +
                    "Maecenas non dolor in elit lacinia egestas quis sed leo. " +
                    "Sed eu massa vitae mauris vestibulum venenatis. Integer " +
                    "scelerisque tincidunt quam. Duis vitae molestie lacus. " +
                    "Suspendisse lobortis, dui eget placerat pretium, eros eros " +
                    "porta sapien, eu maximus tellus urna quis est. Nullam vel sapien " +
                    "et neque ultrices placerat. Etiam ac bibendum libero."
        affair = Affair.WateringPlants.name
        images = realmListOf("", "")
    }, onClick = {})
}