package com.globe.hcj.data.firestore

import java.util.*

data class TextMessage(
        var messageId: String = "",
        var sendName: String = "",
        var profileURL : String = "",
        var unReadCount: Int = 1,
        var messageDate: Date? = null,
        var message: String = "",
        var sendEmail:String=""

) : IMessageInterface