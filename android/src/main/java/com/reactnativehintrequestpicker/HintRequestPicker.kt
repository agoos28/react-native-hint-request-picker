package com.reactnativehintrequestpicker
import android.app.PendingIntent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.facebook.react.bridge.ReactApplicationContext
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity

class HintRequestPicker(private val appContext: ReactApplicationContext) {
  fun getPhoneNumber() {
    val request: GetPhoneNumberHintIntentRequest = GetPhoneNumberHintIntentRequest.builder().build()
    Identity.getSignInClient(appContext.baseContext).getPhoneNumberHintIntent(request).addOnSuccessListener {
      result: PendingIntent ->
      try {
        appContext.currentActivity?.let { startIntentSenderForResult(it, result.intentSender, Constants.PHONE_PICKER_REQUEST, null, 0, 0, 0, Bundle()) }
      } catch (e: SendIntentException) {
        e.printStackTrace()
      }
    }
  }
  fun getGoogleAccount(clientId: String?) {
    if (clientId !== null) {
      Log.i("clientId", clientId)
    }
  }
}
