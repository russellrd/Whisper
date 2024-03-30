package com.example.whisper.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whisper.model.AnnouncementChannel
import com.example.whisper.model.AnnouncementChannelRepositories
import com.example.whisper.view_model.AnnouncementChannelViewModel

@Composable
fun AnnouncementChannelList(
    viewModel: AnnouncementChannelViewModel = AnnouncementChannelViewModel(),
    modifier: Modifier = Modifier
) {
    val value by remember { viewModel.repositories }

    LaunchedEffect(key1 = Unit) {
        viewModel.getAnnouncementChannels()
    }

    AnnouncementChannelListBody(
        value = value,
        modifier = modifier
    )
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
private fun AnnouncementChannelListPreview(
    viewModel: AnnouncementChannelViewModel = AnnouncementChannelViewModel(),
    modifier: Modifier = Modifier
) {
    val data: MutableState<AnnouncementChannelRepositories> =
    mutableStateOf(
        AnnouncementChannelRepositories(
            announcementChannels = listOf()
        )
    )

    data.value = AnnouncementChannelRepositories(
        announcementChannels = listOf(
            AnnouncementChannel("c0651fd6-9433-42de-8a44-d5bc9b3ff06e", "Cool Page", "Wawdawd"),
            AnnouncementChannel("ce358139-7ab3-4a35-a30e-0950f8a7ad8f", "Special", "WDAWDAWDWAWA"),
            AnnouncementChannel("d12561cf-a2b6-4184-be31-d4d44055a47e", "Tuvok's Words-o-Wisdom", "Awdawd")
        )
    )

    val value by remember { data }

    AnnouncementChannelListBody(
        value = value,
        modifier = modifier
    )
}

@Composable
private fun AnnouncementChannelListBody(
    value: AnnouncementChannelRepositories,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(bottom = 16.dp)
    ){
        LazyColumn(){
            items(value.announcementChannels) { announcementChannel: AnnouncementChannel ->
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable(
                            role = Role.Checkbox,
                            onClick = {
                                //TODO: Add event
                            }
                        ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.fillMaxWidth(),
                    ){
                        Column(
                            modifier = modifier
                                .padding(10.dp)
                                .fillMaxWidth(0.9f)
                        ) {
                            Text(
                                announcementChannel.title,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center,
                                fontSize = 25.sp
                            )
                            Text(
                                announcementChannel.department,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }
                        Checkbox(
                            checked = false,
                            onCheckedChange = null,
                        )
                    }
                }
            }
        }
    }
}