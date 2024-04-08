package com.example.whisper.ui.view

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

@Composable
fun MessageChannelList(
    mcViewModel: MessageChannelViewModel = MessageChannelViewModel(),
    authViewModel: AuthStateViewModel = viewModel(),
    navigateToDM: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val value by remember { mcViewModel.repositories }
    val auth: Auth by authViewModel.auth.collectAsStateWithLifecycle() // Create authentication object reference for API calls 

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
                                navigateToDM("3")
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

@Composable
fun NewChatButton(modifier: Modifier = Modifier) {
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
            contentPadding = PaddingValues(all = 25.dp)
        ) {
            Text("New Chat")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("New Chat") },
                text = {
                    TextField(
                        value = nameSearch,
                        onValueChange = { nameSearch = it },
                        label = { Text("Enter Name") }
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

//@SuppressLint("UnrememberedMutableState")
//@Composable
//private fun MessageChannelListPreview(
//    navigateToDM: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val data: MutableState<MessageChannelRepositories> =
//        mutableStateOf(
//            MessageChannelRepositories(
//                data = listOf()
//            )
//        )
//
//    data.value = MessageChannelRepositories(
//        data = listOf(
//            MessageChannel("c0651fd6-9433-42de-8a44-d5bc9b3ff06e", "Person1", "Person2"),
//            MessageChannel("ce358139-7ab3-4a35-a30e-0950f8a7ad8f", "Person2", "Person3"),
//            MessageChannel("d12561cf-a2b6-4184-be31-d4d44055a47e", "Person3", "Person1")
//        )
//    )
//
//    val value by remember { data }
//
//    MessageChannelListBody(
//        value = value,
//        modifier = modifier,
//        navigateToDM = navigateToDM
//    )
//}