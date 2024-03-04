package com.reactnativehintrequestpicker

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.google.android.gms.auth.api.identity.Identity
import javax.annotation.Nullable


class HintRequestPickerModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ActivityEventListener {
  private val hintPicker : HintRequestPicker
  init {
    reactContext.addActivityEventListener(this);
    hintPicker = HintRequestPicker(reactContext);
  }

  override fun getName(): String {
    return "HintRequestPicker"
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  fun getPhoneNumber() {
    hintPicker.getPhoneNumber();
  }

  @ReactMethod
  fun getGoogleAccount(clientId: String?) {
    Log.d("Hint Request Module", "Getting Email");
    hintPicker.getGoogleAccount(clientId);
  }

  private fun sendEvent(eventName: String,
                        @Nullable params: WritableMap) {
    reactContext
      .getJSModule(RCTDeviceEventEmitter::class.java) //supply the result in params
      .emit(eventName, params)
  }

  override fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {
    if(data == null){
      return;
    }
    if (requestCode == Constants.PHONE_PICKER_REQUEST) {
      val map = Arguments.createMap()
      if (resultCode == RESULT_OK) {
        if(activity != null) {
          val phoneNumber = Identity.getSignInClient(activity).getPhoneNumberFromIntent(data)
          map.putString("phoneNumber", phoneNumber);
        }
      } else {
        map.putString("phoneNumber", null);
      }
      sendEvent(Constants.PHONE_SELECTED_EVENT, map)
    }
    else if (requestCode == Constants.EMAIL_PICKER_REQUEST) {
      val map = Arguments.createMap()
//      if (resultCode == RESULT_OK) {
//        val credential: Credential? = data.getParcelableExtra(Credential.EXTRA_KEY)
//          if (credential !== null) {
//              val token = credential.idTokens;
//
//              if (token.size > 0) {
//                  map.putString("tokenId", token.first().idToken);
//              }
//
//              map.putString("givenName", credential.givenName);
//              map.putString("name", credential.name);
//              map.putString("id", credential.id);
//              map.putString("email", credential.id);
//              map.putString("familyName", credential.familyName);
//              map.putString("profilePictureUri", credential.profilePictureUri.toString());
//              map.putString("accountType", credential.accountType);
//          }
//      }
//      else {
//        map.putString("email", null);
//        map.putString("id", null);
//      }
      sendEvent(Constants.EMAIL_SELECTED_EVENT, map)
    }
  }

  override fun getConstants(): Map<String, Any> {
    val constants: MutableMap<String, Any> = HashMap()
    constants["PHONE_SELECTED_EVENT"] = Constants.PHONE_SELECTED_EVENT
    constants["EMAIL_SELECTED_EVENT"] = Constants.EMAIL_SELECTED_EVENT
    return constants
  }
  override fun onNewIntent(intent: Intent?) {}
}
