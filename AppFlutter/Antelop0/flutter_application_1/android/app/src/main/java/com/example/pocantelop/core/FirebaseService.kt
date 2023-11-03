package com.example.pocantelop.core

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import fr.antelop.sdk.firebase.AntelopFirebaseMessagingUtil


class FirebaseService: FirebaseMessagingService() {

    override fun onNewToken(s: String) {
        //forward 'new token' firebase messaging event to Entrust SDK
        AntelopFirebaseMessagingUtil.onTokenRefresh(baseContext)

        //Processing 'new token' event on app side....
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //forward 'message received' firebase messaging event to Entrust SDK
        if (AntelopFirebaseMessagingUtil.onMessageReceived(baseContext, remoteMessage)) {
            //Message caught by Entrust SDK, nothing to do
            return
        }

        //Message not caught by Entrust SDK , it must be processed by the app...
    }
}