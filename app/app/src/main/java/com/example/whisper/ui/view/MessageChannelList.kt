package com.example.whisper.ui.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.whisper.crypto.CryptoSystem
import com.example.whisper.model.Auth
import com.example.whisper.model.MessageChannel
import com.example.whisper.view_model.AuthStateViewModel
import com.example.whisper.view_model.MessageChannelViewModel
import com.example.whisper.view_model.MessagesViewModel

//Display Message Channels
@Composable
fun MessageChannelList(
    mcViewModel: MessageChannelViewModel = MessageChannelViewModel(),
    authViewModel: AuthStateViewModel = viewModel(),
    navigateToDM: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val value by remember { mcViewModel.repositories }
    val auth: Auth by authViewModel.auth.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {

        if(CryptoSystem.authenticate(auth, com.example.whisper.crypto.Role.USER, authViewModel::logout))
            mcViewModel.getMessageChannels(auth = auth)
    }

    Box(
        modifier = modifier
            .padding(bottom = 16.dp)
            .fillMaxSize()
    ){
        LazyColumn(){
            items(value.data) { messageChannel: MessageChannel ->
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable(
                            role = Role.Checkbox,
                            onClick = {
                                //After clicking on a DM send DM information to new page to grab chat data
                                navigateToDM(messageChannel.user1,messageChannel.user2,messageChannel.id)
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
                                messageChannel.user2,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Left,
                                fontSize = 25.sp
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

        NewChatButton()

    }
}

//button that opens a dialog box to create a new DM
@Composable
fun NewChatButton(modifier: Modifier = Modifier,
        //navigateToDM: (String, String, String) -> Unit,
                  ) {
        //bool value to determine whether or not dialog box should be shown
    var showDialog by remember { mutableStateOf(false) }
    var nameSearch by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .padding(bottom = 16.dp)
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                //show the box when clicking the button
                showDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            contentPadding = PaddingValues(all = 25.dp)
        ) {
            Text("New Chat")
        }
        //Logic for what to do when inside the Dialog Box
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("New Chat") },
                text = {
                    TextField(
                        value = nameSearch,
                        onValueChange = { nameSearch = it },
                        label = { Text("Enter ID of User") }
                    )
                },
                //Logic for what to do after user ID is searched
                confirmButton = {
                    Button(
                        onClick = {

                            //navigateToDM(messageChannel.user1,nameSearch,messageChannel.id)
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
