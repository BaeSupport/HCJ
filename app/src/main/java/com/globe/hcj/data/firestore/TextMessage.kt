package com.globe.hcj.data.firestore

import java.util.*

data class TextMessage(
        var messageId: String = "",
        var sendUserRef: UserRef? = null,
        var sendEmail: String = "",
        var sendName: String = "",
        var unReadCount: Int = 1,
        var messageDate: Date? = null,
        var message: String = ""

) : IMessageInterface