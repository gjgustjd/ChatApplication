package com.miso.chatapplication.model

data class Message(
    var senderUid:String,
    var receiverUid:String,
    var sended_date:String,
    var content:String)
{
}