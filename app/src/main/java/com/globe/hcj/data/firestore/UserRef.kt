package com.globe.hcj.data.firestore

import com.google.firebase.firestore.DocumentReference

data class UserRef(
        var user: DocumentReference? = null,
        var email: String = "",
        var name: String = ""
) {}