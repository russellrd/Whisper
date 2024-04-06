package com.example.whisper.ui.routes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color


@Composable
fun DirectMessagesRoute(
    userId: String,
    modifier: Modifier = Modifier
) {

    DirectMessagesScreen(
        userId = userId,
        modifier = modifier
    )
    
}

@Composable
fun LazyColumn(modifier: Modifier, content: () -> Unit) {

}

// Message data class to hold message information
data class Message(val id: Int, val content: String)

@Composable
fun DirectMessagesScreen(
    userId: String,
    modifier: Modifier = Modifier
) {
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Message>() }  //MESSAGES ARE GETTING STORED HERE TEMPORARILY
    var messageId by remember { mutableStateOf(0) }


    Box(modifier = modifier.fillMaxSize().padding(10.dp)) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 10.dp)
            .padding(bottom = 60.dp),
            reverseLayout = true // Newest messages at the bottom


        ) {
            items(messages) { message ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.End // Aligns the message boxes to the right
                ) {
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .background(Color(0xFFFFC0CB)) // Light pink background color
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = message.content,
                            color = Color.White // Choose a text color that contrasts well with the background
                        )
                    }
                }
            }
        }

            Text("$userId")


        MessageTypingBar(
            messageText = messageText,
            onMessageChange = { newText ->
                messageText = newText
            },
            onMessageSend = {  ///Can be changed to send to backend
                    // Add the message to the list
                    messages.add(Message(messages.size + 1, messageText)) // Adding new message to the list
                    val newMessage = Message(id = messageId, content = messageText)
                    messageId++ // Increment messageId for uniqueness
                    messageText = "" // Reset the text field after sending

            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth() // Ensure the typing bar spans the width of the screen
        )
    }
}



@Composable
fun MessageTypingBar(
    messageText: String, // Use this directly, no need for a local remember state
    onMessageChange: (String) -> Unit,
    onMessageSend: (String) -> Unit, // This lambda will be triggered when the send button is clicked.
    modifier: Modifier = Modifier
) {
    // Removed the local remember state here

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically // Aligns children vertically center.
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = onMessageChange, // Use the passed in onMessageChange
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message") },
            singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = { onMessageSend(messageText) } // Use the messageText passed to this composable
        ) {
            Text("Send")
        }
    }
}

