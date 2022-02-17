package com.miso.chatapplication.model

data class ChatRoom (
    val user:User,
    val opponent:User,
    var messages:List<Message>)
{
}