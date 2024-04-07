package com.example.whisper.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whisper.model.AnnouncementChannel
import com.example.whisper.model.AnnouncementChannelRepositories
import com.example.whisper.model.UserAuth
import com.example.whisper.view_model.AnnouncementChannelViewModel
import com.example.whisper.view_model.AuthState

@Composable
fun AnnouncementChannelList(
    viewModel: AnnouncementChannelViewModel = AnnouncementChannelViewModel(),
    modifier: Modifier = Modifier
) {
    val value by remember { viewModel.repositories }
    val p = AuthState.current.auth

    LaunchedEffect(key1 = Unit) {
        viewModel.getAnnouncementChannels(p)
    }

    AnnouncementChannelListBody(
        value = value,
        modifier = modifier
    )
    NewAnnouncementPageButton()
    ManageSubscriptionButton()
}

@Composable
fun NewAnnouncementPageButton(modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    var nameSearch by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .padding(bottom = 16.dp)
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                showDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            contentPadding = PaddingValues(all = 25.dp),
        ) {
            Text("New Announcement Page")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("New Announcement Page") },
                text = {
                    TextField(
                        value = nameSearch,
                        onValueChange = { nameSearch = it },
                        label = { Text("Enter Channel Name") }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            //figure out how to handle
                            showDialog = false // Close after confirmation

                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun ManageSubscriptionButton(modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    var nameSearch by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .padding(bottom = 96.dp)
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                showDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            contentPadding = PaddingValues(all = 25.dp)
        ) {
            Text("Manage Subscriptions")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Manage Subscriptions") },
                text = {
                    TextField(
                        value = nameSearch,
                        onValueChange = { nameSearch = it },
                        label = { Text("Enter Channel Name") }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            //figure out how to handle
                            showDialog = false // Close after confirmation

                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun SubscriptionPage() {
    // Your page content
    Text(text = "This is the Subscription Page")
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
private fun AnnouncementChannelListPreview(
    modifier: Modifier = Modifier
) {
    val data: MutableState<AnnouncementChannelRepositories> =
    mutableStateOf(
        AnnouncementChannelRepositories(
            data = listOf()
        )
    )

    data.value = AnnouncementChannelRepositories(
        data = listOf(
            AnnouncementChannel("0", "Cool Page", "Wawdawd"),
            AnnouncementChannel("1", "Special", "WDAWDAWDWAWA"),
            AnnouncementChannel("2", "Tuvok's Words-o-Wisdom", "Awdawd")
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
            items(value.data) { announcementChannel: AnnouncementChannel ->
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