package com.miso.chatapplication.model

import android.os.Parcelable
import java.io.Serializable

data class ChatRoom(
    val users: Map<String, Boolean>? = HashMap(),
    var messages: List<Message>? = listOf<Message>()
) : Serializable {
}