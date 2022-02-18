package com.miso.chatapplication.model

data class ChatRoom (
    val users:Map<String,Boolean>?=HashMap(),
    var messages:List<Message>?= listOf<Message>()
)
{
}