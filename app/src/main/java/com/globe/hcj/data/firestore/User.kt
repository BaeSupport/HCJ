package com.globe.hcj.data.firestore

import com.google.firebase.firestore.DocumentReference

/**
 * Created by baeminsu on 10/10/2018.
 */

data class User(
        var name: String = "",
        var email: String = "",
        var profileURL: String = "",
        var pair: String = "",
        var pairRefName: DocumentReference? = null,
        var roomId: String = "") {

}